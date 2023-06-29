package ru.parma.filesdistr.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import ru.parma.filesdistr.models.Roles;
import ru.parma.filesdistr.service.CustomUserDetailsService;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig  {

    private final CustomUserDetailsService customUserDetailsService;
    private final ApplicationContext applicationContext;

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults()  {
        return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
    }

    @Bean
    public DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler handler = new CustomWebSecurityExpressionHandler();
        return handler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().disable().csrf().disable()
                .formLogin()
                .loginProcessingUrl("/perform_login")
                .defaultSuccessUrl("/hello", true)
                .failureUrl("/login?error=true")
                .and()
                .authorizeRequests()
                .expressionHandler(defaultWebSecurityExpressionHandler()) // !
                .antMatchers("/login","/encrypt").permitAll()
                .antMatchers("/hello", "/jpa").permitAll()
                    .antMatchers("/user").hasAnyRole(Roles.USER.getAuthority(),  Roles.ADMIN.getAuthority())
                    .antMatchers("/admin").hasAuthority( Roles.ADMIN.getAuthority())
                .anyRequest().authenticated()
                .and().logout().permitAll()
                .and()
                .userDetailsService(customUserDetailsService)
                .addFilterAfter(new CustomFilterSec(), BasicAuthenticationFilter.class) // !
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }


}
