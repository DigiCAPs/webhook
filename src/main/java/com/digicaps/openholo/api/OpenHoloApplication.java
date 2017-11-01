package com.digicaps.openholo.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.digicaps.openholo.repository")
public class OpenHoloApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(OpenHoloApplication.class, args);
	}
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(OpenHoloApplication.class);
    }
}
