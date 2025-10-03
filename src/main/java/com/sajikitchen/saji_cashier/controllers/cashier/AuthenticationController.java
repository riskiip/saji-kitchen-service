package com.sajikitchen.saji_cashier.controllers.cashier;

import com.sajikitchen.saji_cashier.dto.cashier.LoginRequest;
import com.sajikitchen.saji_cashier.dto.cashier.LoginResponse;
import com.sajikitchen.saji_cashier.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        // 1. Lakukan autentikasi menggunakan username & password
        // Jika kredensial salah, Spring Security akan otomatis melempar exception
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // 2. Jika autentikasi berhasil, ambil detail user
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        // 3. Buat token JWT
        final String token = jwtService.generateToken(userDetails);

        // 4. Kembalikan token ke frontend
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
