package br.com.sw2u.realmeet.config;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutorConfiguration {

    @Bean
    public Executor controllerExecutor(
        @Value("${meet.taskExecutor.pool.coreSize:20}") int corePoolSize,
        @Value("${meet.taskExecutor.pool.maxSize:20}") int maxPoolSize,
        @Value("${meet.taskExecutor.pool.queueCapacity:50}") int queueCapacity,
        @Value("${meet.taskExecutor.pool.keepAliveSeconds:60}") int keepAliveSeconds
    ) {
        return new ThreadPoolExecutor(
            corePoolSize,
            maxPoolSize,
            keepAliveSeconds,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(queueCapacity, true)
        );
    }
}
