package com.jwtspring.controller;

import com.jwtspring.Helper.JwtUtil;
import com.jwtspring.Model.JwtRequest;
import com.jwtspring.Model.JwtResponse;
import com.jwtspring.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jwt")
public class JWTController {

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/token")
    public ResponseEntity<?> getToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        System.out.println(jwtRequest);

        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(),jwtRequest.getPassword()));

        }catch (UsernameNotFoundException e){
            e.printStackTrace();
            throw new Exception("Bad Request");
        }
        catch (BadCredentialsException e){

            throw new Exception("Bad Request");
        }

       UserDetails  userDetails = this.customUserDetailService.loadUserByUsername(jwtRequest.getUsername());

        String token = this.jwtUtil.generateToken(userDetails);
        System.out.println("Token: "+token);

        return ResponseEntity.ok(new JwtResponse(token));
    }


}
