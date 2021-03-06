package com.cts.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cts.common.Constants;

@Configuration
public class RabbitMQConfig {

	@Bean
	public Exchange exchange() {
		return ExchangeBuilder.directExchange("FDC-SAGA-EXCHANGE").build();
	}

	@Bean
	public Queue queue() {
		return QueueBuilder.durable("FDC-FLIGHT-QUEUE").build();
	}

	@Bean
	public Binding binding() {
		//This is done so that depending upon the package type of the event the routing will happen. If you want to change the 
		//routing key from package to something else please define the bean public RoutingKeyResolver routingKeyResolver()
		return BindingBuilder.bind(queue()).to(exchange()).with(Constants.FLIGHT_KEY).noargs();
	}

	@Autowired
	public void configure(AmqpAdmin admin) {		
		admin.declareExchange(exchange());
		admin.declareQueue(queue());
		admin.declareBinding(binding());
	}
	
}
