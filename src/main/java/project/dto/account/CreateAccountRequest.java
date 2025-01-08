package project.dto.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateAccountRequest {
    @Pattern(regexp = "^[a-z A-Z]+$")
    @NotBlank
    private final String bankName;
    @Pattern(regexp = "^[a-z A-Z]+$")
    @NotBlank
    private final String firstName;
    @Pattern(regexp = "^\\+(?:\\d{1,3})?\\d{9,15}$")
    @NotBlank
    private final String phoneNumber;
    @Email
    @NotBlank
    private final String emailAddress;
}
