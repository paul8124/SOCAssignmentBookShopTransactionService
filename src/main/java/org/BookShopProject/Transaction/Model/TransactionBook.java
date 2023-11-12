package org.BookShopProject.Transaction.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.print.Book;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionBook {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer transactionBookId;
    @Valid
    @JsonProperty("book")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Books book;
    @PositiveOrZero(message = "{book.invalid.quantity}")
    @JsonProperty("quantity")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer quantity;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;
}
