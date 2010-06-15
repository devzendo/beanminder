/**
 * Copyright (C) 2008-2010 Matt Gumbley, DevZendo.org <http://devzendo.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.devzendo.beanminder.persistence.dao;

import java.util.List;

import org.devzendo.beanminder.persistence.domain.Account;
import org.devzendo.beanminder.persistence.domain.Transaction;
import org.devzendo.minimiser.util.Pair;


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
