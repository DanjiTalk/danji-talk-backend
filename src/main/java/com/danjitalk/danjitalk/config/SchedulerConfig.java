package com.danjitalk.danjitalk.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ConditionalOnProperty(name = "scheduler.enabled", havingValue = "true", matchIfMissing = true)
@EnableScheduling
public class SchedulerConfig {
}
