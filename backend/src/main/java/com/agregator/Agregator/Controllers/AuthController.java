package com.agregator.Agregator.Controllers;

import com.agregator.Agregator.DTO.VerificationRequest;
import com.agregator.Agregator.Entity.User;
import com.agregator.Agregator.Enums.UserRole;
import com.agregator.Agregator.Services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/send-code")
    public ResponseEntity<String> sendCode(@RequestParam String email) {
        String answer = authService.sendVerificationCode(email);
        if(answer.equals("Код отправлен")){
            return ResponseEntity.ok("Код отправлен");
        }else {
            return ResponseEntity.badRequest().body(answer);
        }
    }


    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestBody VerificationRequest verificationRequest) {
        String email = verificationRequest.getEmail();
        String code = verificationRequest.getCode();

        logger.info("code " + code);
        logger.info("email "+ email);


        String token = authService.verifyCode(email, code);
        if (token!="false") {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный код");
        }
    }

    @PostMapping("/admin/Send-code")
    public ResponseEntity<String> sendCodeAdmin(@RequestParam String email){
        String answer = authService.sendVerificationCodeAdmin(email);
        if(answer.equals("Код отправлен")){
            return ResponseEntity.ok("Код отправлен");
        }else {
            return ResponseEntity.badRequest().body(answer);
        }
    }

    @PostMapping("/admin/verify")
    public ResponseEntity<String> verifyCodeAdmin(@RequestBody VerificationRequest verificationRequest) {
        String email = verificationRequest.getEmail();
        String code = verificationRequest.getCode();

        logger.info("code " + code);
        logger.info("email "+ email);


        String token = authService.verifyCodeAdmin(email, code);
        if (token!="false") {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный код");
        }
    }

    @PostMapping("/Organization/Send-code")
    public ResponseEntity<String> sendCodeOrganization(@RequestParam String email){
        String answer = authService.sendVerificationCodeOrganization(email);
        if(answer.equals("Код отправлен")){
            return ResponseEntity.ok("Код отправлен");
        }else {
            return ResponseEntity.badRequest().body(answer);
        }
    }

    @PostMapping("/Organization/verify")
    public ResponseEntity<String> verifyCodeOrganization(@RequestBody VerificationRequest verificationRequest) {
        String email = verificationRequest.getEmail();
        String code = verificationRequest.getCode();

        logger.info("code " + code);
        logger.info("email "+ email);


        String token = authService.verifyCodeOrganization(email, code);
        if (token!="false") {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный код");
        }
    }
}
