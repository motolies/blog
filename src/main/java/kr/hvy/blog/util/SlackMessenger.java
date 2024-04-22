package kr.hvy.blog.util;

import static com.slack.api.model.block.Blocks.asBlocks;
import static com.slack.api.model.block.Blocks.header;
import static com.slack.api.model.block.Blocks.section;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.model.Attachment;
import com.slack.api.model.block.LayoutBlock;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kr.hvy.blog.model.SlackChannelType;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class SlackMessenger {

  private static final String SLACK_TOKEN = System.getenv("SLACK_BOT_TOKEN");
  private static final MethodsClient METHODS_CLIENT = Slack.getInstance().methods(SLACK_TOKEN);

  // todo : 나중에 용도에 따라서 기본 템플릿을 분리해봐야 겠다

  @NotNull
  private static List<LayoutBlock> getBlocks(Exception e, boolean isChannel) {
    String packageName = e.getStackTrace()[0].getClassName();
    String className = packageName.substring(packageName.lastIndexOf(".") + 1);
    String methodName = e.getStackTrace()[0].getMethodName();
    int lineNumber = e.getStackTrace()[0].getLineNumber();

    return asBlocks(
        isChannel ? section(section -> section.text(markdownText("<!channel>"))) : null,
        header(header -> header.text(plainText(e.getMessage()))),
        section(section -> section.text(markdownText(MessageFormat.format("*Package* {0}", packageName)))),
        section(section -> section.fields(Arrays.asList(
            markdownText(MessageFormat.format("*Class* {0}", className)),
            markdownText(MessageFormat.format("*Method* {0}", methodName)),
            markdownText(MessageFormat.format("*Line* {0}", String.valueOf(lineNumber)))
        )))
    );
  }

  public static void send(Exception e) {
    send(e, false);
  }

  public static void send(Exception e, boolean isChannel) {
    try {

      ChatPostMessageRequest request = ChatPostMessageRequest.builder()
          .channel(SlackChannelType.HVY_ERROR.getChannel())
          .blocks(getBlocks(e, isChannel))
          .attachments(Collections.singletonList(
              Attachment.builder()
                  .color("#ff0000") // 빨간색 (#rgb 또는 #rrggbb 형식)
                  .text(Arrays.toString(e.getStackTrace()))
                  .build()
          ))
          .build();

      METHODS_CLIENT.chatPostMessage(request);

    } catch (SlackApiException | IOException ex) {
      log.error(ex.getMessage());
    }
  }

  public static void send(SlackChannelType channel, String message, boolean isChannel) {
    try {
      List<LayoutBlock> blocks = new ArrayList<>();

      if (isChannel) {
        blocks.add(section(section -> section.text(markdownText("<!channel>"))));
      }

      blocks.addAll(Arrays.asList(
          section(section -> section.text(markdownText(message)))
      ));

      ChatPostMessageRequest request = ChatPostMessageRequest.builder()
          .channel(channel.getChannel())
          .blocks(blocks)
          .build();

      METHODS_CLIENT.chatPostMessage(request);

    } catch (SlackApiException | IOException e) {
      log.error(e.getMessage());
    }
  }



  public static void send(SlackChannelType channel, String message) {
    send(channel, message, false);
  }

  public static void send(String message, boolean isChannel) {
    send(SlackChannelType.HVY_NOTIFY, message, isChannel);
  }

  public static void send(String message) {
    send(SlackChannelType.HVY_NOTIFY, message);
  }


}
