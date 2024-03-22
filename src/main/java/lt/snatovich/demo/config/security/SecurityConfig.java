package lt.snatovich.demo.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests((auth) -> auth
                        .requestMatchers("admin/**").hasRole("ADMIN")
                        .anyRequest()
                        .authenticated())
                .oauth2ResourceServer((oauth2) -> oauth2
                        .jwt((jwt) -> jwt.jwtAuthenticationConverter(grantedAuthoritiesExtractor())))
                .cors(Customizer.withDefaults())
                .build();
    }

    private Converter<Jwt, AbstractAuthenticationToken> grantedAuthoritiesExtractor() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new GrantedAuthoritiesExtractor());
        return jwtAuthenticationConverter;
    }

    static class GrantedAuthoritiesExtractor implements Converter<Jwt, Collection<GrantedAuthority>> {

        @SuppressWarnings("unchecked")
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            final String REALM_ACCESS_KEY = "realm_access";
            final String ROLES_KEY = "roles";
            final String ROLE_FORMAT_PREFIX = "ROLE_%s";

            return ((Map<String, Collection<?>>) jwt.getClaims()
                    .getOrDefault(REALM_ACCESS_KEY, Collections.emptyMap()))
                    .getOrDefault(ROLES_KEY, Collections.emptyList())
                    .stream()
                    .map(Object::toString)
                    .map(String::toUpperCase)
                    .map(ROLE_FORMAT_PREFIX::formatted)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
    }
}
