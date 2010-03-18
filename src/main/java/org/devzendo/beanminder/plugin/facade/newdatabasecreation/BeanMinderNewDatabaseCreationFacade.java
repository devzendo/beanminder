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

package org.devzendo.beanminder.plugin.facade.newdatabasecreation;

import java.util.Map;

import javax.sql.DataSource;

import org.devzendo.commoncode.patterns.observer.Observer;
import org.devzendo.minimiser.persistence.PersistenceObservableEvent;
import org.devzendo.minimiser.plugin.facade.newdatabase.NewDatabaseCreationFacade;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;


/**
 * Create the SimpleAccounts database.
 *
 * @author matt
 *
 */
public final class BeanMinderNewDatabaseCreationFacade
    implements NewDatabaseCreationFacade {
    private static final String[] CREATION_DDL_STRINGS =
        new String[] {
        "CREATE TABLE Accounts("
                + "id INT IDENTITY,"
                + "name VARCHAR(40) NOT NULL,"
                + "with VARCHAR(40),"
                + "accountCode VARCHAR(40) NOT NULL,"
                + "initialBalance INT,"
                + "currentBalance INT"
                + ")",
        "CREATE TABLE Transactions("
                + "id INT IDENTITY,"
                + "accountId INT NOT NULL,"
                + "FOREIGN KEY (accountId) REFERENCES Accounts (id) ON DELETE CASCADE,"
                + "index INT NOT NULL,"
                + "amount INT NOT NULL,"
                + "isCredit BOOLEAN,"
                + "isReconciled BOOLEAN,"
                + "transactionDate DATE,"
                + "accountBalance INT"
                + ")",
    };

    /**
     * {@inheritDoc}
     */
    public void createDatabase(
            final DataSource dataSource,
            final SimpleJdbcTemplate jdbcTemplate,
            final Observer<PersistenceObservableEvent> observer,
            final Map<String, Object> pluginProperties) {
        for (int i = 0; i < CREATION_DDL_STRINGS.length; i++) {
            observer.eventOccurred(new PersistenceObservableEvent(
                "Creating SimpleAccounts database..."));
            jdbcTemplate.getJdbcOperations().
                execute(CREATION_DDL_STRINGS[i]);
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getNumberOfDatabaseCreationSteps(
            final Map<String, Object> pluginProperties) {
        return CREATION_DDL_STRINGS.length;
    }

    /**
     * {@inheritDoc}
     */
    public void populateDatabase(
            final SimpleJdbcTemplate jdbcTemplate,
            final SingleConnectionDataSource dataSource,
            final Observer<PersistenceObservableEvent> observer,
            final Map<String, Object> pluginProperties) {
        // TODO Nothing here yet
    }
}
