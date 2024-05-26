package id.muhadif.spring.authmanagement.controller.api;

import id.muhadif.spring.authmanagement.dto.UserDto;
import id.muhadif.spring.authmanagement.dto.UserDto;
import id.muhadif.spring.authmanagement.pkg.BaseResponse;
import id.muhadif.spring.authmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/api/users")
    public ResponseEntity<BaseResponse<List<UserDto>>> getUsers() {
        BaseResponse<List<UserDto>> response = new BaseResponse<>();
        try {
            List<UserDto> users = userService.findAllUsers();
            response.setData(users);
            response.setStatus(HttpStatus.OK);
            response.setMessage("sucess");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("User creation failed: " + e.getMessage());
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
