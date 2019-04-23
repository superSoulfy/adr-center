package com.sou1fy.serialize.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Slf4j
@Import(Application.MCVConfig.class)
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * register serializerModifier those you has customized
     */

    class MCVConfig implements WebMvcConfigurer {
        @Override
        public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
            converters.forEach(HttpMessageConverter -> {
                if (AbstractJackson2HttpMessageConverter.class.isAssignableFrom(HttpMessageConverter.getClass())) {
                    ObjectMapper objectMapper = ((AbstractJackson2HttpMessageConverter) HttpMessageConverter).getObjectMapper();
                    SerializerFactory serializerFactory = objectMapper.getSerializerFactory().withSerializerModifier(new SerializerModifier());
                    objectMapper.setSerializerFactory(serializerFactory);
                    log.info("HttpMessageConverter to be customized --> {}", HttpMessageConverter);
                }
            });
        }
    }
}