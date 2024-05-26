package id.muhadif.spring.authmanagement.service;

import id.muhadif.spring.authmanagement.dto.RoleDto;
import id.muhadif.spring.authmanagement.dto.UpdateUserDto;
import id.muhadif.spring.authmanagement.dto.UserDto;
import id.muhadif.spring.authmanagement.dto.RoleDto;
import id.muhadif.spring.authmanagement.dto.UpdateUserDto;
import id.muhadif.spring.authmanagement.dto.UserDto;
import id.muhadif.spring.authmanagement.entity.Role;
import id.muhadif.spring.authmanagement.entity.User;
import id.muhadif.spring.authmanagement.repository.RoleRepository;
import id.muhadif.spring.authmanagement.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDto userDto, boolean isAdmin) {
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setGender(userDto.getGender());
        user.setPhone(userDto.getPhone());
        user.setDateBirth(userDto.getDateBirth());
        user.setIdentityNumber(userDto.getIdentityNumber());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setAvatar(userDto.getAvatar().getOriginalFilename());

        if (isAdmin) {
            Role role = roleRepository.findByName("ROLE_ADMIN");
            if (role == null) {
                role = checkRoleExist(isAdmin);
            }
            user.setRoles(List.of(role));
        } else {
            Role role = roleRepository.findByName("ROLE_USER");
            if (role == null) {
                role = checkRoleExist(isAdmin);
            }
            user.setRoles(List.of(role));
        }

        userRepository.save(user);
    }

    @Override
    public void updateUser(UpdateUserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());
        if (StringUtils.hasText(userDto.getFirstName()) || StringUtils.hasText(userDto.getLastName())) {
            user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        }
        if (StringUtils.hasText(userDto.getEmail())) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getGender() != null) {
            user.setGender(userDto.getGender());
        }
        if (StringUtils.hasText(userDto.getPhone())) {
            user.setPhone(userDto.getPhone());
        }
        if (userDto.getDateBirth() != null) {
            user.setDateBirth(userDto.getDateBirth());
        }
        if (StringUtils.hasText(userDto.getIdentityNumber())) {
            user.setIdentityNumber(userDto.getIdentityNumber());
        }
        if (StringUtils.hasText(userDto.getPassword())) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        userRepository.save(user);
    }

    private Role checkRoleExist(boolean isAdmin) {
        Role role = new Role();
        if (isAdmin) {
            role.setName("ROLE_ADMIN");
        } else {
            role.setName("ROLE_USER");
        }

        return roleRepository.save(role);
    }

    @Override
    public UserDto findUserByEmail(String email) {
        return convertEntityToDto(userRepository.findByEmail(email));
    }

    @Override
    public UserDto findUserById(Long id)  {
        User user = userRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));

        return convertEntityToDto(user);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map((user) -> convertEntityToDto(user))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private UserDto convertEntityToDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto userDto = new UserDto();
        String[] name = user.getName().split(" ");
        userDto.setId(user.getId());
        userDto.setFirstName(name[0]);
        userDto.setLastName(name[1]);
        userDto.setEmail(user.getEmail());
        userDto.setGender(user.getGender());
        userDto.setPhone(user.getPhone());
        userDto.setDateBirth(user.getDateBirth());
        userDto.setIdentityNumber(user.getIdentityNumber());
        userDto.setAvatarURL(user.getAvatar());

        userDto.setRoles(user.getRoles().stream().map((role -> convertRoleEntityToDto(role))).collect(Collectors.toList()));
        return userDto;
    }

    private RoleDto convertRoleEntityToDto(Role role) {
        RoleDto roleDto = new RoleDto();
        roleDto.setId(role.getId());
        roleDto.setName(role.getName());
        return roleDto;
    }
}
