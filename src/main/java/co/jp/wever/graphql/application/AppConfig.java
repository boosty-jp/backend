package co.jp.wever.graphql.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    LogSettingFilter logSettingFilterFilter() {

        LogSettingFilter filter = new LogSettingFilter();
        return filter;
    }
}

