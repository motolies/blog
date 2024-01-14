package kr.hvy.blog.service;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class SlackService {

    @Value("${slack.token}")
    String SLACK_TOKEN;

    public void send(String message) {

        try {
            MethodsClient methods = Slack.getInstance().methods(SLACK_TOKEN);

            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel("#hvy")
                    .text(message)
                    .build();

            methods.chatPostMessage(request);

        } catch (SlackApiException | IOException e) {
            log.error(e.getMessage());
        }
    }


}
