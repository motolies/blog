package kr.hvy.blog.util;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import kr.hvy.blog.model.SlackChannelType;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class SlackMessenger {

    private static final String SLACK_TOKEN = System.getenv("SLACK_BOT_TOKEN");
    private static final MethodsClient METHODS_CLIENT = Slack.getInstance().methods(SLACK_TOKEN);

    public static void send(String message, Exception e) {
        try {
            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(SlackChannelType.HVY_ERROR.getChannel())
                    .text(message + "\n" + e.getMessage() +"\n" + Arrays.toString(e.getStackTrace()))
                    .build();

            METHODS_CLIENT.chatPostMessage(request);

        } catch (SlackApiException | IOException ex) {
            log.error(ex.getMessage());
        }
    }

    public static void send(Exception e) {
        try {
            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(SlackChannelType.HVY_ERROR.getChannel())
                    .text(e.getMessage() +"\n" + Arrays.toString(e.getStackTrace()))
                    .build();

            METHODS_CLIENT.chatPostMessage(request);

        } catch (SlackApiException | IOException ex) {
            log.error(ex.getMessage());
        }
    }


    public static void send(SlackChannelType channel, String message) {

        try {

            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(channel.getChannel())
                    .text(message)
                    .build();

            METHODS_CLIENT.chatPostMessage(request);

        } catch (SlackApiException | IOException e) {
            log.error(e.getMessage());
        }
    }

    public static void send(String message) {

        try {

            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(SlackChannelType.HVY_NOTIFY.getChannel())
                    .text(message)
                    .build();

            METHODS_CLIENT.chatPostMessage(request);

        } catch (SlackApiException | IOException e) {
            log.error(e.getMessage());
        }
    }


}
