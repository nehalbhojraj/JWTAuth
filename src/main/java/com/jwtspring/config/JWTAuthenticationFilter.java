package com.jwtspring.config;

import com.jwtspring.Helper.JwtUtil;
import com.jwtspring.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //get jwt
        //get Bearer
        //validate

        String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken;

        if(requestTokenHeader!=null && requestTokenHeader.startsWith("Bearer "))
        {
             jwtToken = requestTokenHeader.substring(7);

            try {
                username = this.jwtUtil.extractUsername(jwtToken);
            }catch (Exception e){
                e.printStackTrace();
            }
            UserDetails userDetails = this.customUserDetailService.loadUserByUsername(username);

            //security check
            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                //authenticate Successfully
            }else{
                System.out.println("Token not validated...");
            }

        }
        //everyThing ok then-->
        filterChain.doFilter(request,response);


    }
}
