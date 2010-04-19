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

import org.devzendo.beanminder.persistence.domain.Account;
import org.devzendo.commoncode.datetime.SQLDateUtils;
import org.devzendo.minimiser.persistence.DAOFactory;
import org.devzendo.minimiser.persistence.PersistencePluginHelper;
import org.devzendo.minimiser.pluginmanager.PluginException;
import org.devzendo.minimiser.pluginmanager.PluginHelper;
import org.devzendo.minimiser.pluginmanager.PluginHelperFactory;
import org.devzendo.minimiser.util.InstanceSet;
import org.junit.After;
import org.junit.Before;


/**
 * Base class for simple database tests.
 *
 * @author matt
 *
 */
public abstract class BeanMinderDatabaseTest {
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
    public BeanMinderDatabaseTest() {
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
     * @return the BeanMinderDAOFactory.
     *
     */
    protected final BeanMinderDAOFactory createTestDatabase() {
        final InstanceSet<DAOFactory> database =
            getPersistencePluginHelper().createDatabase(
                DBNAME, DBPASSWORD);
        final BeanMinderDAOFactory beanMinderDaoFactory =
            database.getInstanceOf(BeanMinderDAOFactory.class);
        return beanMinderDaoFactory;
    }

    /**
     * Create the test database, returning the set of DAO Factories
     * @return the BeanMinderDAOFactory.
     *
     */
    protected final InstanceSet<DAOFactory> createTestDatabaseReturningAllDAOFactories() {
        return getPersistencePluginHelper().createDatabase(
                DBNAME, DBPASSWORD);
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
     * @param beanMinderDaoFactory the DAO Factory
     * @param account the account to save
     * @return the saved account
     */
    protected final Account saveTestAccount(
            final BeanMinderDAOFactory beanMinderDaoFactory, final Account account) {
        final Account savedAccount =
            beanMinderDaoFactory.getAccountsDao().saveAccount(account);
        return savedAccount;
    }

    /**
     * @return today, normalised as an SQL date
     */
    protected final Date todayNormalised() {
        return SQLDateUtils.normalise(new Date(System.currentTimeMillis()));
    }
}