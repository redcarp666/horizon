package org.redcarp.horizon.infrastructure.domain;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @author redcarp
 * @date 2024/3/8
 */
@Data
@Accessors(chain = true)
public class HorizonBaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.ASSIGN_ID)
	@Schema(name = "ID")
	private String id;

	@Schema(name = "创建人")
	@TableField(fill = FieldFill.INSERT)
	private String createBy;

	@Schema(name = "创建时间")
	@DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;

	@Schema(name = "更新人")
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private String updateBy;

	@Schema(name = "更新时间")
	@DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		HorizonBaseEntity that = (HorizonBaseEntity) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
