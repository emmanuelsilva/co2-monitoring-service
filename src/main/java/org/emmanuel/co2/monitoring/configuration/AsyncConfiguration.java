package org.emmanuel.co2.monitoring.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Configure Spring boot to disable async configuration for integration tests.
 **/
@Configuration
@EnableAsync
@Profile("!test")
public class AsyncConfiguration {
}
