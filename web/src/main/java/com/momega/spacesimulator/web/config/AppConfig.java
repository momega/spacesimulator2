package com.momega.spacesimulator.web.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.simulation.json.TimestampTypeAdapter;
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

    @Bean
    public Gson createGsonHttpMessageConverter() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter())
                .serializeNulls()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();
        return gson;
    }

}
