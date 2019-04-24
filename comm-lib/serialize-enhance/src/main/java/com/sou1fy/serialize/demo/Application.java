package com.sou1fy.serialize.demo;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
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
        public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
            //remove the default serialzier jackson
            converters.removeIf(converter -> converter instanceof MappingJackson2HttpMessageConverter);

            FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
            FastJsonConfig config = new FastJsonConfig();
            config.setSerializerFeatures(
                    // 保留map空的字段
                    SerializerFeature.WriteMapNullValue,
                    // 将String类型的null转成""
                    SerializerFeature.WriteNullStringAsEmpty,
                    // 将Number类型的null转成0
                    SerializerFeature.WriteNullNumberAsZero,
                    // 将List类型的null转成[]
                    SerializerFeature.WriteNullListAsEmpty,
                    // 将Boolean类型的null转成false
                    SerializerFeature.WriteNullBooleanAsFalse,
                    // 避免循环引用
                    SerializerFeature.DisableCircularReferenceDetect);

            converter.setFastJsonConfig(config);
            converter.setDefaultCharset(StandardCharsets.UTF_8);
            converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, new MediaType("application", "*+json")));
            converters.add(converter);
        }

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