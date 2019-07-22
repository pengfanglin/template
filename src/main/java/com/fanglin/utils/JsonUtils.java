package com.fanglin.utils;

import com.fanglin.core.others.BusinessException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * JSON转换
 * @author 彭方林
 * @date 2019/4/3 16:34
 * @version 1.0
 **/
@Component
@Slf4j
@ConditionalOnClass(ObjectMapper.class)
public class JsonUtils {
	private static ObjectMapper objectMapper=null;

	public JsonUtils(ObjectMapper objectMapper) {
		log.info("JsonUtils配置成功");
		JsonUtils.objectMapper=objectMapper;
	}
	/**
	 * 将集合类型json字符串转换为java对象
	 */
	public static <T> T jsonToObject(String json, Class<T> collectionClass, Class<?> elementClass) {
		JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClass);
		try {
			return objectMapper.readValue(json, javaType);
		} catch (IOException e) {
			log.warn(e.getMessage());
			throw new BusinessException("JSON序列化异常");
		}
	}

	/**
	 * 简单json对象序列化为java对象
	 */
	public static <T> T jsonToObject(String json, Class<T> elementClass) {
		try {
			return objectMapper.readValue(json, elementClass);
		} catch (IOException e) {
			log.warn(e.getMessage());
			throw new BusinessException("JSON序列化异常");
		}
	}
	/**
	 * 简单java对象转json
	 */
	public static String objectToJson(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (IOException e) {
			log.warn(e.getMessage());
			throw new BusinessException("JSON序列化异常");
		}
	}
}
