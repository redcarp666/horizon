package org.redcarp.horizon.security.jwt.handler;

import cn.hutool.core.date.DateUtil;
import org.redcarp.horizon.component.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户黑名单
 *
 * @author redcarp
 * @date 2024/2/26
 */
@Component
public class BlacklistHandler {

	private final static String PREFIX = "BLACKLIST:";

	@Autowired
	RedisService redisService;

	public void addToBlacklist(String userId) {
		redisService.set(PREFIX + userId, DateUtil.now());
	}

	public void delFromBlacklist(String userId) {
		redisService.del(PREFIX + userId);
	}

	public String getBlackTime(String userId) {
		return redisService.get(PREFIX + userId);
	}

}
