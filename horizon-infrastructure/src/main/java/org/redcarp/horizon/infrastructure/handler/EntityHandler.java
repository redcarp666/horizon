package org.redcarp.horizon.infrastructure.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

/**
 * 实体处理器
 *
 * @author redcarp
 * @date 2024/2/20
 */
public class EntityHandler implements MetaObjectHandler {
	@Override
	public void insertFill(MetaObject metaObject) {
		//todo 填充公共字段
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		//todo
	}
}
