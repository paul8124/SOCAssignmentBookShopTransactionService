package org.BookShopProject.Transaction.Entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@Entity
@Builder
@Table(name = "UserTransaction")
@AllArgsConstructor
@NoArgsConstructor
public class TransactionUserDetailsEntity {
    @Id
    @Column(name = "transaction_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer transactionId;

    @Column(name = "user_email_id")
    private String userEmailId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name ="transaction_books")
    private Set<TransactionBookDetailsEntity> transactionBook;
}
