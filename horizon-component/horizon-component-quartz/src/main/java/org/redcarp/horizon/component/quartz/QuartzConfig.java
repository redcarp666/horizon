package org.redcarp.horizon.component.quartz;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

/**
 * @author redcarp
 * @date 2024/3/5
 */
@Configuration
@EnableScheduling
public class QuartzConfig {

	@Bean
	@QuartzDataSource
	@ConfigurationProperties(prefix = "horizon.quartz.datasource")
	@ConditionalOnProperty(prefix = "spring.quartz", name = "job-store-type", havingValue = "jdbc")
	public DataSource quartzDataSource() {
		return DataSourceBuilder.create().build();
	}
}
