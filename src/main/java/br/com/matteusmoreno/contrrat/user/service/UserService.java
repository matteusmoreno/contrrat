package br.com.matteusmoreno.contrrat.user.service;

import br.com.matteusmoreno.contrrat.user.domain.User;
import br.com.matteusmoreno.contrrat.user.constant.Profile;
import br.com.matteusmoreno.contrrat.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    public void createUser(String name, String username, Profile profile, String artistId, String customerId, String rawPassword) {
        String hashedPassword = bCryptPasswordEncoder.encode(rawPassword);

        User user = User.builder()
                .userId(UUID.randomUUID().toString())
                .name(name)
                .username(username)
                .profile(profile)
                .artistId(artistId)
                .customerId(customerId)
                .password(hashedPassword)
                .build();

        userRepository.save(user);
    }

    @Transactional
    public User findOrCreateUserForOAuth(String email, String name) {
        return userRepository.findByUsername(email)
                .orElseGet(() -> {
                    // Usuário logando pela 1ª vez com o Google
                    User newUser = User.builder()
                            .userId(UUID.randomUUID().toString())
                            .name(name)
                            .username(email)
                            // O perfil (ARTIST/CUSTOMER) e o artistId/customerId
                            // serão nulos. O frontend deve direcionar o usuário
                            // para uma página de "Complete seu perfil".
                            .build();

                    return userRepository.save(newUser);
                });
    }
}
