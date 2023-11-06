package com.recipia.member.aws;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class SqsListenerService {

    private final ObjectMapper objectMapper;

    @SqsListener(value = "${spring.cloud.aws.sqs.nickname-sqs-name}")
    public void receiveMessage(String messageJson) {

        try {
            JsonNode messageNode = objectMapper.readTree(messageJson);
            String topicArn = messageNode.get("TopicArn").asText();
            String messageContent = messageNode.get("Message").asText();

            // Assuming the "Message" is also a JSON string, we parse it to print as JSON object
            JsonNode message = objectMapper.readTree(messageContent);

            System.out.println("Topic ARN: " + topicArn);
            System.out.println("Message: " + message.toString());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error parsing message JSON", e);
        }

    }

}
