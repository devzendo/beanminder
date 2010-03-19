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

package org.devzendo.beanminder.persistence;

import java.sql.Date;
import java.util.List;

import junit.framework.Assert;

import org.devzendo.beanminder.persistence.BeanMinderDAOFactory;
import org.devzendo.beanminder.persistence.dao.TransactionsDao;
import org.devzendo.beanminder.persistence.domain.Account;
import org.devzendo.beanminder.persistence.domain.Transaction;
import org.devzendo.minimiser.util.Pair;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;


/**
 * Tests for the correct operation of the DAO Layer's TransactionDao.
 *
 * @author matt
 *
 */
public final class TestTransactionsDao extends BeanMinderDatabaseTest {

    /**
     *
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void transactionsCannotBeAddedToAnUnsavedAccount() {
        final BeanMinderDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        simpleAccountsDaoFactory.getTransactionsDao().saveTransaction(createTestAccount(),
            new Transaction(200, true, false, todayNormalised()));
    }

    /**
     *
     */
    @Test
    public void numberOfTransactionsIsCorrectAfterSavingTransactions() {
        final BeanMinderDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        final Account newAccount = createTestAccount();
        final Account savedAccount = saveTestAccount(simpleAccountsDaoFactory,
            newAccount);
        final Date todayNormalised = todayNormalised();
        final TransactionsDao transactionsDao = simpleAccountsDaoFactory.getTransactionsDao();
        Assert.assertEquals(0, transactionsDao.getNumberOfTransactions(savedAccount));
        transactionsDao.saveTransaction(savedAccount,
            new Transaction(200, true, false, todayNormalised));
        Assert.assertEquals(1, transactionsDao.getNumberOfTransactions(savedAccount));
        transactionsDao.saveTransaction(savedAccount,
            new Transaction(400, true, false, todayNormalised));
        Assert.assertEquals(2, transactionsDao.getNumberOfTransactions(savedAccount));
    }

    /**
     *
     */
    @Test
    public void transactionsHaveMonotonicallyIncreasingIndexAndCorrectAccountBalance() {
        final BeanMinderDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        final Account newAccount = createTestAccount(); // balance 5600
        final Account savedAccount = saveTestAccount(simpleAccountsDaoFactory,
            newAccount);
        final Date todayNormalised = todayNormalised();
        // Transaction 1
        final Transaction newTransaction1 = new Transaction(200, true, false,
                todayNormalised); // +200 = 5800
        Assert.assertEquals(-1, newTransaction1.getIndex()); // HMMM internal detail?
        Assert.assertEquals(-1, newTransaction1.getAccountBalance()); // HMMM internal detail?

        final Pair<Account, Transaction> pair1 = simpleAccountsDaoFactory
                .getTransactionsDao().saveTransaction(savedAccount,
                    newTransaction1);
        final Account savedAccount1 = pair1.getFirst();
        final Transaction savedTransaction1 = pair1.getSecond();
        Assert.assertEquals(0, savedTransaction1.getIndex());
        Assert.assertEquals(5800, savedTransaction1.getAccountBalance());
        Assert.assertEquals(5800, savedAccount1.getCurrentBalance());

        // Transaction 2
        final Transaction newTransaction2 = new Transaction(20, true, false,
            todayNormalised); // +20 = 5820
        final Pair<Account, Transaction> pair2 = simpleAccountsDaoFactory
                .getTransactionsDao().saveTransaction(savedAccount,
                    newTransaction2);
        final Account savedAccount2 = pair2.getFirst();
        final Transaction savedTransaction2 = pair2.getSecond();
        Assert.assertEquals(1, savedTransaction2.getIndex());
        Assert.assertEquals(5820, savedTransaction2.getAccountBalance());
        Assert.assertEquals(5820, savedAccount2.getCurrentBalance());

        // Transaction 3
        final Transaction newTransaction3 = new Transaction(10, false, false,
            todayNormalised); // -10 = 5810
        final Pair<Account, Transaction> pair3 = simpleAccountsDaoFactory
                .getTransactionsDao().saveTransaction(savedAccount,
                    newTransaction3);
        final Account savedAccount3 = pair3.getFirst();
        final Transaction savedTransaction3 = pair3.getSecond();
        Assert.assertEquals(2, savedTransaction3.getIndex());
        Assert.assertEquals(5810, savedTransaction3.getAccountBalance());
        Assert.assertEquals(5810, savedAccount3.getCurrentBalance());
    }

