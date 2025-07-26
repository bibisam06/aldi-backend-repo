package com.bibisam06.aldi.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
        @PropertySource("classpath:properties/env.properties") // local.properties 파일 소스 등록
})
public class PropertyConfig {
}
