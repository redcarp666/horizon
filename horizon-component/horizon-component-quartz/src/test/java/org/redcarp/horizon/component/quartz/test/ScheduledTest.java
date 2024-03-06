package org.redcarp.horizon.component.quartz.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduled注解用法示例
 *
 * @author redcarp
 * @date 2024/3/5
 */
@Component
@Slf4j
public class ScheduledTest {

	@Scheduled(cron = "*/3 * * * * ?") //每3秒
	public void myTask() {
		log.info("hello,{},{}", "horizon", "world");
	}
}
