package org.BookShopProject.Transaction.Repository;

import org.BookShopProject.Transaction.Entity.TransactionUserDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<TransactionUserDetailsEntity, Long> {
    Optional<TransactionUserDetailsEntity>  findByUserEmailId(String email);
}
