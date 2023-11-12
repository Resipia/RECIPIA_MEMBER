package com.recipia.member.aws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class SqsListenerService {

    private final ObjectMapper objectMapper;

    @SqsListener(value = "${spring.cloud.aws.sqs.nickname-sqs-name}")
    public void receiveMessage(String messageJson) throws JsonProcessingException {

        JsonNode messageNode = objectMapper.readTree(messageJson);
        String topicArn = messageNode.get("TopicArn").asText();
        String messageContent = messageNode.get("Message").asText();

        // messageId 추출 및 로깅 (만약 메시지에 messageId 정보가 있다면)
        String messageId = messageNode.get("messageId").asText();
        log.info("Received message from SQS with messageId: {}", messageId);


        // Assuming the "Message" is also a JSON string, we parse it to print as JSON object
        JsonNode message = objectMapper.readTree(messageContent);

        log.info("Topic ARN: {}", topicArn);
        log.info("Message:  {}", message.toString());
    }


}
