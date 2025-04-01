package org.redcarp.horizon.component.quartz.test;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

/**
 * 动态创建任务示例
 *
 * @author redcarp
 * @date 2024/3/6
 */
@SpringBootTest
@Log4j2
public class OrderTest {

	//建议将bean注入容器以复用
	@Autowired
	JobDetail orderCanceledJob;
	@Autowired
	Scheduler scheduler;

	@Test
	public void orderCreate() throws InterruptedException, SchedulerException {
		log.info("order created! Waiting 30s for user pay");
		long startTime = System.currentTimeMillis() + 30000L;  // 当前时间+30秒
		Date startAt = new Date(startTime);
		//指定触发器（什么时候触发）
		SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger().forJob(orderCanceledJob).startAt(startAt).withSchedule(
				SimpleScheduleBuilder.simpleSchedule()).build();
		simpleTrigger.getJobDataMap().put("orderId", "123456");
		scheduler.scheduleJob(simpleTrigger);
		Thread.sleep(40000);  // 暂停40秒等待结果
	}
}