    /**
     *
     */
    @Test
    public void transactionsAreListedOrderedByIndex() {
        // if the select is not ordered by index, this insertion seems to yield
        // a list in the order inserted here, but it needs the ORDER BY index
        // ASC for correctness, so I'll test for it anyway
        final BeanMinderDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        final Account newAccount = createTestAccount();
        final Account savedAccount = saveTestAccount(simpleAccountsDaoFactory,
            newAccount);
        final TransactionsDao transactionsDao = simpleAccountsDaoFactory.getTransactionsDao();
        final Date todayNormalised = todayNormalised();
        // Transaction 0
        final Transaction newTransaction0 = new Transaction(200, true, false,
                todayNormalised);
        transactionsDao.saveTransaction(savedAccount, newTransaction0);

        // Transaction 1
        final Transaction newTransaction1 = new Transaction(20, true, false,
            todayNormalised);
        transactionsDao.saveTransaction(savedAccount, newTransaction1);

        // Transaction 2
        final Transaction newTransaction2 = new Transaction(10, false, false,
            todayNormalised);
        transactionsDao.saveTransaction(savedAccount, newTransaction2);

        final List<Transaction> allTransactions = transactionsDao.findAllTransactionsForAccount(savedAccount);
        Assert.assertEquals(3, allTransactions.size());
        Assert.assertEquals(200, allTransactions.get(0).getAmount());
        Assert.assertEquals(0, allTransactions.get(0).getIndex());
        Assert.assertEquals(20, allTransactions.get(1).getAmount());
        Assert.assertEquals(1, allTransactions.get(1).getIndex());
        Assert.assertEquals(10, allTransactions.get(2).getAmount());
        Assert.assertEquals(2, allTransactions.get(2).getIndex());
    }

    /**
     *
     */
    @Test
    public void updateATransactionAndSubsequentTransactionsAndAccountBalanceUpdated() {
        final BeanMinderDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        final Account newAccount = createTestAccount();
        final Account savedAccount = saveTestAccount(simpleAccountsDaoFactory,
            newAccount);
        final TransactionsDao transactionsDao = simpleAccountsDaoFactory.getTransactionsDao();
        final Date todayNormalised = todayNormalised();
        // Transaction 0
        final Transaction newTransaction0 = new Transaction(200, true, false,
                todayNormalised);
        transactionsDao.saveTransaction(savedAccount, newTransaction0);

        // Transaction 1
        final Transaction newTransaction1 = new Transaction(20, true, false,
            todayNormalised);
        final Pair<Account, Transaction> savedTransaction1Pair =
            transactionsDao.saveTransaction(savedAccount, newTransaction1);
        final Transaction savedTransaction1 = savedTransaction1Pair.getSecond();

        // Transaction 2
        final Transaction newTransaction2 = new Transaction(10, false, false,
            todayNormalised);
        transactionsDao.saveTransaction(savedAccount, newTransaction2);

        // Transaction 3
        final Transaction newTransaction3 = new Transaction(500, true, false,
            todayNormalised);
        final Pair<Account, Transaction> saveTransaction3Pair = transactionsDao.saveTransaction(savedAccount, newTransaction3);
        final Account accountBeforeUpdate = saveTransaction3Pair.getFirst();
        Assert.assertEquals(5600 + 200 + 20 - 10 + 500, accountBeforeUpdate.getCurrentBalance());

        // Update Transaction 1
        savedTransaction1.setAmount(30); // +10
        final Pair<Account, Transaction> savedUpdatedTransaction1Pair = transactionsDao.saveTransaction(savedAccount, savedTransaction1);

        // Have the account, the updated transaction, and subsequent transactions been modified?
        final Account reloadedAccount = savedUpdatedTransaction1Pair.getFirst();
        Assert.assertEquals(5600 + 200 + 30 - 10 + 500, reloadedAccount.getCurrentBalance());

        final List<Transaction> allTransactions = transactionsDao.findAllTransactionsForAccount(savedAccount);
        Assert.assertEquals(4, allTransactions.size());

        Assert.assertEquals(200, allTransactions.get(0).getAmount());
        Assert.assertEquals(0, allTransactions.get(0).getIndex());
        Assert.assertEquals(5800, allTransactions.get(0).getAccountBalance());

        Assert.assertEquals(30, allTransactions.get(1).getAmount());
        Assert.assertEquals(1, allTransactions.get(1).getIndex());
        Assert.assertEquals(5830, allTransactions.get(1).getAccountBalance());

        Assert.assertEquals(10, allTransactions.get(2).getAmount());
        Assert.assertEquals(2, allTransactions.get(2).getIndex());
        Assert.assertEquals(5820, allTransactions.get(2).getAccountBalance());

        Assert.assertEquals(500, allTransactions.get(3).getAmount());
        Assert.assertEquals(3, allTransactions.get(3).getIndex());
        Assert.assertEquals(6320, allTransactions.get(3).getAccountBalance());
    }

