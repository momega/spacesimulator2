package com.momega.spacesimulator.simulation;

import com.momega.spacesimulator.simulation.json.TimestampTypeAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.momega.spacesimulator.model.Timestamp;

/**
 * Created by martin on 7/19/15.
 */
@ComponentScan(basePackages = {"com.momega.spacesimulator.service", "com.momega.spacesimulator.simulation"})
public class TestConfig {

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
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
