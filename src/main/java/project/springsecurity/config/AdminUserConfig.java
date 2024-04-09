package project.springsecurity.config;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import project.springsecurity.entity.Role;
import project.springsecurity.entity.User;
import project.springsecurity.repository.RoleRepository;
import project.springsecurity.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

@Configuration
@AllArgsConstructor
public class AdminUserConfig implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final Logger logger = Logger.getLogger(getClass().getName());

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        List<Role> roles = createRoles();
        roleRepository.saveAll(roles);

        Role roleAdmin = roleRepository.findByName("ADMIN");
        Optional<User> userAdmin = userRepository.findByUsername("admin");

        userAdmin.ifPresentOrElse(
                user -> logger.info("Usuário admin já existe"),
                () -> {
                    var user = new User();
                    user.setUsername("admin");
                    user.setPassword(passwordEncoder.encode("123456"));
                    user.setRoles(Set.of(roleAdmin));
                    userRepository.save(user);
                }
        );
    }

    private List<Role> createRoles() {
        return List.of(new Role("ADMIN"), new Role("BASIC"));
    }
}
