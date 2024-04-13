package hexlet.code.component;

import hexlet.code.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import hexlet.code.dto.users.UserCreateDTO;


@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private UserService userService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        var admin = new UserCreateDTO();

        admin.setEmail("hexlet@example.com");
        admin.setFirstName("Admin");
        admin.setLastName("Admin");
        admin.setPassword("qwerty");

        userService.create(admin);
    }
}
