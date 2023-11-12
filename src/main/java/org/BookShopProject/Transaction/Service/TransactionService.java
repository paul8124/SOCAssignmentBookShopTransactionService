package org.BookShopProject.Transaction.Service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.BookShopProject.Transaction.Entity.TransactionBookDetailsEntity;
import org.BookShopProject.Transaction.Entity.TransactionUserDetailsEntity;
import org.BookShopProject.Transaction.Model.Books;
import org.BookShopProject.Transaction.Model.TransactionBook;
import org.BookShopProject.Transaction.Model.UserTransaction;
import org.BookShopProject.Transaction.Repository.TransactionRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Transactional
@Slf4j
public class TransactionService implements ITransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private RestTemplate template;
    @Value("${application.book.name}")
    private String bookService;

    public String addBookToTransaction(UserTransaction userTransaction) {

        System.out.println("userTransaction->"+userTransaction);

        Set<TransactionBookDetailsEntity> transactionBookDetails = new HashSet<>();
        for (TransactionBook transactionBookDetail : userTransaction.getTransactionBooks()) {
            TransactionBookDetailsEntity transactionBook = TransactionBookDetailsEntity.builder()
                    .bookId(transactionBookDetail.getBook().getBookId())
                    .quantity(transactionBookDetail.getQuantity())
                    .build();
            transactionBookDetails.add(transactionBook);
        }
        Optional<TransactionUserDetailsEntity> transactionOptional = transactionRepository
                .findByUserEmailId(userTransaction.getUserEmailId());
        if (transactionOptional.isEmpty()) {
            TransactionUserDetailsEntity newTransaction = TransactionUserDetailsEntity.builder()
                    .userEmailId(userTransaction.getUserEmailId())
                    .transactionBook(transactionBookDetails)
                    .build();
            System.out.println("newTransaction-> "+newTransaction);
            transactionRepository.save(newTransaction);
        } else {
            TransactionUserDetailsEntity transaction = transactionOptional.get();
            for (TransactionBookDetailsEntity bookToBeAdded : transactionBookDetails) {
                boolean checkProductAlreadyPresent = false;
                for (TransactionBookDetailsEntity bookTransactionFromCart : transaction.getTransactionBook()) {
                    if (bookTransactionFromCart.equals(bookToBeAdded)) {
                        bookTransactionFromCart.setQuantity(bookToBeAdded.getQuantity()
                                + bookTransactionFromCart.getQuantity());
                        checkProductAlreadyPresent = true;
                    }
                }
                if (!checkProductAlreadyPresent) {
                    transaction.getTransactionBook().add(bookToBeAdded);
                }
            }
        }
        return "Book transaction completed.";
    }

    public UserTransaction getBooksFromTransaction(String customerEmailId) throws Exception {
        try{
            Optional<TransactionUserDetailsEntity> transactionOptional = transactionRepository
                    .findByUserEmailId(customerEmailId);
            Set<TransactionBook> transactionBooksDetail = new HashSet<>();
            TransactionUserDetailsEntity transaction = transactionOptional.orElseThrow(() ->
                    new Exception("No transaction found"));
            if (transaction.getTransactionBook().isEmpty()) {
                throw new Exception("No book added to transaction");
            }
            Set<TransactionBookDetailsEntity> transactionBooks = transaction.getTransactionBook();
            for (TransactionBookDetailsEntity transactionBook : transactionBooks) {

                Books books = Books.builder().bookId(transactionBook.getBookId()).build();
                TransactionBook transactionBook1 = TransactionBook.builder()
                        .quantity(transactionBook.getQuantity())
                        .book(books)
                        .build();
                transactionBooksDetail.add(transactionBook1);
            }
            transactionBooksDetail = getBooks(transactionBooksDetail);
            Optional<TransactionBook> errorState = transactionBooksDetail.stream().findFirst();
            String errState = errorState.get().getBook().getError();
            if (!StringUtils.isEmpty(errState)) {
                throw new Exception(errState);
            } else {
                UserTransaction bookTransaction = UserTransaction.builder().transactionId(transaction.getTransactionId())
                        .userEmailId(transaction.getUserEmailId())
                        .transactionBooks(transactionBooksDetail)
                        .build();
                return bookTransaction;
            }
        }catch(Exception e){
        return UserTransaction.builder().error(e.getMessage()).build();
        }
    }

    public String deleteBookFromTransaction(String userEmailId) throws Exception {
        try{
            Optional<TransactionUserDetailsEntity> transactionOptional = transactionRepository.findByUserEmailId(userEmailId);
            TransactionUserDetailsEntity transaction = transactionOptional.orElseThrow(() -> new Exception(
                    "No transaction found"));
            if (transaction.getTransactionBook().isEmpty()) {
                throw new Exception("No product added to cart");
            }
            transactionRepository.delete(transaction);
            return "Book delete successfully";
        } catch (Exception e){
            return "Book deletion failed. Reason: " +e.getMessage();
        }
    }

    public Set<TransactionBook> getBooks(Set<TransactionBook> transactionBooks){
        try{
            for (TransactionBook transactionBook : transactionBooks) {
                ResponseEntity<Books> response = template.getForEntity("http://"+bookService + "/books/getBook/"
                        + transactionBook.getBook().getBookId(), Books.class);
                Books books = response.getBody();
                transactionBook.setBook(books);
            }
            return  transactionBooks;
        }catch (Exception e){
            log.info(e.getMessage());
            for (TransactionBook transactionBook : transactionBooks) {
                transactionBook.setBook(Books.builder().error(e.getMessage()).build());
            }
            return  transactionBooks;
        }
    }

}
