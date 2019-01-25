package com.zb.commons.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 
 * 针对API响应的数据交互对象
 * 
 * @author Edison Lyu
 *
 */
@Getter
@Setter
public class RestfulResultDto<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8156344850835366252L;

	/**
	 * 编码.如'200'
	 */
	private String code;
	
	/**
	 * 编码对应的文本信息.如'200'对应'OK'
	 */
	private String message;

	/**
	 * 回调类型
	 */
	private String type;
	
	/**
	 * 业务数据
	 */
	private T body;
	
	/**
	 * 数字签名
	 */
	private String digitalSign;

	public RestfulResultDto() {
	}

	public RestfulResultDto(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public RestfulResultDto(String code, String message, T body) {
		this.code = code;
		this.message = message;
		this.body = body;
	}
	
	public static <T> RestfulResultDto<T> succeed() {
		return new RestfulResultDto<>("200", "OK");
	}
	
	public static <T> RestfulResultDto<T> succeed(T body) {
		return new RestfulResultDto<>("200", "OK", body);
	}
	
	public static <T> RestfulResultDto<T> succeedNotify(String notifyType) {
		RestfulResultDto<T> resultDto = new RestfulResultDto<>("200", "OK");
		resultDto.setType(notifyType);
		return resultDto;
	}
	
	public static <T> RestfulResultDto<T> fail(String errorMsg) {
		return new RestfulResultDto<>("500", errorMsg);
	}
	
	public static <T> RestfulResultDto<T> fail(String errorCode, String errorMsg) {
		return new RestfulResultDto<>(errorCode, errorMsg);
	}

}
