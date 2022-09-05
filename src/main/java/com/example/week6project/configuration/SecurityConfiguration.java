package com.example.week6project.configuration;


import com.example.week6project.security.AccessDeniedHandlerException;
import com.example.week6project.security.AuthenticationEntryPointException;
import com.example.week6project.security.TokenProvider;
import com.example.week6project.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityConfiguration {

  @Value("${jwt.secret}")
  String SECRET_KEY;
  private final TokenProvider tokenProvider;
  private final UserDetailsServiceImpl userDetailsService;
  private final AuthenticationEntryPointException authenticationEntryPointException;
  private final AccessDeniedHandlerException accessDeniedHandlerException;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Order(SecurityProperties.BASIC_AUTH_ORDER)
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors();

    http.csrf().disable()

            .headers().frameOptions().disable()

            .and()
            .exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPointException)
            .accessDeniedHandler(accessDeniedHandlerException)

        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        .and()
        .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS, "/**/*").permitAll()
        .anyRequest().permitAll()

        .and()
        .apply(new JwtSecurityConfiguration(SECRET_KEY, tokenProvider, userDetailsService));

    /*
    http
            .formLogin()
            .loginPage("") //로그인 페이지
            .defaultSuccessUrl("") // 기본 페이지
            .failureUrl("") // 실패시 이동하는 페이지
            .usernameParameter("id")
            //Html에서 input 박스내 name값(id=username,name=id)
            .passwordParameter("password")
            .loginProcessingUrl("")
            .successHandler(new AuthenticationSuccessHandler() {
              @Override
              public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                System.out.println("Authentication" + authentication.getName());
                response.sendRedirect("/");//기본페이지
              }
            })
            .failureHandler(new AuthenticationFailureHandler() {
              @Override
              public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                System.out.println("Exception" + exception.getMessage());
                response.sendRedirect("/");//실패시 이동페이지
              }
            })
            .permitAll();

     */
    return http.build();
  }
}
