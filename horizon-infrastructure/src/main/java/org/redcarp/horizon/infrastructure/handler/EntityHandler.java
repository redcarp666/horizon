package org.redcarp.horizon.infrastructure.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.redcarp.horizon.core.security.SecurityUserHolder;
import org.redcarp.horizon.core.util.EmptyUtils;
import org.springframework.beans.factory.annotation.Autowired;
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


	@Autowired(required = false)
	SecurityUserHolder securityUserHolder;

	@Override
	public void insertFill(MetaObject metaObject) {
		if (securityUserHolder != null) {
			String userName = securityUserHolder.getCurrentUserName();
			if (EmptyUtils.isNotEmpty(userName)) {
				this.strictInsertFill(metaObject, "createBy", String.class, userName);
				this.strictInsertFill(metaObject, "updateBy", String.class, userName);
			}
			this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
			this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
		}
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
		if (securityUserHolder != null) {
			String userName = securityUserHolder.getCurrentUserName();
			if (EmptyUtils.isNotEmpty(userName)) {
				this.strictInsertFill(metaObject, "updateBy", String.class, userName);
			}
			this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
		}
	}
}
