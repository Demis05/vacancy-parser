package com.example.vacancyparser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ImportExecutorConfig {

    @Bean(destroyMethod = "shutdown")
    public ExecutorService detailFetchExecutor(TechstarsProperties properties) {
        int threadCount = Math.max(1, properties.http().detailThreadCount());
        return Executors.newFixedThreadPool(threadCount);
    }
}
