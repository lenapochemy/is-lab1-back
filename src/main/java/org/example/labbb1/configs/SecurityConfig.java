package org.example.labbb1.configs;


import org.example.labbb1.utils.PasswordHasher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        //@formatter:off
        http
                .securityMatcher("/**")
                .authorizeHttpRequests((authorize) -> authorize

                        .requestMatchers("/").permitAll()
                        .requestMatchers("/user/reg").permitAll()
                        .requestMatchers("/user/logIn").permitAll()
                        .anyRequest().authenticated())
                // 401-UNAUTHORIZED when anonymous user tries to access protected URLs
                .exceptionHandling((handle) -> handle.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) )
                .formLogin((form) ->{
                    form.loginPage("/login");
                    form.loginProcessingUrl("/user/logIn");
                    form.usernameParameter("login");
                    form.passwordParameter("password");
                    form.successHandler((req, res, auth) -> res.setStatus(HttpStatus.OK.value()));
                    form.failureHandler(new SimpleUrlAuthenticationFailureHandler());
                })
                .csrf(AbstractHttpConfigurer::disable);
//                .cors(withDefaults());
        //@formatter:on
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordHasher();
    }

}
