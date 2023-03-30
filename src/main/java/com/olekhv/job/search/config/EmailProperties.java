package com.olekhv.job.search.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

@Configuration
@Data
@ConfigurationProperties(prefix = "yaml")
@PropertySource(value = "classpath:application.yml")
public class EmailProperties {
    private Map<String, EmailMessage> emailMessageMap;

    @Data
    public static class EmailMessage {
        private String subject;
        private String body;
    }
}
