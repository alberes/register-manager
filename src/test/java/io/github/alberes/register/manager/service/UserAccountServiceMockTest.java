package io.github.alberes.register.manager.service;

import io.github.alberes.register.manager.domains.UserAccount;
import io.github.alberes.register.manager.repositories.UserAccountRepository;
import io.github.alberes.register.manager.services.UserAccountService;
import io.github.alberes.register.manager.services.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserAccountServiceMockTest {

    @Mock
    private UserAccountRepository repository;

    @InjectMocks
    private UserAccountService service;

    private UserAccount userAccount;

    private UUID uuid = UUID.randomUUID();

    private String email = "usermock@usermock.com";

    private String messageObjectNotFoundException;

    private String messageDuplicateRecordException;

    @BeforeEach
    public void init(){
        userAccount = new UserAccount();
        userAccount.setId(this.uuid);
        userAccount.setName("User Mock");
        userAccount.setEmail("usermock@usermock.com");
        userAccount.setPassword("usermock123456");
        userAccount.setRoles(new HashSet<String>());
        userAccount.getRoles().add("USER");

        this.messageObjectNotFoundException = "Object not found! Id: " + uuid.toString() + ", Type: " + UserAccount.class.getName();
        this.messageDuplicateRecordException = "Registration with e-mail " + userAccount.getEmail() + " has already been registered!";
    }

    @Test
    public void test_save(){
        when(this.repository.save(userAccount)).thenReturn(userAccount);

        UserAccount userAccountDB = this.service.save(userAccount);
        Assertions.assertNotNull(userAccountDB);
        Assertions.assertEquals(uuid, userAccountDB.getId());
        Assertions.assertEquals(email, userAccountDB.getEmail());
    }

    @Test
    public void test_findByEmail(){
        when(this.repository.findByEmail(email)).thenReturn(userAccount);

        Assertions.assertEquals(email, userAccount.getEmail());
    }

    @Test
    public void test_findById(){
        Optional<UserAccount> optional = Optional.of(this.userAccount);

        when(this.repository.findById(uuid)).thenReturn(optional);

        UserAccount userAccountDB = this.service.find(uuid);

        Assertions.assertNotNull(userAccountDB);
        Assertions.assertEquals(email, userAccount.getEmail());
    }

    @Test
    public void test_update(){
        UserAccount userAccountDB = new UserAccount();
        userAccountDB.setId(this.uuid);
        userAccountDB.setName("User Mock");
        userAccountDB.setEmail("usermockupdated@usermock.com");
        userAccountDB.setPassword("usermock123456");
        userAccountDB.setRoles(new HashSet<String>());
        userAccountDB.getRoles().add("USER");

        Optional<UserAccount> optional = Optional.of(this.userAccount);
        when(this.repository.findById(this.uuid)).thenReturn(optional);
        when(this.repository.save(userAccount)).thenReturn(userAccountDB);

        this.service.update(userAccountDB);

        Assertions.assertNotEquals(userAccountDB.getEmail(), userAccount.getEmail());
        Assertions.assertEquals("usermockupdated@usermock.com", userAccountDB.getEmail());
    }

    @Test
    public void test_objectNotFoundId(){
        doThrow(new ObjectNotFoundException(this.messageObjectNotFoundException))
                .when(this.repository).findById(this.uuid);

        ObjectNotFoundException objectNotFoundException =
                Assertions.assertThrows(ObjectNotFoundException.class, () -> {
                    UserAccount userAccountNull = this.service.find(this.uuid);
                });

        Assertions.assertEquals(this.messageObjectNotFoundException,
                objectNotFoundException.getMessage());
    }

}
