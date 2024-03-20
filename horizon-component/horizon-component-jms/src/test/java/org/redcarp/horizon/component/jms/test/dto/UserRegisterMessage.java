package org.redcarp.horizon.component.jms.test.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterMessage implements Serializable {
	private String id;
	private Integer sex;
}