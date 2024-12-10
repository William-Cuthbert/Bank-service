package project.model;

import javax.validation.constraints.Email;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

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
