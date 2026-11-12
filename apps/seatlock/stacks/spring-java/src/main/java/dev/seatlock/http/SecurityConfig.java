package dev.seatlock.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.seatlock.AppConfig;
import dev.seatlock.domain.DomainException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security as an OAuth2 resource server (Chapter 19). Route rules here are the coarse,
 * role-based checks (Chapter 21); ownership lives in the queries. Deny by default: anyRequest().
 */
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain api(HttpSecurity http, ObjectMapper json) throws Exception {
        return http
                .csrf(c -> c.disable()) // bearer tokens, no cookies
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(a -> a
                        .requestMatchers(HttpMethod.GET, "/health", "/events/*", "/events/*/seats").permitAll()
                        .requestMatchers(HttpMethod.POST, "/webhooks/payments").permitAll()
                        .requestMatchers(HttpMethod.POST, "/events", "/admin/holds/release-expired").hasRole("box-office")
                        .requestMatchers(HttpMethod.POST, "/holds", "/holds/*/checkout").hasRole("customer")
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(o -> o
                        .jwt(j -> j.jwtAuthenticationConverter(rolesFromRealmAccess()))
                        .authenticationEntryPoint((req, res, e) -> Problems.write(json, req, res, DomainException.unauthorized())))
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((req, res, ex) -> Problems.write(json, req, res, DomainException.unauthorized()))
                        .accessDeniedHandler((req, res, ex) -> Problems.write(json, req, res, DomainException.forbidden())))
                .build();
    }

    /** Keys from the JWKS URL; issuer and audience checked on every token. */
    @Bean
    JwtDecoder jwtDecoder(AppConfig config) {
        var decoder = NimbusJwtDecoder.withJwkSetUri(config.jwksUrl()).jwsAlgorithm(SignatureAlgorithm.RS256).build();
        var audience = new JwtClaimValidator<List<String>>(JwtClaimNames.AUD, aud -> aud != null && aud.contains(config.audience()));
        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(JwtValidators.createDefaultWithIssuer(config.issuer()), audience));
        return decoder;
    }

    /** Keycloak nests roles in realm_access.roles; hasRole("customer") wants ROLE_customer. */
    static JwtAuthenticationConverter rolesFromRealmAccess() {
        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter((Jwt jwt) -> {
            Map<String, Object> realm = jwt.getClaimAsMap("realm_access");
            Object roles = realm == null ? null : realm.get("roles");
            if (!(roles instanceof Collection<?> list)) return List.<GrantedAuthority>of();
            return list.stream().map(r -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + r)).toList();
        });
        return converter;
    }
}
