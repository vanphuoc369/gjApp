package vn.greenglobal.core;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

public class CoreCommon extends CoreObject<Object> {
	protected static CoreCommon instance;
	
	public CoreCommon() {
		super();
		setCore();
		instance = this; 
	}
	
	@Bean
	public ServletRegistrationBean dispatcherServlet() {
		return dispatcherServlet("/b/*");
	}

	@Bean
	public FilterRegistrationBean characterEncodingFilterBean() {
		return characterEncodingFilter();
	}
}
