package com.uade.tpo.marketplace.config;

import com.uade.tpo.marketplace.security.CustomUserDetailsService;
import com.uade.tpo.marketplace.security.JwtAccessDeniedHandler;
import com.uade.tpo.marketplace.security.JwtAuthenticationEntryPoint;
import com.uade.tpo.marketplace.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          JwtAuthenticationFilter jwtAuthenticationFilter,
                          JwtAuthenticationEntryPoint authenticationEntryPoint,
                          JwtAccessDeniedHandler accessDeniedHandler) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/webjars/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/camisetas/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/catalogo/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/camisetas/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/camisetas/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/camisetas/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/camisetas/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/catalogo/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/catalogo/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/catalogo/**").hasRole("ADMIN")
                // Usuarios - self endpoints for any authenticated user
                .antMatchers(HttpMethod.GET, "/api/usuarios/me").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/usuarios/me").authenticated()
                .antMatchers(HttpMethod.PATCH, "/api/usuarios/me/password").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/usuarios/me").authenticated()
                // Usuarios - admin CRUD (same base path)
                .antMatchers(HttpMethod.GET, "/api/usuarios").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/usuarios/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/usuarios").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/usuarios/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/usuarios/*").hasRole("ADMIN")
                // Pedidos - estado update is admin-only
                .antMatchers(HttpMethod.PATCH, "/api/pedidos/*/estado").hasRole("ADMIN")
                // Compras: Restricciones para que los ADMINs no puedan usar el carrito ni crear pedidos
                .antMatchers("/api/carrito/**").hasRole("USER")
                .antMatchers("/api/favoritos/**").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/api/pedidos").hasRole("USER")
                .antMatchers(HttpMethod.PATCH, "/api/pedidos/*/cancelar").hasRole("USER")
                .anyRequest().authenticated();

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:*", "http://127.0.0.1:*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
