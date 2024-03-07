package org.redcarp.horizon.infrastructure.configuration;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.redcarp.horizon.infrastructure.handler.EntityHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * bean配置公共类
 *
 * @author redcarp
 * @date 2024/2/20
 */
@Configuration
public class HorizonAppConfig {
	@Bean
	public MetaObjectHandler metaObjectHandler() {
		return new EntityHandler();
	}

}
