package com.momega.spacesimulator.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * The application configuration. It instantiates all the services
 * Created by martin on 6/18/14.
 */
@Configuration
@ComponentScan(basePackages = {"com.momega.spacesimulator.service", "com.momega.spacesimulator.simulation"})
public class AppConfig {
	
	@Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(10);
        return executor;
    }

}
