package com.proyecto.configuration;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerConfig {

    @Value("${server.http.port:9080}")
    private int httpPort;

    @Value("${server.port:9090}")
    private int httpsPort;

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };

        tomcat.addAdditionalTomcatConnectors(createHttpConnector());

        tomcat.addConnectorCustomizers(connector -> {
            connector.setXpoweredBy(false);
            connector.setAllowTrace(false);
            connector.setMaxParameterCount(1000);
            connector.setMaxPostSize(10485760); // 10MB
            connector.setMaxSavePostSize(10485760); // 10MB
        });

        return tomcat;
    }

    private Connector createHttpConnector() {
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setScheme("http");
        connector.setPort(httpPort);
        connector.setSecure(false);
        connector.setRedirectPort(httpsPort);

        // Configuraciones adicionales del conector HTTP
        connector.setProperty("relaxedPathChars", "[]{}|");
        connector.setProperty("relaxedQueryChars", "[]{}|");
        connector.setProperty("maxThreads", "200");
        connector.setProperty("acceptCount", "100");
        connector.setProperty("connectionTimeout", "20000");

        return connector;
    }
}