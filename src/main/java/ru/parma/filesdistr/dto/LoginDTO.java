package ru.parma.filesdistr.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.parma.filesdistr.models.Role;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class LoginDTO {
    Long userId;
    List<Role> roles;

    public LoginDTO(Long userId, List<Role> roles) {
        this.userId = userId;
        this.roles = roles;
    }
}
