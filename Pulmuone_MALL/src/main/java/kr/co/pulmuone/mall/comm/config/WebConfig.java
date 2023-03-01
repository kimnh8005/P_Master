package kr.co.pulmuone.mall.comm.config;

import kr.co.pulmuone.v1.comm.framework.resolver.MultiFileUploadResolver;
import kr.co.pulmuone.v1.comm.util.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.co.pulmuone.mall.comm.interceptor.AuthInterceptor;

import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Value("${app.storage.public.public-url-path}")
	private String publicUrlPath; // public 파일 접근 url

	@Value("${app.storage.public.public-root-location}")
	private String publicRootLocation; // public 파일의 최상위 저장경로

	@Autowired
	private MultiFileUploadResolver multiFileUploadResolver;

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/",
            "classpath:/resources/",
            "classpath:/static/",
            "classpath:/public/" };
	@Bean
	public AuthInterceptor authInterceptor() {
		return new AuthInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authInterceptor())
								.addPathPatterns("/","/*","/*/*","/*/*/*"); //interceptor 적용 URL
//								.excludePathPatterns("/*/*/**"); // interceptor 미적용 URL
		WebMvcConfigurer.super.addInterceptors(registry);
	}


	/* 리소스 핸들러 성정 - Static Resource의 외부 디렉토리 관리 */
	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		/*
		registry.addResourceHandler("/static/js/**").addResourceLocations("classpath:/js/").setCachePeriod(604800);//(604800 = 7일)
		registry.addResourceHandler("/static/image/**").addResourceLocations("classpath:/js/").setCachePeriod(604800);//(604800 = 7일)
		registry.addResourceHandler("/static/css/**").addResourceLocations("classpath:/js/").setCachePeriod(604800);//(604800 = 7일)
		registry.addResourceHandler("/static/file/**").addResourceLocations("classpath:/file/").setCachePeriod(604800);
		registry.addResourceHandler("/static/plugin/**").addResourceLocations("classpath:/plugin/").setCachePeriod(604800);
		registry.addResourceHandler("/static/contents/**").addResourceLocations("classpath:/contents/").setCachePeriod(604800);
		*/
		// public 파일의 ResourceHandler / ResourceLocations 지정
		SystemUtil.setResourceInfo(registry, publicUrlPath, publicRootLocation);
	}

	/* HandlerMethodArgumentResolver 설정 */
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {

		argumentResolvers.add(multiFileUploadResolver); // 멀티파일 업로드 Resolver

	}
}
