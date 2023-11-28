package kim.restful.api.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
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
                        //.requestMatchers(toAntPathRequestMatchers(HttpMethod.OPTIONS, CORS_PATH_PATTERNS)).permitAll()
                        .anyRequest().authenticated()
                )
                .cors(withDefaults())
        ;
        http.headers(headers -> headers.cacheControl(HeadersConfigurer.CacheControlConfig::disable));
        return http.build();
    }

}