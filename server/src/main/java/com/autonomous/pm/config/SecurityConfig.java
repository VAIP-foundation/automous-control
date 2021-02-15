package com.autonomous.pm.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.autonomous.pm.auth.BaseSecurityHandler;
import com.autonomous.pm.auth.ajax.AjaxAuthenticationProvider;
import com.autonomous.pm.auth.ajax.filter.AjaxAuthenticationFilter;
import com.autonomous.pm.auth.jwt.JwtAuthenticationProvider;
import com.autonomous.pm.auth.jwt.filter.JwtAuthenticationFilter;
import com.autonomous.pm.auth.jwt.matcher.SkipPathRequestMatcher;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
@PropertySource(value = "classpath:application.properties")
// @EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private static final String LOGIN_ENTRY_POINT = "/api/v1/login";
	private static final String LOGOUT_ENTRY_POINT = "/api/v1/logout";
	private static final String REFRESH_TOKEN_ENTRY_POINT = "/api/v1/refresh";
    private static final String VRN_LOGIN_ENTRY_POINT = "/api/v1/authen";
    // private static final String HOME_ENTRY_POINT = "/api/v1/home/**";
    // private static final String API_v1_ENTRY_POINT = "/api/v1/**";

    private static final String ERROR_ENTRY_POINT = "/error";
   // private static final String TEST_ENTRY_POINT = "/test";
    // private static final String H2_CONSOLE_ENTRY_POINT = "/h2-console/**";
    private static final String RESOURCE_ENTRY_POINT = "/resource/**";
    private static final String BACKEND_ENTRY_POINT = "/backend/**";
    // private static final String ROOT_ENTRY_POINT = "/**";

    @Autowired
    private JwtAuthenticationProvider jwtProvider;

    @Autowired
    private AjaxAuthenticationProvider ajaxProvider;

    @Autowired
    private BaseSecurityHandler securityHandler;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${security.disabled.url:@null}")
    private String securityDisabledUrl;

    @Override
    public void configure(WebSecurity web) {

        String[] ignoring = {
        		RESOURCE_ENTRY_POINT 
                , BACKEND_ENTRY_POINT
        };
        
        web.ignoring().antMatchers(ignoring);

        if (securityDisabledUrl != null) {
            String[] disabled = securityDisabledUrl.split(",");
            if (disabled != null && disabled.length > 0) {
                web.ignoring().antMatchers(disabled);
            }
        }
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(ajaxProvider).authenticationProvider(jwtProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	String[] permitAllEndpointList = {
			"/oauth/token"
			, LOGIN_ENTRY_POINT
			, LOGOUT_ENTRY_POINT
			, REFRESH_TOKEN_ENTRY_POINT
			, VRN_LOGIN_ENTRY_POINT
			, ERROR_ENTRY_POINT
    	};
    	
        http
        	.csrf().disable()
        	.addFilterBefore(ajaxAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtAuthenticationFilter(), FilterSecurityInterceptor.class)
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .authorizeRequests()
		            // springboot security 에서 cors 설정
		            // https://oddpoet.net/blog/2017/04/27/cors-with-spring-security/
		            .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
		            .antMatchers(permitAllEndpointList).permitAll()
            .and()
            	.cors();

    }

    @Bean
    public AntPathRequestMatcher antPathRequestMatcher() {
        return new AntPathRequestMatcher(LOGIN_ENTRY_POINT, HttpMethod.POST.name());
    }

    @Bean
    public AjaxAuthenticationFilter ajaxAuthenticationFilter() throws Exception {
        AjaxAuthenticationFilter filter = new AjaxAuthenticationFilter(antPathRequestMatcher(), objectMapper);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(securityHandler);
        filter.setAuthenticationFailureHandler(securityHandler);
        return filter;
    }

    @Bean
    public SkipPathRequestMatcher skipPathRequestMatcher() {
        List<String> defaultSkipPath = new ArrayList<String>();
        defaultSkipPath.addAll(Arrays.asList(
        		LOGIN_ENTRY_POINT
        		, LOGOUT_ENTRY_POINT
        		, REFRESH_TOKEN_ENTRY_POINT
        		, VRN_LOGIN_ENTRY_POINT
        		, BACKEND_ENTRY_POINT
                // , H2_CONSOLE_ENTRY_POINT
                , RESOURCE_ENTRY_POINT 
                // , HOME_ENTRY_POINT
                , ERROR_ENTRY_POINT
                ));
        
        if (securityDisabledUrl != null && securityDisabledUrl.length() > 0) {
            String[] disabledPath = securityDisabledUrl.split(",");
            for (String path : disabledPath)
                defaultSkipPath.add(path);
        }
        
        return new SkipPathRequestMatcher(defaultSkipPath);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(skipPathRequestMatcher());
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationFailureHandler(securityHandler);
        return filter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // - (3)
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
}