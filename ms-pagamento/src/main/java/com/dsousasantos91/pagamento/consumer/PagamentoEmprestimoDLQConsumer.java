package com.dsousasantos91.pagamento.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

@Slf4j
@Component
@RequiredArgsConstructor
public class PagamentoEmprestimoDLQConsumer {

    private final RabbitTemplate rabbitTemplate;
    private static final String X_RETRY_HEADER = "x-dlq-retry";
    private static final String EMPRESTIMO_QUEUE = "emprestimo.queue";
    private static final String EMPRESTIMO_QUEUE_DLQ = "emprestimo.queue-dlq";

    @RabbitListener(queues = EMPRESTIMO_QUEUE_DLQ)
    public void processar(@Payload Long idEmprestimo, @Headers Map<String, Object> headers) {
        Integer retryHeader = (Integer) headers.get(X_RETRY_HEADER);
        if (isNull(retryHeader)) retryHeader = 0;
        log.info("Reprocessando pagamento para o emprestimo de ID [{}]", idEmprestimo);
        if (retryHeader < 3) {
            Map<String, Object> updateHeaders = new HashMap<>(headers);
            int tryCount = retryHeader + 1;
            updateHeaders.put(X_RETRY_HEADER, tryCount);
            this.rabbitTemplate.convertAndSend(EMPRESTIMO_QUEUE, idEmprestimo);
            final MessagePostProcessor messagePostProcessor = message -> {
                MessageProperties messageProperties = message.getMessageProperties();
                updateHeaders.forEach(messageProperties::setHeader);
                return message;
            };
            log.info("Enviando emprestimo de ID [{}] para DLQ", idEmprestimo);
            this.rabbitTemplate.convertAndSend(EMPRESTIMO_QUEUE_DLQ, idEmprestimo, messagePostProcessor);
        }
    }
}
