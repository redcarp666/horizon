package org.redcarp.horizon.core.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author ssh
 * @date 2025/3/13
 */
@Slf4j
public class CustomThreadPoolExecutor extends ThreadPoolExecutor {


	public CustomThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	protected void afterExecute(Runnable r, Throwable t) {
		super.afterExecute(r, t);
		if (t == null && r instanceof Future<?> && ((Future<?>) r).isDone()) {
			try {
				((Future<?>) r).get();
			} catch (CancellationException ce) {
				t = ce;
			} catch (ExecutionException ee) {
				t = ee.getCause();
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
			}
		}
		if (t != null) {
			log.error("任务异常执行线程: {}", Thread.currentThread().getName(), t);
		}

	}
}
