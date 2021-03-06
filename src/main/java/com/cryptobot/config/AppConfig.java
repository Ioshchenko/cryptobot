package com.cryptobot.config;

import com.cryptobot.model.ExchangeConfig;
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
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@Configuration
@EnableScheduling
public class AppConfig implements SchedulingConfigurer {

    @Bean
    public FreeMarkerConfigurationFactoryBean getFreeMarkerConfiguration() {
        FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
        bean.setTemplateLoaderPath("classpath:/templates");
        return bean;
    }

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

    private Map<String, ExchangeConfig> getExchanges() {
        return Stream.of(getResources())
                .map(this::convert)
                .collect(Collectors.toMap(ExchangeConfig::getName, e -> e));
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

    private ExchangeConfig convert(Resource resource) {
        try {
            Config config = ConfigFactory.parseReader(new InputStreamReader(resource.getInputStream()));
            return ExchangeConfig.builder()
                    .name(config.getString("name"))
                    .url(config.getString("url"))
                    .build();
        } catch (IOException e) {
            log.error(e);
            throw new IllegalArgumentException("Error parsing config file");
        }

    }

}
