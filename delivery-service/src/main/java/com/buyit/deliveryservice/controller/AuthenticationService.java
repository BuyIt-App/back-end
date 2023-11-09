package com.buyit.deliveryservice.controller;


import com.buyit.deliveryservice.config.JwtService;
import com.buyit.deliveryservice.model.DeliveryPerson;
import com.buyit.deliveryservice.repository.DeliveryPersonRepo;
import com.buyit.deliveryservice.token.Token;
import com.buyit.deliveryservice.token.TokenRepository;
import com.buyit.deliveryservice.token.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final DeliveryPersonRepo deliveryPersonRepo;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;


  public AuthenticationResponse register(RegisterRequest request) {
    var deliverPerson = DeliveryPerson.builder()
            .firstName(request.getFirstName())
        .lastName(request.getLastName())
            .vehicleNumber(request.getVehicleNumber())
        .emailId(request.getEmailId())
            .phoneNumber(request.getPhoneNumber())
        .password(passwordEncoder.encode(request.getPassword()))
        .role("DELIVERY_PERSON")
            .resetPasswordToken(null)
        .build();

    var savedUser = deliveryPersonRepo.save(deliverPerson);
    var jwtToken = jwtService.generateToken(deliverPerson);
    var refreshToken = jwtService.generateRefreshToken(deliverPerson);
    saveUserToken(savedUser, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
           .refreshToken(refreshToken)
        .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmailId(),
            request.getPassword()
        )
    );
    var deliveryPerson = deliveryPersonRepo.findByEmailId(request.getEmailId())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(deliveryPerson);
    var refreshToken = jwtService.generateRefreshToken(deliveryPerson);
    revokeAllUserTokens(deliveryPerson);
    saveUserToken(deliveryPerson, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
          .refreshToken(refreshToken)
        .build();
  }

  private void saveUserToken(DeliveryPerson deliveryPerson, String jwtToken) {
    var token =
            Token.builder()
        .deliveryPerson(deliveryPerson)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(DeliveryPerson deliveryPerson) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(deliveryPerson.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public AuthenticationResponse refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return null;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.deliveryPersonRepo.findByEmailId(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
//        var authResponse = AuthenticationResponse.builder()
//                .accessToken(accessToken)
//               .refreshToken(refreshToken)
//                .build();
//        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
      }
    }
    return null;
  }
}
