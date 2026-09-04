package e_commerce.khilat.admin;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import e_commerce.khilat.dtomodels.LoginRequest;
import e_commerce.khilat.dtomodels.SignupRequest;
import e_commerce.khilat.dtomodels.UserResponse;
import e_commerce.khilat.entity.User;
import e_commerce.khilat.service.UserService;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@RequestBody SignupRequest request) {
        User savedUser = userService.register(request);
        return ResponseEntity.ok(UserResponse.fromEntity(savedUser));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        User user = userService.login(request.getEmail(), request.getPassword());
        String token = jwtUtil.generateToken(user.getEmail());

        return ResponseEntity.ok(
            Map.of(
                "token", token,
                "message", "Login successful"
            )
        );
    }
}