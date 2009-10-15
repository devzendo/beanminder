package uk.me.gumbley.simpleaccounts.persistence;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import uk.me.gumbley.minimiser.persistence.DAOFactory;
import uk.me.gumbley.minimiser.persistence.MiniMiserDAOFactory;
import uk.me.gumbley.minimiser.persistence.PersistencePluginHelper;
import uk.me.gumbley.minimiser.pluginmanager.PluginException;
import uk.me.gumbley.minimiser.pluginmanager.PluginHelper;
import uk.me.gumbley.minimiser.pluginmanager.PluginHelperFactory;
import uk.me.gumbley.minimiser.util.InstanceSet;


/**
 * Is the database created correctly?
 * @author matt
 *
 */
public final class SimpleAccountsDatabaseCreatedCorrectly {
    private static final String DBNAME = "sadatabase";
    private static final String DBPASSWORD = "";
    private PersistencePluginHelper mPersistencePluginHelper;
    
    @Before
    public void getPrerequisites() throws PluginException {
        final PluginHelper pluginHelper =
            PluginHelperFactory.createMiniMiserPluginHelper();
        mPersistencePluginHelper =
            new PersistencePluginHelper(false, pluginHelper);
        mPersistencePluginHelper.validateTestDatabaseDirectory();
        pluginHelper.loadStandardPlugins();
    }
    
    @After
    public void tidyDatabases() {
        //mPersistencePluginHelper.tidyTestDatabasesDirectory();
    }
    
    @Test
    public void areTablesCreated() {
        final InstanceSet<DAOFactory> database =
            mPersistencePluginHelper.createDatabase(
                DBNAME, DBPASSWORD);
        final MiniMiserDAOFactory miniMiserDAOFactory =
            database.getInstanceOf(MiniMiserDAOFactory.class);
        try {
            final SimpleJdbcTemplate jdbcTemplate =
                miniMiserDAOFactory.
                    getSQLAccess().getSimpleJdbcTemplate();
            
            final int accounts = jdbcTemplate.queryForInt(
                "select count(*) from Accounts");
            Assert.assertTrue(accounts == 0);

            final int transactions = jdbcTemplate.queryForInt(
                "select count(*) from Transactions");
            Assert.assertTrue(transactions == 0);

        } finally {
            miniMiserDAOFactory.close();
        }
    }
}
