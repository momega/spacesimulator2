package com.momega.spacesimulator.runnable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by martin on 7/19/15.
 */
@ComponentScan(basePackages = "com.momega.spacesimulator")
public class RunnableConfig {

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(20);
        return executor;
    }

}
