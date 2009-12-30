package uk.me.gumbley.simpleaccounts.persistence;

import java.sql.Date;

import org.junit.After;
import org.junit.Before;

import uk.me.gumbley.commoncode.datetime.SQLDateUtils;
import uk.me.gumbley.minimiser.persistence.DAOFactory;
import uk.me.gumbley.minimiser.persistence.PersistencePluginHelper;
import uk.me.gumbley.minimiser.pluginmanager.PluginException;
import uk.me.gumbley.minimiser.pluginmanager.PluginHelper;
import uk.me.gumbley.minimiser.pluginmanager.PluginHelperFactory;
import uk.me.gumbley.minimiser.util.InstanceSet;
import uk.me.gumbley.simpleaccounts.persistence.domain.Account;

/**
 * Base class for simple database tests.
 * 
 * @author matt
 *
 */
public abstract class SimpleAccountsDatabaseTest {
    /**
     * The test database name
     */
    protected static final String DBNAME = "sadatabase";
    /**
     * The test database password
     */
    protected static final String DBPASSWORD = "";
    
    private PersistencePluginHelper mPersistencePluginHelper;

    /**
     * @return the persistencePluginHelper
     */
    public final PersistencePluginHelper getPersistencePluginHelper() {
        return mPersistencePluginHelper;
    }

    /**
     * 
     */
    public SimpleAccountsDatabaseTest() {
        super();
    }

    /**
     * @throws PluginException on failure
     */
    @Before
    public final void getPrerequisites() throws PluginException {
        final PluginHelper pluginHelper =
            PluginHelperFactory.createMiniMiserPluginHelper();
        mPersistencePluginHelper =
            new PersistencePluginHelper(false, pluginHelper);
        mPersistencePluginHelper.validateTestDatabaseDirectory();
        pluginHelper.loadStandardPlugins();
    }

    /**
     * 
     */
    @After
    public final void tidyDatabases() {
        mPersistencePluginHelper.tidyTestDatabasesDirectory();
    }
    
    
    /**
     * Create the test database, returning the DAO Factory
     * @return the SimpleAccountsDAOFactory.
     * 
     */
    protected final SimpleAccountsDAOFactory createTestDatabase() {
        final InstanceSet<DAOFactory> database =
            getPersistencePluginHelper().createDatabase(
                DBNAME, DBPASSWORD);
        final SimpleAccountsDAOFactory simpleAccountsDaoFactory =
            database.getInstanceOf(SimpleAccountsDAOFactory.class);
        return simpleAccountsDaoFactory;
    }
    
    /**
     * Create a test account, but don't save it.
     * @return the test account
     */
    protected final Account createTestAccount() {
        final Account newAccount =
            new Account("Test account", "123456",
                "Imaginary Bank of London", 5600);
        return newAccount;
    }

    /**
     * Create a second test account, but don't save it.
     * @return the second test account.
     */
    protected final Account createSecondTestAccount() {
        final Account newAccount =
            new Account("Aardvark Test account", "867456",
                "Imaginary Bank of London", 50);
        return newAccount;
    }

    /**
     * Save a test account.
     * @param simpleAccountsDaoFactory the DAO Factory
     * @param account the account to save
     * @return the saved account
     */
    protected final Account saveTestAccount(
            final SimpleAccountsDAOFactory simpleAccountsDaoFactory, final Account account) {
        final Account savedAccount =
            simpleAccountsDaoFactory.getAccountsDao().saveAccount(account);
        return savedAccount;
    }

    /**
     * @return today, normalised as an SQL date
     */
    protected final Date todayNormalised() {
        return SQLDateUtils.normalise(new Date(System.currentTimeMillis()));
    }
}