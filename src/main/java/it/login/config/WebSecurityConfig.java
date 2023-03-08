package it.login.config;

import it.login.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	private AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setHideUserNotFoundExceptions(false);
		return authenticationProvider;
	}
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeRequests()
				.requestMatchers("/index").authenticated()
				.anyRequest().permitAll()
				.and()
				.formLogin().loginPage("/index")
				.usernameParameter("username")
				.defaultSuccessUrl("/login_success")
				.permitAll()
				.and()
				.logout().logoutSuccessUrl("/").permitAll();

		return http.build();

	}

	
}
