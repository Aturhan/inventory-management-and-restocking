package com.abdullahturhan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Warehouse1ServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(Warehouse1ServiceApplication.class,args);
    }
}