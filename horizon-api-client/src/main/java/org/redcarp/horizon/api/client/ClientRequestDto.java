package org.redcarp.horizon.api.client;

import lombok.Data;

/**
 * @author redcarp
 * @date 2024/3/13
 */
@Data
public class ClientRequestDto<T> {
	Page page;
	T data;

	@Data
	public static class Page {
		long size;
		long current;
	}
}
