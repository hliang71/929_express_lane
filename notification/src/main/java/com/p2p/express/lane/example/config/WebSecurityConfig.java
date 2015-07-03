/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.p2p.express.lane.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.writers.frameoptions.AllowFromStrategy;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * Customizes Spring Security configuration.
 *
 * @author Rob Winch
 */
@EnableWebSecurity
@Configuration

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	//@Autowired
	//private UserRepository userRepo;
	@Bean
	public AllowFromStrategy expressLaneAllowFromStrategy() {
		return new ExpressLaneAllowFromStrategy();
	}

    @Bean
	public CORSHeaderWriter cORSHeaderWriter() {
		/**List<Header> headers = new ArrayList<Header>();
		Header header = new Header("Access-Control-Allow-Methods",  new String[]{"POST", "GET", "OPTIONS"});
		Header header2 = new Header("Access-Control-Allow-Origin",  new String[]{"*"});
		headers.add(header);
		headers.add(header2);
		return new CORSHeaderWriter(headers);*/
		return new CORSHeaderWriter();
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()  // Refactor login form

			// See https://jira.springsource.org/browse/SPR-11496
			.headers().addHeaderWriter(
				new XFrameOptionsHeaderWriter(expressLaneAllowFromStrategy()
						)).addHeaderWriter(cORSHeaderWriter())
				.and()

			.formLogin()
				.defaultSuccessUrl("/index.html")
				.loginPage("/login.html")
				.failureUrl("/login.html?error")
				.permitAll()
				.and()
			.logout()
				.logoutSuccessUrl("/login.html?logout")
				.logoutUrl("/logout.html")
				.permitAll()
				.and()
			.authorizeRequests()
				.antMatchers("/**/**").permitAll()
				.anyRequest().authenticated()
				.and();
	}


	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.inMemoryAuthentication()
				.withUser("fabrice").password("fab123").roles("USER").and()
				.withUser("paulson").password("bond").roles("ADMIN", "USER");
	}
	/**protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.userDetailsService(this.userRepo)
				.passwordEncoder( new ShaPasswordEncoder() );
	}  */
}