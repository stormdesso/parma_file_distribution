package ru.parma.filesdistr.dto;


import lombok.*;
import ru.parma.filesdistr.enums.Roles;

import java.util.List;

@Data
@NoArgsConstructor
public class LoginDto {
    private Long userId;
    private List<Roles> roles;
}
