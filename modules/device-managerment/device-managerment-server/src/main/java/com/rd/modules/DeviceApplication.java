/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Application
 * @author ThinkGem
 * @version 2018-10-13
 */
@SpringCloudApplication
@EnableFeignClients(basePackages={"com.rd.modules", "com.jeesite.modules"})
public class DeviceApplication extends SpringBootServletInitializer {
	
	public static void main(String[] args) {
		SpringApplication.run(DeviceApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		this.setRegisterErrorPageFilter(false); // 错误页面有容器来处理，而不是SpringBoot
		return builder.sources(DeviceApplication.class);
	}
	
}