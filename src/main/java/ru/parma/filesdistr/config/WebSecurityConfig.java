package ru.parma.filesdistr.config;

import lombok.RequiredArgsConstructor;
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
import ru.parma.filesdistr.enums.Roles;
import ru.parma.filesdistr.service.CustomUserDetailsService;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig{
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults () {
        return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
    }

    @Bean
    public DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler () {
        return new CustomWebSecurityExpressionHandler();
    }

    @Bean
    public SecurityFilterChain filterChain ( HttpSecurity http ) throws Exception {
        http.cors().disable().csrf().disable()
                //авторизация
                .formLogin()
                .loginProcessingUrl("/perform_login")
                .defaultSuccessUrl("/test/hello", true)
                .failureUrl("/login?error=true")
                .and()
                .logout().permitAll()
                //пространства
                .and()
                .authorizeRequests()
                .expressionHandler(defaultWebSecurityExpressionHandler())
                //.antMatchers("/scope/{scope_id:\\d+}").hasAnyRole(Roles.ROOT.getAuthority(), Roles.ADMIN.getAuthority(), Roles.ADMIN_SCOPES.getAuthority())
                .antMatchers("/scope/**").hasAnyRole(Roles.ROOT.getAuthority(), Roles.ADMIN.getAuthority(), Roles.ADMIN_SCOPES.getAuthority())
                //папки
                .and()
                .authorizeRequests()
                .expressionHandler(defaultWebSecurityExpressionHandler())
                //.antMatchers("/folder/{folder_id:\\d+}").hasAnyRole(Roles.ADMIN.getAuthority(), Roles.ADMIN_SCOPES.getAuthority())
                .antMatchers("/folder/**").hasAnyRole(Roles.ROOT.getAuthority(), Roles.ADMIN.getAuthority(), Roles.ADMIN_SCOPES.getAuthority())
                //версии
                .and()
                .authorizeRequests()
                .expressionHandler(defaultWebSecurityExpressionHandler())
                //.antMatchers("/version/{version_id:\\d+}").hasAnyRole(Roles.ADMIN.getAuthority(), Roles.ADMIN_SCOPES.getAuthority())
                .antMatchers("/version/**").hasAnyRole(Roles.ROOT.getAuthority(), Roles.ADMIN.getAuthority(), Roles.ADMIN_SCOPES.getAuthority())
                //пользователи
                .and()
                .authorizeRequests()
                .expressionHandler(defaultWebSecurityExpressionHandler())
                .antMatchers("/users/**").hasAnyRole(Roles.ROOT.getAuthority(), Roles.ADMIN.getAuthority())
                //настройки:внешний вид тэга
                .and()
                .authorizeRequests()
                .expressionHandler(defaultWebSecurityExpressionHandler())
                .antMatchers("/settings/appearance/**").hasAnyRole(Roles.ROOT.getAuthority(), Roles.ADMIN.getAuthority())
                //файлы
                .and()
                .authorizeRequests()
                .expressionHandler(defaultWebSecurityExpressionHandler())
                .antMatchers("/file/download/{file_id:\\d+}").permitAll()
                .antMatchers("/file/**").hasAnyRole(Roles.ROOT.getAuthority(), Roles.ADMIN.getAuthority(), Roles.ADMIN_SCOPES.getAuthority())

                //test api
                .antMatchers("/test/login", "/test/encrypt").permitAll()
                .antMatchers("/test/hello", "/test/jpa").permitAll()
                .antMatchers("/test/user").hasAnyRole(Roles.USER.getAuthority(), Roles.ADMIN.getAuthority())
                .antMatchers("/test/admin").hasAuthority(Roles.ADMIN.getAuthority())


                //GuestPage
                .and()
                    .authorizeRequests()
                    .expressionHandler(defaultWebSecurityExpressionHandler())
                    .antMatchers("/guest_page/**").permitAll()
                //GuestPage

                .and()
                .userDetailsService(customUserDetailsService)
                .addFilterAfter(new CustomFilterSec(), BasicAuthenticationFilter.class)
                .httpBasic(withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder encoder () {
        return new BCryptPasswordEncoder(8); //NoOpPasswordEncoder.getInstance();
    }

}
