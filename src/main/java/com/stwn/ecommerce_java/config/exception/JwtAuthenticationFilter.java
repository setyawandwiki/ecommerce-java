package com.stwn.ecommerce_java.config.exception;

import com.stwn.ecommerce_java.service.JwtService;
import com.stwn.ecommerce_java.service.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final UserDetailsImpl userDetailsService;
    private final JwtService jwtService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            try{
                filterChain.doFilter(request, response);
            }catch (InsufficientAuthenticationException e){
                handlerExceptionResolver.resolveException(request, response, null, e);
            }
            return;
        }
        try{
            final String jwt = authHeader.substring(7);
            if(jwtService.validateToken(jwt)){
                final String userIdentifier = jwtService.getUsernameFromToken(jwt);
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if(userIdentifier != null && authentication != null){
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userIdentifier);
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities()
                    );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                filterChain.doFilter(request, response);
            }
        }catch (Exception e){
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}
