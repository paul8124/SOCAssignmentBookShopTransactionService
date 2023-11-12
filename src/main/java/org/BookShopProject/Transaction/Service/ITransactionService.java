package org.BookShopProject.Transaction.Service;

import org.BookShopProject.Transaction.Model.UserTransaction;


public interface ITransactionService {

    public String addBookToTransaction(UserTransaction userTransaction);
    UserTransaction getBooksFromTransaction(String userEmailId) throws Exception;
    String deleteBookFromTransaction(String userEmailId) throws Exception;

}
