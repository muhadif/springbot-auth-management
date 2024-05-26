package id.muhadif.spring.authmanagement.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import id.muhadif.spring.authmanagement.entity.User;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
    private Long id;

    private String name;

    private List<User> users;

    @Override
    public String toString() {
        return name;
    }
}