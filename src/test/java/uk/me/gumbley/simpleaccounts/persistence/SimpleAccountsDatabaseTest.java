package uk.me.gumbley.simpleaccounts.persistence;

import org.junit.After;
import org.junit.Before;

import uk.me.gumbley.minimiser.persistence.PersistencePluginHelper;
import uk.me.gumbley.minimiser.pluginmanager.PluginException;
import uk.me.gumbley.minimiser.pluginmanager.PluginHelper;
import uk.me.gumbley.minimiser.pluginmanager.PluginHelperFactory;

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
}