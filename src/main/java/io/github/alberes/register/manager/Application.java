package io.github.alberes.register.manager;

import io.github.alberes.register.manager.domains.Address;
import io.github.alberes.register.manager.domains.UserAccount;
import io.github.alberes.register.manager.services.AddressService;
import io.github.alberes.register.manager.services.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.HashSet;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
public class Application implements CommandLineRunner {

	@Autowired
	private UserAccountService service;

	@Autowired
	private AddressService addressService;

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

			userAccount = new UserAccount();
			userAccount.setName("Manager Data");
			userAccount.setEmail("manager@manager.com");
			userAccount.setPassword("manager123456");
			userAccount.setRoles(new HashSet<String>());
			userAccount.getRoles().add("MANAGER");
			this.service.save(userAccount);

			userAccount = new UserAccount();
			userAccount.setName("User Data");
			userAccount.setEmail("user@user.com");
			userAccount.setPassword("user123456");
			userAccount.setRoles(new HashSet<String>());
			userAccount.getRoles().add("USER");
			this.service.save(userAccount);

			for(int i = 1; i <= 120; i++){
				userAccount = new UserAccount();
				userAccount.setName("User " + i + " register");
				userAccount.setEmail("user" + i + "@user" + i + ".com");
				userAccount.setPassword("user123456_" + i);
				userAccount.setRoles(new HashSet<String>());
				userAccount.getRoles().add("USER");
				this.service.save(userAccount);

				for(int j = 1; j <= 120; j++) {
					Address address = new Address();
					address.setUserAccount(userAccount);
					address.setPublicArea("Avenida principal " + userAccount.getId());
					address.setNumber(j);
					address.setAdditionalAddress("Additional address " + userAccount.getId());
					address.setNeighborhood("Neighborhood " + userAccount.getId());
					address.setCity("City");
					address.setState("ST");
					address.setZipCode("00000000");
					this.addressService.save(address);
				}
			}
		}

	}
}
