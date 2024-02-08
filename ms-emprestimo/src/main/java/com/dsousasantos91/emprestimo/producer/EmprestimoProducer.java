package com.dsousasantos91.emprestimo.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmprestimoProducer {

    private static final String EMPRESTIMO_EXCHANGE = "emprestimo.exchange";
    private static final String EMPRESTIMO_ROUTINGKEY = "emprestimo.routingkey";

    private final RabbitTemplate rabbitTemplate;

    public void enviarEmprestimoParaPagamento(Long idEmprestimo) {
        log.info("Enviar emprestimo de ID [{}] para pagamento.", idEmprestimo);
        rabbitTemplate.convertAndSend(EMPRESTIMO_EXCHANGE, EMPRESTIMO_ROUTINGKEY, idEmprestimo);
        log.info("Emprestimo de ID [{}] enviada para pagamento com sucesso.", idEmprestimo);
    }
}
