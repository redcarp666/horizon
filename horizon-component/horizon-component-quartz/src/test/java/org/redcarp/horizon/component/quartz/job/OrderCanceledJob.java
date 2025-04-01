package org.redcarp.horizon.component.quartz.job;

import lombok.extern.log4j.Log4j2;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

/**
 * @author redcarp
 * @date 2024/3/6
 */
@Log4j2
public class OrderCanceledJob implements Job {
	@Override
	public void execute(JobExecutionContext context) {
		var orderId = context.getMergedJobDataMap().get("orderId");
		log.info("get order from database,orderId is {}", orderId.toString());
		// get order service ,change order status
		log.info("order got! order canceled now");
	}
}
