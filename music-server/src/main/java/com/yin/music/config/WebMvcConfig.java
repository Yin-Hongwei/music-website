package com.yin.music.config;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

/**
 * Web MVC：CORS、本地静态资源、UTF-8 字符串响应。
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final MediaProperties mediaProperties;

    @Value("${app.security.cors.allowed-origins:http://localhost:3000}")
    private String allowedOriginsCsv;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] allowedOrigins = Arrays.stream(allowedOriginsCsv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);
        registry.addMapping("/**")
                .allowedOriginPatterns(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/avatarImages/**")
                .addResourceLocations(mediaProperties.resourceLocation(mediaProperties.imgDir("avatarImages")))
                .setCachePeriod(3600);
        registry.addResourceHandler("/img/singerPic/**")
                .addResourceLocations(mediaProperties.resourceLocation(mediaProperties.imgDir("singerPic")))
                .setCachePeriod(3600);
        registry.addResourceHandler("/img/songPic/**")
                .addResourceLocations(mediaProperties.resourceLocation(mediaProperties.imgDir("songPic")))
                .setCachePeriod(3600);
        registry.addResourceHandler("/song/**")
                .addResourceLocations(mediaProperties.resourceLocation(mediaProperties.songDir()))
                .setCachePeriod(3600);
        registry.addResourceHandler("/img/songListPic/**")
                .addResourceLocations(mediaProperties.resourceLocation(mediaProperties.imgDir("songListPic")))
                .setCachePeriod(3600);
        registry.addResourceHandler("/img/swiper/**")
                .addResourceLocations(mediaProperties.resourceLocation(mediaProperties.imgDir("swiper")))
                .setCachePeriod(3600);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter utf8 = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        utf8.setWriteAcceptCharset(false);
        if (!converters.isEmpty()) {
            converters.add(converters.get(0));
            converters.set(0, utf8);
        } else {
            converters.add(utf8);
        }
    }
}
