package project.springsecurity.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.springsecurity.dto.UserDTO;
import project.springsecurity.entity.User;
import project.springsecurity.repository.RoleRepository;
import project.springsecurity.repository.UserRepository;

import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@EnableMethodSecurity
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/users")
    @Transactional
    public ResponseEntity<Void> create(@RequestBody UserDTO userDTO) {
        var basicRole = roleRepository.findByName("BASIC");
        var newUser = new User(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()));
        newUser.setRoles(Set.of(basicRole));

        userRepository.save(newUser);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    @Transactional
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<User>> find() {
        var users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
}
