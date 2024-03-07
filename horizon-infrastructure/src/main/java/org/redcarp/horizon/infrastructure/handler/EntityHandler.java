package org.redcarp.horizon.infrastructure.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.redcarp.horizon.core.util.EmptyUtils;
import org.redcarp.horizon.security.jwt.handler.CurrentUserHolder;

import java.util.Date;

/**
 * 实体处理器
 *
 * @author redcarp
 * @date 2024/2/20
 */
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
	public void updateFill(MetaObject metaObject) {
		String userName = CurrentUserHolder.getCurrentUserName();
		if (EmptyUtils.isNotEmpty(userName)) {
			this.strictInsertFill(metaObject, "updateBy", String.class, userName);
		}
		this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
	}
}
