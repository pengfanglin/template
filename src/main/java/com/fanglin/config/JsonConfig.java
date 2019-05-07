package com.fanglin.config;

import com.fanglin.core.others.AjaxSerializerModifier;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.text.SimpleDateFormat;

/**
 * 和JSON相关的配置（包含@ResponseBody序列化规则）
 * @author 彭方林
 * @date 2019/4/3 16:33
 * @version 1.0
 **/
@Configuration
public class JsonConfig {
	/**
	 * jackson对象声明
	 */
	@Bean
	public ObjectMapper objectMapper(){
		return baseObjectMapper();
	}

	/**
	 * 基本配置
	 */
	private ObjectMapper baseObjectMapper(){
		ObjectMapper objectMapper=new ObjectMapper();
		// 转换为格式化的json
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		//如果字段类型为空，不报错
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		//如果json中的字段在实体类中未出现，不报错
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper;
	}
	/**
	 * jackson对象声明
	 */
	@Bean("ajaxObjectMapper")
	public ObjectMapper ajaxObjectMapper(){
		ObjectMapper objectMapper=baseObjectMapper();
		// 为mapper注册一个带有SerializerModifier的Factory，针对值为null的字段进行特殊处理
		objectMapper.setSerializerFactory(objectMapper.getSerializerFactory().withSerializerModifier(new AjaxSerializerModifier()));
		return objectMapper;
	}
	/**
	 * 配置springMvc的默认序列化规则，对null对象做特殊处理
	 */
	@Bean
	public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		//获取springMvc默认的objectMapper
		ObjectMapper objectMapper = converter.getObjectMapper();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		objectMapper.setDateFormat(sdf);
		// 为mapper注册一个带有SerializerModifier的Factory，针对值为null的字段进行特殊处理
		objectMapper.setSerializerFactory(objectMapper.getSerializerFactory().withSerializerModifier(new AjaxSerializerModifier()));
		return converter;
	}
}
