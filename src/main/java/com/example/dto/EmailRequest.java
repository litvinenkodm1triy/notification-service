package com.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class EmailRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "CREATE|DELETE", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String operation;
}