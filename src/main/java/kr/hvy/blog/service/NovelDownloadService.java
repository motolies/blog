package kr.hvy.blog.service;

import kr.hvy.blog.model.LinkInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * The type Novel download service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NovelDownloadService {

    private final TaskExecutor taskExecutor;

    @Async
    public void download() throws InterruptedException {

        List<LinkInfo> novelList = getNovelList("https://booktoki321.com/novel/222");

        for(LinkInfo linkInfo : novelList) {
            String content = downloadNovel(linkInfo.getLink(), "novel_content");
            System.out.println(content);
            Thread.sleep(1000);
        }

    }


    /**
     * 일반적인 사이트에서는 너무 많은 호출로 인해서 블럭당함
     */
    @Deprecated
    @Async
    public void completableFutureDownload() {
        List<LinkInfo> novelList = getNovelList("https://booktoki321.com/novel/222");
        // CompletableFuture로 감싸서 비동기로 다운로드하고 결과를 얻기
        List<CompletableFuture<String>> downloadFutures = novelList.stream()
                .map(linkInfo -> CompletableFuture.supplyAsync(() ->
                        downloadNovel(linkInfo.getLink(), "novel_content"), taskExecutor))
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

    private List<LinkInfo> getNovelList(String urlPath) {
        List<LinkInfo> links = new ArrayList<>();

        String listPage = getHtml(urlPath);
        Document document = Jsoup.parse(listPage);

        Elements liElements = document.select("li.list-item");

        for (Element liElement : liElements) {
            String index = liElement.attr("data-index");
            String link = liElement.select("div.wr-subject a.item-subject").attr("href");

            // LiInfo 클래스에 저장
            LinkInfo info = LinkInfo.builder()
                    .link(link)
                    .order(Integer.parseInt(index))
                    .build();

            links.add(info);

        }

        return links.stream().sorted().toList();
    }

    private String downloadNovel(String urlPath, String targetId) {
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

    private String getHtml(String urlString) {
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


}
