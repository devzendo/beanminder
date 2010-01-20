package uk.me.gumbley.simpleaccounts.persistence.dao;

import java.util.List;

import org.devzendo.minimiser.util.Pair;

import uk.me.gumbley.simpleaccounts.persistence.domain.Account;
import uk.me.gumbley.simpleaccounts.persistence.domain.Transaction;

/**
 * The Data Access Object for Transactions.
 *
 * @author matt
 *
 */
public interface TransactionsDao {
    /**
     * Obtain all transactions for an account, ordered by transaction index.
     * @param account the Account whose Transactions are to be
     * found
     * @return the list of Transactions
     */
    List<Transaction> findAllTransactionsForAccount(Account account);

    /**
     * Obtain a range of transactions for an account, ordered by transaction index,
     * starting at a 'from' index to a 'to' index (inclusive).
     * @param account the Account whose Transactions are to be
     * found
     * @param fromIndex the lowest transaction index in the range
     * @param toIndex the highest transaction index in the range
     * @return the list of Transactions
     */
    List<Transaction> findAllTransactionsForAccountByIndexRange(Account account, int fromIndex, int toIndex);

    /**
     * Save a Transaction under a given account, and update the
     * account's balance with the amount of the transaction.
     * @param account the account with which to update with this
     * transaction
     * @param transaction the transaction to add to the account
     * @return the account with updated balance, and the
     * transaction with its primary key added, if it has been
     * inserted.
     */
    Pair<Account, Transaction> saveTransaction(Account account, Transaction transaction);

    /**
     * Delete a Transaction under a given account, and update the
     * account's balance with the amount of the transaction.
     * @param account the account with which to delete with this
     * transaction
     * @param transaction the transaction to remove from the account
     * @return the account with updated balance.
     */
    Account deleteTransaction(Account account, Transaction transaction);

    /**
     * How many Transactions are there in this Account?
     * @param account the Account to count.
     * @return the number of Transactions in the Account
     */
    int getNumberOfTransactions(Account account);
}
