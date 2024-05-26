package id.muhadif.spring.authmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {

    private Long id;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty(message = "Email should not be empty")
    @Email
    private String email;
    private String password;

    private String confirmPassword;

    private String phone;

    private String avatar;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateBirth;

    private GenderDto gender;

    private String identityNumber;

    private List<RoleDto> roles = new ArrayList<>();

    public String rolesToString() {
        return this.roles.stream().map(RoleDto::toString)
                .collect(Collectors.joining(", "));
    }
}

