package ru.glebpad.axiomustelegramapi.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String INFERENCE_REQUESTS_QUEUE = "axiomus.inference.requests";
    public static final String INFERENCE_RESULTS_QUEUE  = "axiomus.inference.results";

    @Bean
    public Queue inferenceRequestsQueue() {
        return new Queue(INFERENCE_REQUESTS_QUEUE, true);
    }

    @Bean
    public Queue inferenceResultsQueue() {
        return new Queue(INFERENCE_RESULTS_QUEUE, true);
    }
}