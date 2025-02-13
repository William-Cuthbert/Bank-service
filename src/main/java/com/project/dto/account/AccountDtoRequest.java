package com.project.dto.account;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDtoRequest {
    @Pattern(regexp = "^[a-z A-Z]+$")
    @NotBlank
    private String bankName;
    @Pattern(regexp = "^[a-z A-Z]+$")
    @NotBlank
    private String firstName;
    @Pattern(regexp = "^\\+(?:\\d{1,3})?\\d{9,15}$")
    @NotBlank
    private String phoneNumber;
    @Email
    @NotBlank
    private String emailAddress;
}
