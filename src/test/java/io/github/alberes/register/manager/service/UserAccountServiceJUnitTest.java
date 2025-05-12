package io.github.alberes.register.manager.service;

import io.github.alberes.register.manager.domains.UserAccount;
import io.github.alberes.register.manager.services.UserAccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.UUID;

@SpringBootTest
public class UserAccountServiceJUnitTest {

    @Autowired
    private UserAccountService service;

    private UserAccount userAccount;

    private UUID uuid = UUID.randomUUID();

    private String email = "userjunit@userjunit.com";

    private String messageObjectNotFoundException;

    private String messageDuplicateRecordException;

    @BeforeEach
    public void init(){
        userAccount = new UserAccount();
        userAccount.setName("User Mock");
        userAccount.setEmail("usermock" + System.currentTimeMillis() + "@usermock.com");
        userAccount.setPassword("usermock123456");
        userAccount.setRoles(new HashSet<String>());
        userAccount.getRoles().add("USER");

        this.messageObjectNotFoundException = "Object not found! Id: " + uuid.toString() + ", Type: " + UserAccount.class.getName();
        this.messageDuplicateRecordException = "Registration with e-mail " + userAccount.getEmail() + " has already been registered!";
    }

    @Test
    public void test_save(){
        UserAccount userAccountDB = this.service.save(this.userAccount);

        Assertions.assertNotNull(userAccountDB.getId());
        Assertions.assertNotNull(userAccountDB);
        Assertions.assertEquals(userAccountDB.getCreatedDate().toLocalDate(), LocalDate.now());
    }

    @Test
    public void test_findById(){
        UserAccount userAccountDB = this.service.find(UUID.fromString("7ff5c600-19fa-4c46-8802-c9080bbb2efb"));

        Assertions.assertNotNull(userAccountDB.getId());
        Assertions.assertNotNull(userAccountDB);
    }

    @Test
    public void test_update(){
        UserAccount userAccountDB = this.service.find(UUID.fromString("32de96ca-7879-4bc3-b70a-4d8558db7b58"));

        userAccountDB.setName("User two Two " + System.currentTimeMillis());

        this.service.update(userAccountDB);

        UserAccount userAccountDBUpdated = this.service.find(UUID.fromString("32de96ca-7879-4bc3-b70a-4d8558db7b58"));

        Assertions.assertEquals(userAccountDB.getName(), userAccountDBUpdated.getName());
        Assertions.assertEquals(userAccountDBUpdated.getLastModifiedDate().toLocalDate(), LocalDate.now());
    }
}
