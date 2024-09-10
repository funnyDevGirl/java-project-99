package hexlet.code.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyFormController {

    @GetMapping("/myform")
    public String creating() {
        return "creatingJS";
    }
}
