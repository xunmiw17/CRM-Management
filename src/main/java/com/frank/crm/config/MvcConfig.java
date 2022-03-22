package com.frank.crm.config;

import com.frank.crm.interceptor.NoLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The MvcConfig class configures the NoLoginInterceptor class to set which paths the interceptor needs to intercept.
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Bean // Lets the return value of the method be passed to IOC to be maintained
    public NoLoginInterceptor noLoginInterceptor() {
        return new NoLoginInterceptor();
    }

    /**
     * Adds interceptor and sets the paths that would need to be intercepted by the interceptor.
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(noLoginInterceptor())
                .addPathPatterns("/**") // Sets the path that needs to be intercepted (** means all)
                .excludePathPatterns("/css/**", "/images/**", "/js/**", "/lib/**", "/index", "/user/login"); // Sets the path that would not need to be intercepted
    }
}
