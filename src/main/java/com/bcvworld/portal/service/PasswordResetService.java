package com.bcvworld.portal.service;



import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcvworld.portal.dto.ForgotPasswordRequest;
import com.bcvworld.portal.dto.ResetPasswordRequest;
import com.bcvworld.portal.model.User;
import com.bcvworld.portal.repository.UserRepository;

@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetService(UserRepository userRepository,
                                PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public boolean verifyEmail(ForgotPasswordRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        Optional<User> userOpt = userRepository.findByEmailIgnoreCase(email);
        return userOpt.isPresent();
    }

    @Transactional
    public boolean resetPassword(ResetPasswordRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        Optional<User> userOpt = userRepository.findByEmailIgnoreCase(email);
        if (userOpt.isEmpty()) {
            return false;
        }
        User user = userOpt.get();
        String encoded = passwordEncoder.encode(request.getPassword());
        user.setPassword(encoded);
        userRepository.save(user);
        return true;
    }
}
