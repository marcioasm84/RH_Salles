package br.com.rhsalles.infrastructure.security.consumers;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import br.com.rhsalles.application.services.ContaService;

@Component
public class ImportaContaConsumer {

	@Autowired
	ContaService contaService;
	
	@RabbitListener(
			bindings = @QueueBinding(
						value = @Queue(value = "${app.broker.queue.importaContaEventQueue.name}", durable = "true"),
						exchange = @Exchange(value="${app.broker.exchange.importaContaEvent}", type = ExchangeTypes.FANOUT, ignoreDeclarationExceptions = "true")
			)
	)
	public void listenImportaContaEvent(@Payload String arquivoCsv) {
		System.out.println("ImportaContaConsumer - importando contas de " + arquivoCsv);
		contaService.processarImportaContas(arquivoCsv);
	}
}
