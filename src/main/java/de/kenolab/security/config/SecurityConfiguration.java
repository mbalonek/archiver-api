package de.kenolab.security.config;

import de.kenolab.security.config.filters.JWTAuthFilter;
import de.kenolab.security.config.filters.UsernamePasswordAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final UnauthorisedEntryPoint unauthorisedEntryPoint;
    private final UserAuthenticationProvider userAuthenticationProvider;

    public SecurityConfiguration(UnauthorisedEntryPoint unauthorisedEntryPoint, UserAuthenticationProvider userAuthenticationProvider) {
        this.unauthorisedEntryPoint = unauthorisedEntryPoint;
        this.userAuthenticationProvider = userAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity
                .exceptionHandling().authenticationEntryPoint(unauthorisedEntryPoint)
                .and()
                .addFilterBefore(new UsernamePasswordAuthFilter(userAuthenticationProvider), BasicAuthenticationFilter.class)
                .addFilterBefore(new JWTAuthFilter(userAuthenticationProvider), UsernamePasswordAuthFilter.class)
                .cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(
                        requests -> requests.requestMatchers(HttpMethod.POST, "/v1/login", "/v1/register").permitAll()
                                        .anyRequest().authenticated());

        return httpSecurity.build();
    }

}
