package org.redcarp.horizon.infrastructure.domain;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @author redcarp
 * @date 2024/3/11
 */
public class RequestFactory {
	public static Request createNoPageRequest(Object data) {
		Request request = new Request<>();
		request.setPage(new Page(0, -1));
		request.setData(data);
		return request;
	}

}
