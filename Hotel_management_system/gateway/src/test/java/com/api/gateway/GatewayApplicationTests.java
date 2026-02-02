/*package com.api.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;*/

package com.api.gateway;

import com.api.gateway.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class GatewayApplicationTests {

	/*@Test
	void contextLoads() {
	}*/

	@Autowired
	private JwtUtil jwtUtil;

	@Test
	void testExtractRolesFromExistingToken() {
		// Paste your real token here (e.g., generated during login)
		String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYWhpbDMwIiwicm9sZXMiOlsiVVNFUiJdLCJpYXQiOjE3NDQ0MzkwNzEsImV4cCI6MTc0NDUyNTQ3MX0.yYmqCjZU8E4xjzYbsrBxGLCOrOwGMOaB9SVRDW5SvAU";

		// Validate the token (optional but good practice)
		assertFalse(token.isEmpty(), "Token should not be empty");
		boolean isValid = jwtUtil.validateToken(token);
		assert isValid : "Invalid token!";

		// Extract roles from token
		List<SimpleGrantedAuthority> authorities = jwtUtil.getAuthoritiesFromToken(token);

		// You can assert roles or just check size
		assert !authorities.isEmpty() : "No roles found in token!";
		authorities.forEach(role -> System.out.println("Role: " + role.getAuthority()));
	}
}
