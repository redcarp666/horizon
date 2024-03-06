package org.redcarp.horizon.component.quartz.config;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.redcarp.horizon.component.quartz.job.OrderCanceledJob;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author redcarp
 * @date 2024/3/6
 */
@Configuration
public class JobConfig {
	@Bean
	public JobDetail orderCanceledJob() {
		//建议withIdentity要调用到，否则每次容器启动，quartz数据库都会新增一个jobDetail
		return JobBuilder.newJob(OrderCanceledJob.class).withIdentity("orderCanceledJob-1").storeDurably().storeDurably().build();
	}

}
