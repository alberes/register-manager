package io.github.alberes.register.manager;

import io.github.alberes.register.manager.domains.UserAccount;
import io.github.alberes.register.manager.services.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.HashSet;

@SpringBootApplication
@EnableJpaAuditing
public class Application implements CommandLineRunner {

	@Autowired
	private UserAccountService service;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		UserAccount userAccount = new UserAccount();
		userAccount.setName("Admin Manager Data");
		userAccount.setEmail("admin@admin.com");
		userAccount.setPassword("admin123456");
		userAccount.setRoles(new HashSet<String>());
		userAccount.getRoles().add("ADMIN");
		if(this.service.notExistsEmail(userAccount.getEmail())){
			this.service.save(userAccount);
		}
	}
}
