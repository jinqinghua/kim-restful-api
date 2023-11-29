package kim.restful.api.configuration;

import kim.restful.api.common.exception.ExceptionResponse;
import kim.restful.api.common.util.MyJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * @see <a href="https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter">Spring Security without the WebSecurityConfigurerAdapter</a>
 */
@Configuration
@EnableMethodSecurity
@Slf4j
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain buildSecurityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/users").permitAll()
                        .requestMatchers("/api/v1/app-exceptions/*").permitAll()
                        //.requestMatchers(toAntPathRequestMatchers(HttpMethod.OPTIONS, CORS_PATH_PATTERNS)).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling.authenticationEntryPoint((request, response, authenticationException) -> {
                        ExceptionResponse exceptionResponse = new ExceptionResponse(authenticationException.getMessage());//自定义返回类
                        response.setStatus(401);
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        response.getWriter().println(MyJsonUtils.stringify(exceptionResponse));

                    });
                    exceptionHandling.accessDeniedHandler((request, response, accessDeniedException) -> {
                        ExceptionResponse exceptionResponse = new ExceptionResponse("403...");
                        response.setStatus(403);
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        response.getWriter().println(MyJsonUtils.stringify(exceptionResponse));
                    });
                })
                .cors(withDefaults())
        ;
        http.headers(headers -> headers.cacheControl(HeadersConfigurer.CacheControlConfig::disable));
        return http.build();
    }

}