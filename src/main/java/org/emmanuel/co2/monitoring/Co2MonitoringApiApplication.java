package org.emmanuel.co2.monitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class Co2MonitoringApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(Co2MonitoringApiApplication.class, args);
	}

}
