package org.redcarp.horizon.infrastructure.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.redcarp.horizon.core.util.EmptyUtils;
import org.redcarp.horizon.security.jwt.handler.CurrentUserHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 实体处理器
 *
 * @author redcarp
 * @date 2024/2/20
 */
@Component
public class EntityHandler implements MetaObjectHandler {
	@Override
	public void insertFill(MetaObject metaObject) {
		String userName = CurrentUserHolder.getCurrentUserName();
		if (EmptyUtils.isNotEmpty(userName)) {
			this.strictInsertFill(metaObject, "createBy", String.class, userName);
			this.strictInsertFill(metaObject, "updateBy", String.class, userName);
		}
		this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
		this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
	}

	@Override
	public MetaObjectHandler strictFillStrategy(MetaObject metaObject, String fieldName, Supplier<?> fieldVal) {
		Object obj = fieldVal.get();
		if (Objects.nonNull(obj)) {
			metaObject.setValue(fieldName, obj);
		}
		return this;
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		String userName = CurrentUserHolder.getCurrentUserName();
		if (EmptyUtils.isNotEmpty(userName)) {
			this.strictInsertFill(metaObject, "updateBy", String.class, userName);
		}
		this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
	}
}