    /**
     *
     */
    @Test
    public void updateATransactionByAlsoChangingCreditDebitFlag() {
        final BeanMinderDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        final Account newAccount = createTestAccount();
        final Account savedAccount = saveTestAccount(simpleAccountsDaoFactory,
            newAccount);
        final TransactionsDao transactionsDao = simpleAccountsDaoFactory.getTransactionsDao();
        final Date todayNormalised = todayNormalised();
        // Transaction 0
        final Transaction newTransaction0 = new Transaction(200, true, false,
                todayNormalised);
        transactionsDao.saveTransaction(savedAccount, newTransaction0);

        // Transaction 1, the last one in the a/c.
        final Transaction newTransaction1 = new Transaction(20, true, false,
            todayNormalised);
        final Pair<Account, Transaction> savedTransaction1Pair =
            transactionsDao.saveTransaction(savedAccount, newTransaction1);
        final Transaction savedTransaction1 = savedTransaction1Pair.getSecond();
        final Account accountBeforeUpdate = savedTransaction1Pair.getFirst();
        Assert.assertEquals(5600 + 200 + 20, accountBeforeUpdate.getCurrentBalance());

        // Update Transaction 1
        savedTransaction1.setAmount(30); // +10, but...
        savedTransaction1.setCredit(false); // change to a debit, so effect on the a/c is -50
        final Pair<Account, Transaction> savedUpdatedTransaction2Pair = transactionsDao.saveTransaction(savedAccount, savedTransaction1);

        // Have the account, the updated transaction, and subsequent transactions been modified?
        final Account reloadedAccount = savedUpdatedTransaction2Pair.getFirst();
        Assert.assertEquals(5600 + 200 - 30, reloadedAccount.getCurrentBalance());

        final List<Transaction> allTransactions = transactionsDao.findAllTransactionsForAccount(savedAccount);
        Assert.assertEquals(2, allTransactions.size());

        Assert.assertEquals(200, allTransactions.get(0).getAmount());
        Assert.assertEquals(0, allTransactions.get(0).getIndex());
        Assert.assertEquals(5800, allTransactions.get(0).getAccountBalance());

        Assert.assertEquals(30, allTransactions.get(1).getAmount());
        Assert.assertEquals(false, allTransactions.get(1).isCredit());
        Assert.assertEquals(1, allTransactions.get(1).getIndex());
        Assert.assertEquals(5600 + 200 - 30, allTransactions.get(1).getAccountBalance());
    }

    /**
     *
     */
    @Test
    public void deleteATransactionAndSubsequentTransactionsAndAccountBalanceUpdated() {
        final BeanMinderDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        final Account newAccount = createTestAccount();
        final Account savedAccount = saveTestAccount(simpleAccountsDaoFactory,
            newAccount);
        final TransactionsDao transactionsDao = simpleAccountsDaoFactory.getTransactionsDao();
        final Date todayNormalised = todayNormalised();
        // Transaction 0
        final Transaction newTransaction0 = new Transaction(200, true, false,
                todayNormalised);
        final Pair<Account, Transaction> savedTransaction0Pair =
            transactionsDao.saveTransaction(savedAccount, newTransaction0);
        final Transaction savedTransaction0 = savedTransaction0Pair.getSecond();

        // Transaction 1
        final Transaction newTransaction1 = new Transaction(20, true, false,
            todayNormalised);
        transactionsDao.saveTransaction(savedAccount, newTransaction1);

        // Transaction 2
        final Transaction newTransaction2 = new Transaction(10, false, false,
            todayNormalised);
        transactionsDao.saveTransaction(savedAccount, newTransaction2);

        // Transaction 3
        final Transaction newTransaction3 = new Transaction(500, true, false,
            todayNormalised);
        final Pair<Account, Transaction> saveTransaction3Pair = transactionsDao.saveTransaction(savedAccount, newTransaction3);
        final Account accountBeforeDelete = saveTransaction3Pair.getFirst();
        Assert.assertEquals(5600 + 200 + 20 - 10 + 500, accountBeforeDelete.getCurrentBalance());

        // Delete Transaction 0
        final Account updatedAccount = transactionsDao.deleteTransaction(accountBeforeDelete, savedTransaction0);

        // Have the account, the updated transaction, and subsequent transactions been modified? The
        // transactions' indices should have changed also.
        Assert.assertEquals(5600 + 20 - 10 + 500, updatedAccount.getCurrentBalance());

        final List<Transaction> allTransactions = transactionsDao.findAllTransactionsForAccount(savedAccount);
        Assert.assertEquals(3, allTransactions.size());

        Assert.assertEquals(20, allTransactions.get(0).getAmount());
        Assert.assertEquals(0, allTransactions.get(0).getIndex());
        Assert.assertEquals(5620, allTransactions.get(0).getAccountBalance());

        Assert.assertEquals(10, allTransactions.get(1).getAmount());
        Assert.assertEquals(1, allTransactions.get(1).getIndex());
        Assert.assertEquals(5610, allTransactions.get(1).getAccountBalance());

        Assert.assertEquals(500, allTransactions.get(2).getAmount());
        Assert.assertEquals(2, allTransactions.get(2).getIndex());
        Assert.assertEquals(6110, allTransactions.get(2).getAccountBalance());
    }

