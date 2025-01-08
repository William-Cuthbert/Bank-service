package project.dto.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AccountRequest {
    @Pattern(regexp = "^[0-9]{2}-[0-9]{2}-[0-9]{2}$")
    @NotBlank
    private final String sortCode;
    @Pattern(regexp = "^[0-9]{8}$")
    @NotBlank
    private final String accountNumber;
}
