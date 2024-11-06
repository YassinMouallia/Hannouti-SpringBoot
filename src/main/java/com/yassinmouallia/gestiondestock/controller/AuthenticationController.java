package com.yassinmouallia.gestiondestock.controller;


import com.yassinmouallia.gestiondestock.controller.api.AuthenticationApi;
import com.yassinmouallia.gestiondestock.dto.auth.AuthenticationRequest;
import com.yassinmouallia.gestiondestock.dto.auth.AuthenticationResponse;
import com.yassinmouallia.gestiondestock.model.auth.ExtendedUser;
import com.yassinmouallia.gestiondestock.services.auth.ApplicationUserDetailsService;
import com.yassinmouallia.gestiondestock.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController implements AuthenticationApi {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private ApplicationUserDetailsService userDetailsService;

  @Autowired
  private JwtUtil jwtUtil;

  @Override
  public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getLogin(),
            request.getPassword()
        )
    );
    final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getLogin());

    final String jwt = jwtUtil.generateToken((ExtendedUser) userDetails);

    return ResponseEntity.ok(AuthenticationResponse.builder().accessToken(jwt).build());
  }

}