    /**
     *
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void cannotDeleteATransactionGivenAnUnsavedAccount() {
        final BeanMinderDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        simpleAccountsDaoFactory.getTransactionsDao().deleteTransaction(createTestAccount(),
            new Transaction(200, true, false, todayNormalised()));
    }

    /**
     *
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void cannotDeleteAnUnsavedTransaction() {
        final BeanMinderDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        final Account newAccount = createTestAccount();
        final Account savedAccount = saveTestAccount(simpleAccountsDaoFactory,
            newAccount);
        simpleAccountsDaoFactory.getTransactionsDao().deleteTransaction(savedAccount,
            new Transaction(200, true, false, todayNormalised()));
    }

    /**
     *
     */
    @Test
    public void transactionsCanBeFoundByIndexRange() {
        final BeanMinderDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        final Account newAccount = createTestAccount();
        final Account savedAccount = saveTestAccount(simpleAccountsDaoFactory,
            newAccount);
        final TransactionsDao transactionsDao = simpleAccountsDaoFactory.getTransactionsDao();
        final Date todayNormalised = todayNormalised();
        transactionsDao.saveTransaction(savedAccount, new Transaction(200, true, false,
                todayNormalised));

        transactionsDao.saveTransaction(savedAccount, new Transaction(20, true, false,
            todayNormalised));

        transactionsDao.saveTransaction(savedAccount, new Transaction(10, false, false,
            todayNormalised));

        transactionsDao.saveTransaction(savedAccount, new Transaction(500, true, false,
            todayNormalised));

        final List<Transaction> midRangeIndices = transactionsDao.findAllTransactionsForAccountByIndexRange(savedAccount, 1, 2);
        checkTransactionAmounts(midRangeIndices, 20, 10);

        final List<Transaction> firstIndex = transactionsDao.findAllTransactionsForAccountByIndexRange(savedAccount, 0, 0);
        checkTransactionAmounts(firstIndex, 200);

        final List<Transaction> lastIndex = transactionsDao.findAllTransactionsForAccountByIndexRange(savedAccount, 3, 3);
        checkTransactionAmounts(lastIndex, 500);

        final List<Transaction> allIndices = transactionsDao.findAllTransactionsForAccountByIndexRange(savedAccount, 0, 3);
        checkTransactionAmounts(allIndices, 200, 20, 10, 500);

        final List<Transaction> allIndicesByOutOfRangeIndices = transactionsDao.findAllTransactionsForAccountByIndexRange(savedAccount, -3, 9);
        checkTransactionAmounts(allIndicesByOutOfRangeIndices, 200, 20, 10, 500);

        final List<Transaction> noIndicesByInvertedOrderIndices = transactionsDao.findAllTransactionsForAccountByIndexRange(savedAccount, 3, 0);
        checkTransactionAmounts(noIndicesByInvertedOrderIndices);
    }

    private void checkTransactionAmounts(
         final List<Transaction> transactionList,
         final int ... amounts) {
        Assert.assertEquals(amounts.length, transactionList.size());
        for (int i = 0; i < amounts.length; i++) {
            Assert.assertEquals(
                "Transaction amount at index " + i + " is "
                + transactionList.get(i).getAmount()
                + " but should be " + amounts[i],
                amounts[i], transactionList.get(i).getAmount());
        }
    }

    /**
     *
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void cannotFindTransactionsByIndexRangeForAnUnsavedAccount() {
       final BeanMinderDAOFactory simpleAccountsDaoFactory = createTestDatabase();
       final Account newAccount = createTestAccount();
       simpleAccountsDaoFactory.getTransactionsDao().findAllTransactionsForAccountByIndexRange(newAccount, 0, 0);
    }

    /**
     *
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void cannotFindAllTransactionsForAnUnsavedAccount() {
       final BeanMinderDAOFactory simpleAccountsDaoFactory = createTestDatabase();
       final Account newAccount = createTestAccount();
       simpleAccountsDaoFactory.getTransactionsDao().findAllTransactionsForAccount(newAccount);
   }
}
