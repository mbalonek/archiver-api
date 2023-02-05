package de.kenolab.security.config.filters;

import de.kenolab.security.config.UserAuthenticationProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class JWTAuthFilter extends OncePerRequestFilter {

    private final UserAuthenticationProvider provider;

    public JWTAuthFilter(UserAuthenticationProvider provider) {
        this.provider = provider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        try{
            if(header != null){
                String [] authItems = header.split(" ");

                if(authItems.length==2 && "Bearer".equals(authItems[0])){
                    SecurityContextHolder.getContext().setAuthentication(
                            provider.validateToken(authItems[1])
                    );
                }
            }
        }catch (Exception ex){
            SecurityContextHolder.clearContext();
            throw ex;
        }
        filterChain.doFilter(request, response);
    }
}
