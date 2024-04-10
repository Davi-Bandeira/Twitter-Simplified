package project.springsecurity.controller;

import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.springsecurity.dto.LoginRequest;
import project.springsecurity.dto.LoginResponse;
import project.springsecurity.entity.Role;
import project.springsecurity.repository.UserRepository;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class TokenController {

    private final Long expiresIn = 300L;
    private final Instant now = Instant.now();

    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) throws BadRequestException {
        var optionalUser = userRepository.findByUsername(loginRequest.getUsername());

        if (optionalUser.isEmpty() || !optionalUser.get().isLoginCorrect(loginRequest, passwordEncoder)) {
            throw new BadRequestException("User or password is invalid");
        }

        var user = optionalUser.get();
        var scopes = user.getRoles().stream().map(Role::getName)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("mybackend")
                .subject(user.getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scopes)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
    }
}
