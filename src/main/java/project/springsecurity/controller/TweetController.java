package project.springsecurity.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import project.springsecurity.dto.TweetDTO;
import project.springsecurity.entity.Tweet;
import project.springsecurity.repository.TweetRepository;
import project.springsecurity.repository.UserRepository;

import javax.management.relation.Role;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@EnableMethodSecurity
@RequestMapping("/tweets")
public class TweetController {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    @Transactional
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody TweetDTO tweetDTO,
                                       JwtAuthenticationToken token) {

        var user = userRepository.findById(UUID.fromString(token.getName()));
        var tweet = new Tweet(user.get(), tweetDTO.getContent());

        tweetRepository.save(tweet);
        return ResponseEntity.ok().build();
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id,
                                       JwtAuthenticationToken token) {
        var tweet = tweetRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var isAdminUser = isAdminUser(token);

        if (isAdminUser || tweet.getUser().getId().equals(UUID.fromString(token.getName()))) {
            tweetRepository.deleteById(id);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok().build();
    }

    @Transactional(readOnly = true)
    @GetMapping("/findAll")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<Tweet>> find() {
        var tweets = tweetRepository.findAll();
        return ResponseEntity.ok(tweets);
    }

    private boolean isAdminUser(JwtAuthenticationToken token) {
        var user = userRepository.findById(UUID.fromString(token.getName()));

        return user.map(value -> value.getRoles()
                .stream().allMatch(role -> role.getName().equalsIgnoreCase("ADMIN"))).orElse(false);
    }
}
