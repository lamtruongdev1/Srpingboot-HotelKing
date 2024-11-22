package com.hotelking.api;

import com.hotelking.dto.SignUpRequest;
import com.hotelking.entity.User;
import com.hotelking.entity.VerificationToken;
import com.hotelking.repository.UserRepository;
import com.hotelking.repository.VerificationTokenRepository;
import com.hotelking.response.ApiResponse;
import com.hotelking.service.security.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin("*")
public class AuthRestController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LoginService loginService;
    @Autowired
    private VerificationTokenRepository tokenRepository;

    @PostMapping("/api/login")
    public ResponseEntity<Map<String, Object>> handleLogin(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");

        try {
            loginService.loginUser(email, password);
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                user.setPassword("********");
                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("message", "Đăng nhập thành công");
                response.put("user", user);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Map.of("status", "error", "message", "User not found"), HttpStatus.UNAUTHORIZED);
            }
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(Map.of("status", "error", "message", "Email hoặc password không đúng"), HttpStatus.UNAUTHORIZED);
        }
    }



    @PostMapping("/api/logout")
    public ResponseEntity<Map<String, String>> handleLogout() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Đăng xuất thành công");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/api/signup")
    public ResponseEntity<ApiResponse<String>> signup(@RequestBody SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Email đã được sử dụng.", null));
        }

        User user = new User();
        user.setFullname(request.getFullname());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(false);
        user.setRole("USER");
        userRepository.save(user);

        sendVerificationEmail(user);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Đăng ký thành công. Vui lòng kiểm tra email của bạn để xác nhận tài khoản.", null)
        );
    }

    private void sendVerificationEmail(User user) {
        String token = UUID.randomUUID().toString();
        String verificationLink = "http://localhost:8080/verify?token=" + token;

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));

        tokenRepository.save(verificationToken);

        String subject = "Xác Nhận Đăng Ký";
        String message = "<p>Vui lòng nhấp vào liên kết dưới đây để xác nhận tài khoản của bạn:</p>"
                + "<a href=\"" + verificationLink + "\">Xác nhận tài khoản</a>";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }

    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("token") String token, Model model) {
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            return "<html><body>"
                    + "<h1>Lỗi</h1>"
                    + "<p>Token không hợp lệ hoặc đã hết hạn.</p>"
                    + "<button onclick=\"window.location.href='/'\">Quay về trang chủ</button>"
                    + "</body></html>";
        }

        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        tokenRepository.delete(verificationToken);

        return "<html><body>"
                + "<h1>Xác Nhận Thành Công</h1>"
                + "<p>Tài khoản của bạn đã được xác nhận thành công!</p>"
                + "<button onclick=\"window.location.href='/'\">Quay về trang chủ</button>"
                + "</body></html>";
    }


}
