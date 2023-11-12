package org.BookShopProject.Transaction.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTransaction {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer transactionId;
    @NotNull(message = "{email.absent}")
    @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+" , message = "{invalid.email.format}")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userEmailId;
    @Valid
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<TransactionBook> transactionBooks;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;
}
