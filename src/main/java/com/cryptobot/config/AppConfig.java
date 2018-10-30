package com.cryptobot.config;

import com.cryptobot.model.Exchange;
import com.cryptobot.service.ExchangeService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@Configuration
@EnableScheduling
public class AppConfig implements SchedulingConfigurer {

    @Bean
    public ObjectMapper jacksonObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
        jsonMessageConverter.setObjectMapper(jacksonObjectMapper());
        messageConverters.add(jsonMessageConverter);
        restTemplate.setMessageConverters(messageConverters);
        return restTemplate;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }

    @Bean(destroyMethod = "shutdown")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(4);
    }

    @Bean
    public ExchangeService getExchangeService() {
        return ExchangeService.builder()
                .exchanges(getExchanges())
                .build();
    }

    private Map<String, Exchange> getExchanges() {
        return Stream.of(getResources())
                .map(this::convert)
                .collect(Collectors.toMap(Exchange::getName, e -> e));
    }

    private Resource[] getResources() {
        try {
            return new PathMatchingResourcePatternResolver()
                    .getResources("classpath*:/exchanges/*.conf");
        } catch (IOException e) {
            log.error(e);
            throw new IllegalStateException("Error load config files");
        }
    }

    private Exchange convert(Resource resource) {
        try {
            Config config = ConfigFactory.parseReader(new InputStreamReader(resource.getInputStream()));
            return Exchange.builder()
                    .name(config.getString("name"))
                    .url(config.getString("url"))
                    .pairs(getPairs(config.getConfig("pairs")))
                    .build();
        } catch (IOException e) {
            log.error(e);
            throw new IllegalArgumentException("Error parsing config file");
        }

    }

    private static Map<String, String> getPairs(Config config) {
        return config.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().unwrapped().toString()));
    }

}
