package uk.me.gumbley.simpleaccounts.persistence;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import uk.me.gumbley.minimiser.persistence.DAOFactory;
import uk.me.gumbley.minimiser.util.InstanceSet;
import uk.me.gumbley.simpleaccounts.persistence.dao.AccountsDao;
import uk.me.gumbley.simpleaccounts.persistence.dao.TransactionsDao;
import uk.me.gumbley.simpleaccounts.persistence.domain.Account;
import uk.me.gumbley.simpleaccounts.persistence.domain.Transaction;


/**
 * Tests the correct operation of the DAO Layer.
 *
 * @author matt
 *
 */
public final class DAOLayerTests extends SimpleAccountsDatabaseTest {
    /**
     *
     */
    @Test
    public void createEmptyAccount() {
        final InstanceSet<DAOFactory> database =
            getPersistencePluginHelper().createDatabase(
                DBNAME, DBPASSWORD);
        final SimpleAccountsDAOFactory simpleAccountsDaoFactory =
            database.getInstanceOf(SimpleAccountsDAOFactory.class);
        Assert.assertNotNull(simpleAccountsDaoFactory);
        final AccountsDao accountsDao =
            simpleAccountsDaoFactory.getAccountsDao();
        Assert.assertNotNull(accountsDao);

        final Account newAccount =
            new Account("Test account", "123456",
                "Imaginary Bank of London", 5600);
        final Account savedAccount =
            accountsDao.saveAccount(newAccount);

        Assert.assertTrue(savedAccount.getId() > 0);
        Assert.assertEquals(newAccount.getAccountCode(), savedAccount.getAccountCode());
        Assert.assertEquals(newAccount.getName(), savedAccount.getName());
        Assert.assertEquals(newAccount.getWith(), savedAccount.getWith());
        Assert.assertEquals(newAccount.getBalance(), savedAccount.getBalance());

        final TransactionsDao transactionsDao =
            simpleAccountsDaoFactory.getTransactionsDao();

        final List<Transaction> transactions =
            transactionsDao.findAllTransactionsForAccount(savedAccount);
        Assert.assertEquals(0, transactions.size());
    }

    // TODO: findAlltransactionsForAccount with a newly constructed
    // but not saved Account (i.e. its primary key is -1)
    @Test
    public void emptyTransactionsForUnsavedAccount() {
        final InstanceSet<DAOFactory> database =
            getPersistencePluginHelper().createDatabase(
                DBNAME, DBPASSWORD);
        final SimpleAccountsDAOFactory simpleAccountsDaoFactory =
            database.getInstanceOf(SimpleAccountsDAOFactory.class);

    }
}
