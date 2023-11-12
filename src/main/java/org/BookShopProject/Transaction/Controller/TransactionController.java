package org.BookShopProject.Transaction.Controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.BookShopProject.Transaction.Model.UserTransaction;
import org.BookShopProject.Transaction.Service.ITransactionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping(value = "/transaction")
public class TransactionController {

    @Autowired
    private ITransactionService transactionService;

    @GetMapping(value = "/userTransaction/{userEmailId}")
    public ResponseEntity<UserTransaction> getBooksFromTransaction(
            @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+",
                    message = "{invalid.email.format}")
            @PathVariable("userEmailId") String userEmailId)
            throws Exception {
        log.info("Received a request to get books details from the transaction of " + userEmailId);

        UserTransaction transactionBook = transactionService.getBooksFromTransaction(userEmailId);
        if(StringUtils.isEmpty(transactionBook.getError())){
            return new ResponseEntity<>(transactionBook, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(transactionBook, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/transactionDelete/{userEmailId}")
    public ResponseEntity<String> deleteBookFromTransaction(
            @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+",
                    message = "{invalid.email.format}")
            @PathVariable("userEmailId") String userEmailId)
            throws Exception {

      String message = transactionService.deleteBookFromTransaction(userEmailId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping(value = "/addBook")
    public ResponseEntity<String> addProductToCart(@Valid @RequestBody UserTransaction transaction)
            throws Exception {
        String message = transactionService.addBookToTransaction(transaction);
        if(message.equalsIgnoreCase("Book transaction completed.")){
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
