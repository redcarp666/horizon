package org.redcarp.horizon.core.web;

import lombok.Getter;
import lombok.Setter;
import cn.hutool.http.HttpStatus;

import java.io.Serializable;


/**
 * 响应信息主体
 *
 */
@Setter
@Getter
public class Response<T> implements Serializable {
	/**
	 * 成功
	 */
	public static final int SUCCESS = HttpStatus.HTTP_OK;
	/**
	 * 失败
	 */
	public static final int FAIL = HttpStatus.HTTP_INTERNAL_ERROR;
	private static final long serialVersionUID = 1L;
	private int code;

	private String msg;

	private T data;

	private String mdc;

	public static <T> Response<T> ok() {
		return restResult(null, SUCCESS, null);
	}

	public static <T> Response<T> ok(T data) {
		return restResult(data, SUCCESS, null);
	}

	public static <T> Response<T> ok(T data, String msg) {
		return restResult(data, SUCCESS, msg);
	}

	public static <T> Response<T> fail() {
		return restResult(null, FAIL, null);
	}

	public static <T> Response<T> fail(String msg) {
		return restResult(null, FAIL, msg);
	}

	public static <T> Response<T> fail(T data) {
		return restResult(data, FAIL, null);
	}

	public static <T> Response<T> fail(T data, String msg) {
		return restResult(data, FAIL, msg);
	}

	public static <T> Response<T> fail(int code, String msg) {
		return restResult(null, code, msg);
	}

	public static <T> Response<T> restResult(T data, int code, String msg) {
		Response<T> response = new Response<>();
		response.setCode(code);
		response.setData(data);
		response.setMsg(msg);
		return response;
	}

	public static <T> Boolean isError(Response<T> ret) {
		return !isSuccess(ret);
	}

	public static <T> Boolean isSuccess(Response<T> ret) {
		return Response.SUCCESS == ret.getCode();
	}

}
