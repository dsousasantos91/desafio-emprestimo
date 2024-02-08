package com.dsousasantos91.pagamento.consumer;

import com.dsousasantos91.pagamento.service.EmprestimoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PagamentoEmprestimoConsumer {

    private static final String EMPRESTIMO_QUEUE = "emprestimo.queue";

    private final EmprestimoService emprestimoService;

    @RabbitListener(queues = EMPRESTIMO_QUEUE)
    public void receber(@Payload Long idEmprestimo) {
        log.info("Efetuar pagamento para o emprestimo de ID [{}].", idEmprestimo);
        this.emprestimoService.pagar(idEmprestimo);
        log.info("Pagamento do emprestimo de ID [{}] realizado com sucesso.", idEmprestimo);
    }
}
