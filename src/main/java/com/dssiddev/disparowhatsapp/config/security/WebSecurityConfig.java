package com.dssiddev.disparowhatsapp.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String [] AUTH_WHITELIST = {
			"/auth/**"
	};
	// private static final String [] H2_CONSOLE = {
	// 		"/h2-console/**",
	// 		"/h2-console/login.do?jsessionid="
	// };
	private static final String [] PESQUISA_WHITELIST = {
			"/pesquisas",
			"/pesquisas/**",
			"/pesquisas/pesquisas",
			"/pesquisas/{chatId}/{pesquisaId}"
	};

	private static final String [] CONTA_WHITELIST = {
			"/contas",
			"/contas/**",
			"/contas/do/logado",
			"/contas/{contaId}",
			"/contas/novo",
			"/contas/{contaId}/ativar",

	};

	private static final String [] CONTATOS_WHITELIST = {
		"/contatos",
		"/contatos/upload"
};


	@Autowired
	private AuthenticationEntryPointImpl authenticationEntryPoint;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Bean
	public AuthenticationJWTFilter authenticationJwtFilter() {
		return new AuthenticationJWTFilter();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint)
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				.antMatchers(HttpMethod.POST, AUTH_WHITELIST).permitAll()
				.antMatchers(HttpMethod.PUT, AUTH_WHITELIST).permitAll()
				.antMatchers(HttpMethod.GET, AUTH_WHITELIST).permitAll()
				.antMatchers(HttpMethod.DELETE, AUTH_WHITELIST).permitAll()
				.antMatchers(HttpMethod.POST, CONTA_WHITELIST).permitAll()
				.antMatchers(HttpMethod.PUT, CONTA_WHITELIST).permitAll()
				.antMatchers(HttpMethod.GET, CONTA_WHITELIST).permitAll()
				.antMatchers(HttpMethod.DELETE, CONTA_WHITELIST).permitAll()
				// .antMatchers(H2_CONSOLE).permitAll()
				.antMatchers(HttpMethod.POST, PESQUISA_WHITELIST).permitAll()
				.antMatchers(HttpMethod.PUT, PESQUISA_WHITELIST).permitAll()
				.antMatchers(HttpMethod.POST, CONTATOS_WHITELIST).authenticated()
				.anyRequest().authenticated()
				.and()
				.cors()
				.and()
				.csrf().disable()
				.headers().frameOptions().disable();
		http.addFilterBefore(authenticationJwtFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	@Bean
	public RoleHierarchy roleHierarchy() {
		RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
		roleHierarchy.setHierarchy(ConstantesValues.HIERARCHY);
		return roleHierarchy;
	}
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("https://feature-dev--e-post.netlify.app"));
		configuration.setAllowedMethods(List.of("POST", "GET", "DELETE", "PUT", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowCredentials(true);
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
