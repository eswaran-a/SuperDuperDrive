package com.superduperdrive.cloudstorage.controller;

import com.superduperdrive.cloudstorage.model.User;
import com.superduperdrive.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignupController {

    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String signupPage() {
        return "signup";
    }


    @PostMapping
    public String signupUser(@ModelAttribute User user, Model model) {
        String error = null;
        if (userService.isUserExists(user.getUsername())) {
            error = "The username already exists !!!";
        } else {
            int addCount = userService.createUser(user);
            if (addCount < 0) {
                error = "There was an error signing you up. Please try again. !!!";
            }
        }
        if(error == null) {
            model.addAttribute("signupSuccess", true);
            return "login";
        } else {
            model.addAttribute("signupError", error);
            return "signup";
        }
    }
}
