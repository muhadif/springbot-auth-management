package id.muhadif.spring.authmanagement.service;

import id.muhadif.spring.authmanagement.dto.UpdateUserDto;
import id.muhadif.spring.authmanagement.dto.UserDto;
import id.muhadif.spring.authmanagement.dto.UpdateUserDto;
import id.muhadif.spring.authmanagement.dto.UserDto;
import id.muhadif.spring.authmanagement.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto, boolean isAdmin);

    void updateUser(UpdateUserDto userDto);

    UserDto findUserByEmail(String email);

    UserDto findUserById(Long id);

    List<UserDto> findAllUsers();

    void deleteUser(Long id);
}