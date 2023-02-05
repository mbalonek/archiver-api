package de.kenolab.security.config.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.kenolab.entity.ArchiveUser;
import de.kenolab.security.config.UserAuthenticationProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class UsernamePasswordAuthFilter extends OncePerRequestFilter {

    private final UserAuthenticationProvider provider;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public UsernamePasswordAuthFilter(UserAuthenticationProvider provider) {
        this.provider = provider;
    }

    /**
     * Authorization requests will be validated for method and path
     * @param request will be mapped onto model representing user
     * @param response
     * @param filterChain will be activated and set free after completion
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if("/v1/login".equals(request.getServletPath()) && HttpMethod.POST.matches(request.getMethod())){
            ArchiveUser authRequest = MAPPER.readValue(request.getInputStream(), ArchiveUser.class);

            try{
                SecurityContextHolder.getContext().setAuthentication(
                        provider.validateCredential(authRequest)
                );
            }catch (Exception ex){
                SecurityContextHolder.clearContext();
                throw ex;
            }
        }
        filterChain.doFilter(request, response);
    }

}
