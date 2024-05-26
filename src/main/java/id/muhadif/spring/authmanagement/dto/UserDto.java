package id.muhadif.spring.authmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty(message = "Email should not be empty")
    @Email
    private String email;
    @NotEmpty(message = "Password should not be empty")
    private String password;

    private String confirmPassword;

    private String phone;

    private MultipartFile avatar;

    private String avatarURL;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateBirth;

    private GenderDto gender;

    private String identityNumber;

    private List<RoleDto> roles = new ArrayList<>();

    public String rolesToString() {
        return this.roles.stream().map(RoleDto::toString)
                .collect(Collectors.joining(", "));
    }

    public String getStringDateBirth() {
        if (dateBirth != null) {
            return dateBirth.toString();
        }
        return "";
    }
}

