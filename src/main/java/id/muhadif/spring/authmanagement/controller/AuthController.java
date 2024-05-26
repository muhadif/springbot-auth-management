package id.muhadif.spring.authmanagement.controller;

import id.muhadif.spring.authmanagement.dto.UserDto;
import id.muhadif.spring.authmanagement.service.FileStorageService;
import id.muhadif.spring.authmanagement.service.UserService;
import jakarta.validation.Valid;
import id.muhadif.spring.authmanagement.dto.UserDto;
import id.muhadif.spring.authmanagement.entity.User;
import id.muhadif.spring.authmanagement.service.FileStorageService;
import id.muhadif.spring.authmanagement.service.UserService;
import id.muhadif.spring.authmanagement.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class AuthController {
    private UserService userService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    // handler method to handle home page request
    @GetMapping("/index")
    public String home() {
        return "index";
    }

    // handler method to handle user registration form request
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // create model object to store form data
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping(path ="/register/save", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model) {
        UserDto existingUser = userService.findUserByEmail(userDto.getEmail());

        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }

        fileStorageService.save(userDto.getAvatar());


        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/register";
        }

        userService.saveUser(userDto, false);
        return "redirect:/register?success";
    }

    // handler method to handle list of users
    @GetMapping("/me")
    public String users(Principal principal, Model model) {
        String email = principal.getName();

        UserDto user = userService.findUserByEmail(email);

        model.addAttribute("user", user);

        return "me";
    }

    // handler method to handle login request
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}