package org.redcarp.horizon.infrastructure.domain;

import cn.hutool.core.text.NamingCase;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.redcarp.horizon.infrastructure.annotation.Unique;
import org.redcarp.horizon.infrastructure.exception.HorizonBusinessException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

/**
 * 业务service实现类可继承该类
 *
 * @author redcarp
 * @date 2024/6/6
 */
public class HorizonBaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

	@Override
	public boolean save(T entity) {
		checkUniqueField(entity, false);
		return super.save(entity);
	}

	@Override
	public boolean updateById(T entity) {
		checkUniqueField(entity, true);
		return super.updateById(entity);
	}

	protected void checkUniqueField(T entity, boolean isUpdate) {
		Field[] allFields = ReflectUtil.getFields(entity.getClass());
		Optional<Field> idFiledOptional = Arrays.stream(allFields).filter(field -> field.isAnnotationPresent(TableId.class)).findFirst();
		if (idFiledOptional.isEmpty()) {
			return;
		}
		Field idField = idFiledOptional.get();
		idField.setAccessible(true);
		for (Field field : allFields) {
			if (field.isAnnotationPresent(Unique.class)) {
				Unique unique = field.getDeclaredAnnotation(Unique.class);
				QueryWrapper<T> wrapper = Wrappers.query();
				Long count;
				try {
					Object value = ReflectUtil.getFieldValue(entity, field);
					String column;
					if (StringUtils.isBlank(unique.column())) {
						column = NamingCase.toUnderlineCase(field.getName());
					} else {
						column = unique.column();
					}
					wrapper.eq(column, value);
					if (isUpdate) {
						wrapper.ne(idField.getAnnotation(TableId.class).value(), idField.get(entity));
					}
					count = getBaseMapper().selectCount(wrapper);
				} catch (Exception e) {
					continue;
				}
				if (count > 0) {
					throw new HorizonBusinessException(unique.code(), field.getName());
				}
			}
		}
	}
}
