package kr.co.pulmuone.bos.comm.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.co.pulmuone.bos.base.interceptor.AuthInterceptor;
import kr.co.pulmuone.v1.comm.framework.resolver.MultiFileUploadResolver;
import kr.co.pulmuone.v1.comm.util.SystemUtil;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("#{new Integer( '${app.static-resource-cache-period}')}")
    private Integer cachePeriod; // static resource 캐시 시간

    @Value("${app.storage.public.public-url-path}")
    private String publicUrlPath; // public 파일 접근 url

    @Value("${app.storage.public.public-root-location}")
    private String publicRootLocation; // public 파일의 최상위 저장경로

    @Autowired
    private MultiFileUploadResolver multiFileUploadResolver;

    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor()).addPathPatterns("/admin/**") // interceptor 적용 URL
                .excludePathPatterns("/admin/login/**")
                .excludePathPatterns("/admin/system/emailtmplt/**"); // interceptor 미적용 URL
        WebMvcConfigurer.super.addInterceptors(registry);
    }

    /* 리소스 핸들러 설정 - Static Resource의 외부 디렉토리 관리 */
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {

        // cachePeriod : 향후 Production 환경에서는 (604800 = 7일) 로 설정, local / dev 환경에서는 0 으로 설정하여 항상 최신 Resource 조회
        registry.addResourceHandler("/js/**").addResourceLocations("/js/").setCachePeriod(cachePeriod);
        registry.addResourceHandler("/file/**").addResourceLocations("/file/").setCachePeriod(cachePeriod);
        registry.addResourceHandler("/plugin/**").addResourceLocations("/plugin/").setCachePeriod(cachePeriod);
        registry.addResourceHandler("/contents/**").addResourceLocations("/contents/").setCachePeriod(cachePeriod);

        // public 파일의 ResourceHandler / ResourceLocations 지정
        SystemUtil.setResourceInfo(registry, publicUrlPath, publicRootLocation);

    }

    /* HandlerMethodArgumentResolver 설정 */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {

        argumentResolvers.add(multiFileUploadResolver); // 멀티파일 업로드 Resolver

    }

}
