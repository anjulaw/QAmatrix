package com.qamatrix.config;


import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.qamatrix.utils.WebConstants;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	Environment env;

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	LogoutSuccessHandler logoutSuccessHandler;
	
	@Value("${spring.queries.users-query}")
	private String usersQuery;
	
	@Value("${spring.queries.roles-query}")
	private String rolesQuery;

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.
			jdbcAuthentication()
				.usersByUsernameQuery(usersQuery)
				.authoritiesByUsernameQuery(rolesQuery)
				.dataSource(dataSource)
				.passwordEncoder(bCryptPasswordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		String webBase = env.getProperty( WebConstants.WEB_BASE_URL );
		
		
		http.
			authorizeRequests()
			.antMatchers( "/**/*.js", "/**/*.css","/**/*.png","/**/*.jpg","/**/*.jpeg","/**/*.gif","/**api/**","/**v1/**","/**/profileitems/**","/**v1/profiles/**","/**/profiles","/**/userlogout").permitAll()
			.antMatchers("/login").permitAll()
				.antMatchers("/registration").permitAll()
				.antMatchers("/admin/**").hasAuthority("ADMIN").anyRequest()
				.authenticated().and().csrf().disable().formLogin()
				.loginPage("/login").failureUrl("/login?error=true")
				.usernameParameter("username")
				.passwordParameter("password")
				.defaultSuccessUrl( webBase, true ).failureUrl("/login?error=true")
				.permitAll()
				.and()
				.logout().logoutRequestMatcher( new AntPathRequestMatcher( "/logout" ))
				/*.logoutSuccessUrl( webBase )*/
				.logoutSuccessHandler( logoutSuccessHandler )			
				.and().logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/").and().exceptionHandling()
				.accessDeniedPage("/access-denied");
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web
	       .ignoring()
	       .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
	}
	
	@Bean
	public LogoutSuccessHandler logoutSuccessHandler()
	{
		LogoutSuccessHandler logoutSuccessHandler = new LogoutSuccessHandler();
		logoutSuccessHandler.setDefaultTargetUrl( "/userlogout" );
		logoutSuccessHandler.setRedirectStrategy( new HttpsRedirectStratergy() );
		return logoutSuccessHandler;

	}

}