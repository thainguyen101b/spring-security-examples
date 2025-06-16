package com.example.customappuserdaoauthentication;

import com.example.customappuserdaoauthentication.user.AppAuthority;
import com.example.customappuserdaoauthentication.user.AppAuthorityRepository;
import com.example.customappuserdaoauthentication.user.AppUser;
import com.example.customappuserdaoauthentication.user.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class CustomAppuserDaoAuthenticationApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomAppuserDaoAuthenticationApplication.class, args);
	}

	@Bean
	public CommandLineRunner setup(AppUserRepository users,
								   AppAuthorityRepository authorities,
								   PasswordEncoder encoder) {
		return args -> {
			AppAuthority roleUser = new AppAuthority("ROLE_USER");
			AppAuthority roleAdmin = new AppAuthority("ROLE_ADMIN");

			authorities.saveAll(List.of(roleUser, roleAdmin));

			AppUser user = new AppUser("user", encoder.encode("password"), "N", "N",
					LocalDate.of(2004, 3, 11), roleUser);

			AppUser admin = new AppUser("admin", encoder.encode("password"), "N", "N",
					LocalDate.of(2004, 3, 11), roleUser, roleAdmin);

			users.save(user);
			users.save(admin);
		};
	}
}
