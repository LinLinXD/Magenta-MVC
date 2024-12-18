package com.proyecto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableAspectJAutoProxy
@EnableScheduling
public class runmain {

    /**
     * Metodo principal que inicia la aplicación Spring Boot.
     *
     * @param args los argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        SpringApplication.run(runmain.class, args);
    }

}