package br.com.rhsalles.infrastructure.security.publishers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ImportaContaEventPublisher {

	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Value(value = "${app.broker.exchange.importaContaEvent}")
	private String exchangeImportaContaEvent;
	
	public void publishImportaContaEvent(String arquivo) {
		rabbitTemplate.convertAndSend(exchangeImportaContaEvent, "", arquivo);
	}
}
