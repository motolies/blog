package kr.hvy.blog.module.novel;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;
import kr.hvy.blog.infra.notify.SlackMessenger;
import kr.hvy.blog.module.novel.domain.Novel;
import kr.hvy.blog.module.novel.dto.LinkInfo;
import kr.hvy.blog.module.novel.dto.NovelDownRequest;
import kr.hvy.blog.module.novel.mapper.NovelMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * The type Novel download service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NovelService {

  private final TaskExecutor taskExecutor;

  private final NovelMapper novelMapper;
  private final NovelRepository novelRepository;

  private void saveAndUpdate(Novel novel) {
    // exception 발생시에도 저장되도록 @Transactional 제거
    Optional<Novel> findNovel = novelRepository.findByTitleAndSeq(novel.getTitle(), novel.getSeq());
    if (findNovel.isPresent()) {
      Novel find = findNovel.get();
      find.setContent(novel.getContent());
      novelRepository.save(find);
    } else {
      novelRepository.save(novel);
    }
  }


  @Async
  public void download(NovelDownRequest request) {

    try {
      List<Integer> seqList = novelMapper.findSeqByTitle(request.getTitle());

      List<LinkInfo> novelList = getNovelList(request.getListUrl());

      List<LinkInfo> novelRequireList = novelList.stream()
          .filter(linkInfo -> !seqList.contains(linkInfo.getSeq()))
          .sorted().toList();

      IntStream.range(0, novelRequireList.size())
          .forEach(index -> {
            try {
              LinkInfo linkInfo = novelRequireList.get(index);
              String content = downloadNovel(linkInfo.getLink(), "novel_content");

              Novel novel = Novel.builder()
                  .title(request.getTitle())
                  .seq(linkInfo.getSeq())
                  .content(content)
                  .build();

              saveAndUpdate(novel);

              String message = String.format("%s 다운로드 중 %d/%d 완료", request.getTitle(), index + 1, novelRequireList.size());
              // index가 10의 배수인 경우에만 노티피케이션 보내기
              if ((index + 1) % 10 == 0) {
                log.info(message);
                SlackMessenger.send(message);
              } else {
                log.info(message);
              }

              Thread.sleep(1000);
            } catch (Exception e) {
              log.error("download Exception title: {}", request.getTitle(), e);
              SlackMessenger.send(e);
            }
          });

      String message = String.format("%s 다운로드 완료", request.getTitle());

      log.info(message);
      SlackMessenger.send(message, true);
    } catch (Exception e) {
      log.error("download Exception title: {}", request.getTitle(), e);
      SlackMessenger.send(e);
    }
  }

  /**
   * 일반적인 사이트에서는 너무 많은 호출로 인해서 블럭당함
   */
  @Deprecated
  @Async
  public void completableFutureDownload(NovelDownRequest request) throws IOException {

    List<LinkInfo> novelList = getNovelList(request.getListUrl());

    // CompletableFuture로 감싸서 비동기로 다운로드하고 결과를 얻기
    List<CompletableFuture<String>> downloadFutures = novelList.stream()
        .map(linkInfo -> CompletableFuture.supplyAsync(() ->
        {
          try {
            return downloadNovel(linkInfo.getLink(), "novel_content");
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }, taskExecutor))
        .toList();

    // CompletableFuture.allOf()를 사용하여 모든 작업이 완료될 때까지 대기
    CompletableFuture<Void> allOf = CompletableFuture.allOf(downloadFutures.toArray(new CompletableFuture[0]));

    // 모든 작업이 완료될 때까지 대기
    allOf.join();

    List<String> contents = downloadFutures.stream()
        .map(CompletableFuture::join)
        .toList();

    contents.forEach(System.out::println);
  }

  private List<LinkInfo> getNovelList(String urlPath) throws IOException {
    List<LinkInfo> links = new ArrayList<>();

    String listPage = getHtml(urlPath);
    Document document = Jsoup.parse(listPage);

    Elements liElements = document.select("li.list-item");

    for (Element liElement : liElements) {
      String seq = liElement.select("div.wr-num").text(); // wr-num 클래스에서 텍스트를 가져옴
      String link = liElement.select("div.wr-subject a.item-subject").attr("href");

      // LiInfo 클래스에 저장
      LinkInfo info = LinkInfo.builder()
          .link(link)
          .seq(Integer.parseInt(seq))
          .build();

      links.add(info);
    }

    return links.stream().sorted().toList();
  }


  private String downloadNovel(String urlPath, String targetId) throws IOException {
    String contentPage = getHtml(urlPath);
    Document document = Jsoup.parse(contentPage);

    Element targetElement = document.getElementById(targetId);

    if (targetElement != null) {
      Elements paragraphs = targetElement.select("p"); // 모든 <p> 태그를 선택

      StringBuilder plainTextBuilder = new StringBuilder();

      for (Element paragraph : paragraphs) {
        String content = paragraph.text();

        // 각 단락의 내용을 StringBuilder에 추가하고 줄바꿈 처리
        plainTextBuilder.append(content).append("\n");
      }

      return plainTextBuilder.toString();
    } else {
      System.out.println("Element with ID " + targetId + " not found");
      return "";
    }

  }

  private String getHtml(String urlString) throws IOException {
    StringBuilder htmlContent = new StringBuilder();
    BufferedReader bufferedReader = null;

    try {
      // 가져올 웹페이지의 URL 설정
      URL url = new URL(urlString);

      // URLConnection을 열어서 연결 설정
      HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
      urlConnection.setRequestMethod("GET");

      // User-Agent 설정 (가짜 브라우저 정보를 추가하여 차단을 방지)
      urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");

      // 403 Forbidden 에러를 방지하기 위해 Referer 설정 (사이트 주소를 추가)
      urlConnection.setRequestProperty("Referer", url.getProtocol() + "://" + url.getHost());

      // InputStream으로부터 데이터를 읽기 위한 BufferedReader 생성
      bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

      // 한 줄씩 읽어와서 StringBuilder에 추가
      String inputLine;
      while ((inputLine = bufferedReader.readLine()) != null) {
        htmlContent.append(inputLine);
      }

    } catch (IOException e) {
      log.error("getHtml Download Exception Url: {}", urlString, e);
      throw e;
    } finally {
      // BufferedReader 닫기
      if (bufferedReader != null) {
        try {
          bufferedReader.close();
        } catch (IOException e) {
          log.error("Error closing BufferedReader", e);
        }
      }
    }

    return htmlContent.toString();
  }

  public InputStream getTxtFile(String title) {
    // title로 검색해서 txt 파일을 만들어서 내려준다
    List<Novel> novelList = novelRepository.findByTitleOrderBySeq(title);

    // Novel의 Content를 IO stream 으로 만들어서 리턴한다
    StringBuilder contentBuilder = new StringBuilder();
    for (Novel novel : novelList) {
      contentBuilder.append(novel.getContent()).append("\n").append("\n").append("\n");
    }

    // StringBuilder를 ByteArrayInputStream으로 변환
    return new ByteArrayInputStream(contentBuilder.toString().getBytes(StandardCharsets.UTF_8));

  }


}
