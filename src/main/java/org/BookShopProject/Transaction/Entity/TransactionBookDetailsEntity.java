package org.BookShopProject.Transaction.Entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@Table(name = "TRANSACTION_BOOK")
@AllArgsConstructor
@NoArgsConstructor
public class TransactionBookDetailsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionBookId;
    private Long bookId;
    private Integer quantity;
}
