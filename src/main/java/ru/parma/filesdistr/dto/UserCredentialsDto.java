package ru.parma.filesdistr.dto;

import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Builder
@Data
public class UserCredentialsDto {
    private Long id;
    @NotNull
    private String name;
    private String password;
}
