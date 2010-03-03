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

package org.devzendo.simpleaccounts.persistence;

import org.devzendo.minimiser.persistence.DAOFactory;
import org.devzendo.minimiser.persistence.MiniMiserDAOFactory;
import org.devzendo.minimiser.util.InstanceSet;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;



/**
 * Is the database created correctly?
 * @author matt
 *
 */
public final class SimpleAccountsDatabaseCreatedCorrectly extends SimpleAccountsDatabaseTest {
    /**
     *
     */
    @Test
    public void areTablesCreated() {
        final InstanceSet<DAOFactory> database =
            getPersistencePluginHelper().createDatabase(
                DBNAME, DBPASSWORD);
        final MiniMiserDAOFactory miniMiserDAOFactory =
            database.getInstanceOf(MiniMiserDAOFactory.class);
        try {
            final SimpleJdbcTemplate jdbcTemplate =
                miniMiserDAOFactory.
                    getSQLAccess().getSimpleJdbcTemplate();

            final int accounts = jdbcTemplate.queryForInt(
                "SELECT COUNT(*) FROM Accounts");
            Assert.assertTrue(accounts == 0);

            final int transactions = jdbcTemplate.queryForInt(
                "SELECT COUNT(*) FROM Transactions");
            Assert.assertTrue(transactions == 0);

        } finally {
            miniMiserDAOFactory.close();
        }
    }
}
