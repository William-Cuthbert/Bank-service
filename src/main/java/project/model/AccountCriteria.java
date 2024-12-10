package project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountCriteria {
  private String accountId;
  private String fullName;
  private String sortCode;
  private String accountNumber;
  private String emailAddress;
  private String bankName;
  private String phoneNumber;
}
