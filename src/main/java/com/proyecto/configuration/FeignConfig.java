package com.proyecto.configuration;

import feign.Client;
import feign.RequestInterceptor;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import feign.hc5.ApacheHttp5Client;
import jakarta.servlet.http.HttpSession;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.net.ssl.SSLContext;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class FeignConfig {

    @Bean
    @Primary
    public Encoder feignFormEncoder() {
        return new SpringFormEncoder(new SpringEncoder(() -> {
            List<HttpMessageConverter<?>> converters = new ArrayList<>();
            converters.add(new ByteArrayHttpMessageConverter());
            converters.add(new ResourceHttpMessageConverter());
            converters.add(new MappingJackson2HttpMessageConverter());
            return new HttpMessageConverters(converters);
        }));
    }

    @Bean
    public Client feignClient() {
        try {
            // Crear SSLContext que confía en todos los certificados
            SSLContext sslContext = SSLContextBuilder.create()
                    .loadTrustMaterial(new TrustAllStrategy())
                    .build();

            // Crear SSLConnectionSocketFactory
            SSLConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactoryBuilder.create()
                    .setSslContext(sslContext)
                    .setHostnameVerifier((hostname, session) -> true)
                    .build();

            // Crear ConnectionManager con SSL
            HttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
                    .setSSLSocketFactory(sslSocketFactory)
                    .setMaxConnTotal(200)
                    .setMaxConnPerRoute(20)
                    .build();

            // Crear HttpClient
            CloseableHttpClient httpClient = HttpClients.custom()
                    .setConnectionManager(cm)
                    .build();

            return new ApacheHttp5Client(httpClient);
        } catch (Exception e) {
            throw new RuntimeException("Error configurando el cliente SSL", e);
        }
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpSession session = attributes.getRequest().getSession(false);
                if (session != null) {
                    String token = (String) session.getAttribute("JWT_TOKEN");
                    if (token != null) {
                        template.header("Authorization", "Bearer " + token);
                    }
                }
            }
        };
    }
}