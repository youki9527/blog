package com.zyz.blogadmin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author zyz
 * @version 1.0
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * 密码加密
	 * */
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		super.configure(web);
	}

	/**
	 * 认证：你是谁
	 * 授权：你可以访问那些资源接口
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		//开启认证授权（那些接口资源通过需要认证并授权才可以访问 那些不需要）
		http.authorizeRequests()
//                .antMatchers("/user/findAll").hasRole("admin") //访问接口需要admin的角色
				.antMatchers("/css/**").permitAll()
				.antMatchers("/img/**").permitAll()
				.antMatchers("/js/**").permitAll()
				.antMatchers("/plugins/**").permitAll()
				//自定义service 来去实现实时的权限认证授权
				.antMatchers("/blogadmin/admin/**").access("@authService.auth(request,authentication)")
				//自定义service 来去实现实时的权限认证
				.antMatchers("/blogadmin/category/**").access("@authService.auth(request,authentication)")
				//自定义service 来去实现实时的权限认证
				.antMatchers("/blogadmin/tag/**").access("@authService.auth(request,authentication)")
				//自定义service 来去实现实时的权限认证
				.antMatchers("/blogadmin/article/**").access("@authService.auth(request,authentication)")
				//自定义service 来去实现实时的权限认证
				.antMatchers("/blogadmin/user/**").access("@authService.auth(request,authentication)")
				//只需要通过认证就可访问
				.antMatchers("/blogadmin/pages/**").authenticated();
		// 登录配置
		http.formLogin()
				//自定义的登录页面
				.loginPage("/blogadmin/login.html")
				//登录处理接口
				.loginProcessingUrl("/blogadmin/login")
				// 登录时的用户名的key 默认为username
				.usernameParameter("username")
				//定义登录时的密码key，默认是password
				.passwordParameter("password")
				//成功跳转页面
				.defaultSuccessUrl("/blogadmin/pages/main.html")
				.failureUrl("/blogadmin/test.html")
				//通过 不拦截，更加前面配的路径决定，这是指和登录表单相关的接口 都通过
				.permitAll();
		//退出登录配置
		http.logout()
				//退出登录接口
				.logoutUrl("/blogadmin/logout")
				.logoutSuccessUrl("/blogadmin/login.html")
				.permitAll() //退出登录的接口放行
				.and()
				.httpBasic()
				.and()
				.csrf().disable() //csrf关闭 如果自定义登录 需要关闭
				.headers().frameOptions().sameOrigin();
	}
}
