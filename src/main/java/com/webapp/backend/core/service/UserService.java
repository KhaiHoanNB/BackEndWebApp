package com.webapp.backend.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webapp.backend.core.dto.RoleDto;
import com.webapp.backend.core.dto.UserDto;
import com.webapp.backend.core.entities.Role;
import com.webapp.backend.core.entities.User;
import com.webapp.backend.core.repositories.TokenRepository;
import com.webapp.backend.core.repositories.UserRepository;
import com.webapp.backend.security.jwt.JwtService;
import com.webapp.backend.security.jwt.Token;
import com.webapp.backend.security.jwt.TokenType;
import com.webapp.backend.security.util.AuthenticationResponse;
import com.webapp.backend.service.GenericService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService, GenericService<User, Long> {

    private final UserRepository repository;
    private final RoleService roleService;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthenticationResponse register(UserDto request) {
        User user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        List<Role> list = new ArrayList<>();
        List<RoleDto> roles = request.getRoles();
        for (RoleDto roleDto : roles) {
            if (roleDto == null) continue;

            Role role = roleService.getByName(roleDto.getName());
            if (role == null) {
                role = Role.builder()
                        .name(roleDto.getName())
                        .description(roleDto.getDescription())
                        .build();
                role = roleService.save(role);
            }

            list.add(role);
        }

        user.setRoles(list);

        User savedUser = save(user);

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        saveUserToken(savedUser, jwtToken);

        return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
    }

    public AuthenticationResponse authenticate(UserDto request) {

        User user = (User) loadUserByUsername(request.getUsername());

        String jwtToken = jwtService.generateToken(user);

        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserTokens(user);

        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        final String refreshToken = authHeader.substring(7);
        final String username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            User user = this.repository.findByUserName(username);
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUserName(username);
    }

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public User getByID(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
