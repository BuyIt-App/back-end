package com.buyit.customerservice.controller;

import com.buyit.customerservice.config.JwtService;
import com.buyit.customerservice.model.Cart;
import com.buyit.customerservice.model.Customer;
import com.buyit.customerservice.repository.CartRepo;
import com.buyit.customerservice.repository.CustomerRepo;
import com.buyit.customerservice.token.Token;
import com.buyit.customerservice.token.TokenRepository;
import com.buyit.customerservice.token.TokenType;
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
  private final CustomerRepo customerRepo;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final CartRepo cartRepo;

  public AuthenticationResponse register(RegisterRequest request) {
    var customer = Customer.builder()
            .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .emailId(request.getEmailId())
            .phoneNumber(request.getPhoneNumber())
        .password(passwordEncoder.encode(request.getPassword()))
        .role("CUSTOMER")
            .resetPasswordToken(null)
        .build();

    Cart cart = new Cart();
    cartRepo.save(cart);
    customer.setCart(cart);
    var savedUser = customerRepo.save(customer);
    var jwtToken = jwtService.generateToken(customer);
    var refreshToken = jwtService.generateRefreshToken(customer);
    saveUserToken(savedUser, jwtToken);
    cart.setTotalPrice(0);
    cartRepo.save(cart);
    return AuthenticationResponse.builder()
            .role(customer.getRole())
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
    var customer = customerRepo.findByEmailId(request.getEmailId())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(customer);
    var role = customer.getRole();
    var refreshToken = jwtService.generateRefreshToken(customer);
    revokeAllUserTokens(customer);
    saveUserToken(customer, jwtToken);
    return AuthenticationResponse.builder()
            .message("User Login Successfully")
            .role(role)
        .accessToken(jwtToken)
          .refreshToken(refreshToken)
        .build();
  }

  private void saveUserToken(Customer customer, String jwtToken) {
    var token = Token.builder()
        .customer(customer)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(Customer customer) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(customer.getCustomerId());
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
      var user = this.customerRepo.findByEmailId(userEmail)
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
