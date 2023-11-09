package com.buyit.productservice.controller;

import com.buyit.productservice.config.JwtService;
import com.buyit.productservice.model.InventoryKeeper;
import com.buyit.productservice.repository.InventoryKeeperRepo;
import com.buyit.productservice.token.Token;
import com.buyit.productservice.token.TokenRepository;
import com.buyit.productservice.token.TokenType;
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
  private final InventoryKeeperRepo inventoryKeeperRepo;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;


  public AuthenticationResponse register(RegisterRequest request) {
    var inventoryKeeper = InventoryKeeper.builder()
            .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .emailId(request.getEmailId())
            .phoneNumber(request.getPhoneNumber())
        .password(passwordEncoder.encode(request.getPassword()))
        .role("INVENTORY KEEPER")
            .resetPasswordToken(null)
        .build();

    var savedUser = inventoryKeeperRepo.save(inventoryKeeper);
    var jwtToken = jwtService.generateToken(inventoryKeeper);
    var refreshToken = jwtService.generateRefreshToken(inventoryKeeper);
    saveUserToken(savedUser, jwtToken);

    return AuthenticationResponse.builder()
            .role(inventoryKeeper.getRole())
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
    var inventoryKeeper = inventoryKeeperRepo.findByEmailId(request.getEmailId())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(inventoryKeeper);
    var role = inventoryKeeper.getRole();
    var refreshToken = jwtService.generateRefreshToken(inventoryKeeper);
    revokeAllUserTokens(inventoryKeeper);
    saveUserToken(inventoryKeeper, jwtToken);
    return AuthenticationResponse.builder()
            .message("User Login Successfully")
            .role(role)
        .accessToken(jwtToken)
          .refreshToken(refreshToken)
        .build();
  }

  private void saveUserToken(InventoryKeeper inventoryKeeper, String jwtToken) {
    var token = Token.builder()
        .inventoryKeeper(inventoryKeeper)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(InventoryKeeper inventoryKeeper) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(inventoryKeeper.getInventoryKeeperId());
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
      var user = this.inventoryKeeperRepo.findByEmailId(userEmail)
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
