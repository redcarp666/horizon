package org.redcarp.horizon.infrastructure.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.redcarp.horizon.core.security.SecurityUserHolder;
import org.redcarp.horizon.core.util.EmptyUtils;
import org.redcarp.horizon.infrastructure.domain.HorizonBaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

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
		Object originalObject = metaObject.getOriginalObject();
		if (originalObject instanceof HorizonBaseEntity) {
			HorizonBaseEntity horizonBaseEntity = (HorizonBaseEntity) originalObject;
			if (securityUserHolder != null) {
				String userName = securityUserHolder.getCurrentUserName();
				if (EmptyUtils.isNotEmpty(userName)) {
					horizonBaseEntity.setCreateBy(userName);
					horizonBaseEntity.setUpdateBy(userName);
				}
			}
			horizonBaseEntity.setCreateTime(new Date());
			horizonBaseEntity.setUpdateTime(new Date());
		}
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		Object originalObject = metaObject.getOriginalObject();
		if (originalObject instanceof HorizonBaseEntity) {
			HorizonBaseEntity horizonBaseEntity = (HorizonBaseEntity) originalObject;
			if (securityUserHolder != null) {
				String userName = securityUserHolder.getCurrentUserName();
				if (EmptyUtils.isNotEmpty(userName)) {
					horizonBaseEntity.setUpdateBy(userName);
				}
			}
			horizonBaseEntity.setUpdateTime(new Date());
		}
	}
}
