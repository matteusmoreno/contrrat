package br.com.matteusmoreno.contrrat.login.service;

import br.com.matteusmoreno.contrrat.exception.UserNotFoundException;
import br.com.matteusmoreno.contrrat.login.request.LoginRequest;
import br.com.matteusmoreno.contrrat.login.response.LoginResponse;
import br.com.matteusmoreno.contrrat.security.TokenService;
import br.com.matteusmoreno.contrrat.user.domain.User;
import br.com.matteusmoreno.contrrat.user.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenService tokenService;

    public LoginService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenService = tokenService;
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username()).orElseThrow(() -> new UserNotFoundException("Invalid username or password"));

        if (!user.isLoginCorrect(request, bCryptPasswordEncoder)) {
            throw new BadCredentialsException("Username or password is incorrect");
        }

        String token = tokenService.generateToken(user);

        return new LoginResponse(token);
    }
}
