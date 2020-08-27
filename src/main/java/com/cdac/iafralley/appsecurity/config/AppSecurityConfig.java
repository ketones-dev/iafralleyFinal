package com.cdac.iafralley.appsecurity.config;

import org.apache.catalina.filters.ExpiresFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.cdac.iafralley.appsecurity.config.*;
import com.cdac.iafralley.filters.SameSiteFilter;
import com.cdac.iafralley.services.UserService;

@Configuration
@ComponentScan(basePackages="com.cdac")
@EnableWebSecurity
@EnableJpaRepositories(basePackages = {"com.cdac.iafralley.Dao"})
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

	// add a reference to our security data source
    @Autowired
    private UserService userService;
	
    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    
   @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
			.antMatchers("/IAFRalley/RegistrationSuccess","/IAFRalley/showRegistrationForm").permitAll()
			.antMatchers("/resources/**","/upload/**").permitAll()
			
			.antMatchers("/Dashboard/**").hasRole("ADMIN")
			.and()
			.formLogin()
				.loginPage("/Login")
				.loginProcessingUrl("/authenticateTheUser")
				.successHandler(successHandler())
				.permitAll()
			.and()
			.logout().invalidateHttpSession(true).clearAuthentication(true).permitAll()
			.and()
				.exceptionHandling().accessDeniedPage("/access-denied").and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.NEVER).maximumSessions(1)
				.maxSessionsPreventsLogin(true);
             
		http.addFilterAfter(new SameSiteFilter(), BasicAuthenticationFilter.class);

		
	}
	
	
	
	//beans
	//bcrypt bean definition
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	//authenticationProvider bean definition
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userService); //set the custom user details service
		auth.setPasswordEncoder(passwordEncoder()); //set the password encoder - bcrypt
		return auth;
	}
	
	//Customised success Login defination
	/**
	 * contains logic for successful login and redirection
	 * @return CustomAuthenticationSuccessHandler 
	 */
	@Bean
	public CustomAuthenticationSuccessHandler successHandler() {
	    return new CustomAuthenticationSuccessHandler();
	}
	
	 @Bean
	    public static ServletListenerRegistrationBean httpSessionEventPublisher() {
	        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
	    }
	  
}


