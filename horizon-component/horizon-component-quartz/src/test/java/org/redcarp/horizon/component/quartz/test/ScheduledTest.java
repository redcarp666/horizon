package org.redcarp.horizon.component.quartz.test;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduled注解用法示例
 *
 * @author redcarp
 * @date 2024/3/5
 */
@Component
@Log4j2
public class ScheduledTest {

	@Scheduled(cron = "*/3 * * * * ?") //每3秒
	public void myTask() {
		log.info("hello,{},{}", "horizon", "world");
	}
}
