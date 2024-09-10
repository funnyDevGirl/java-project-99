package hexlet.code.controller;

import hexlet.code.dto.users.UserCreateDTO;
import hexlet.code.dto.users.UserDTO;
import hexlet.code.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;


@Controller
@AllArgsConstructor
public class MyFormController {

    private final UserService userService;

    @GetMapping("/myform")
    public String creating() {
//        //return "creatingJS"; // использование шаблона с JS
        return "registration"; // использование шаблона с thymeleaf
    }

    @PostMapping("/myform")
    public String processForm(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model) {

        // сохранение в БД:
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setFirstName(firstName);
        userCreateDTO.setLastName(lastName);
        userCreateDTO.setEmail(email);
        userCreateDTO.setPassword(password);

        UserDTO createdUser = userService.create(userCreateDTO);

        String fullName = createdUser.getFirstName() + " " + createdUser.getLastName();
        model.addAttribute("fullName", fullName);

        return "success";
    }
}
