package uk.me.gumbley.simpleaccounts.plugin.facade.newdatabasecreation;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import uk.me.gumbley.commoncode.patterns.observer.Observer;
import uk.me.gumbley.minimiser.persistence.PersistenceObservableEvent;
import uk.me.gumbley.minimiser.plugin.facade.newdatabase.NewDatabaseCreationFacade;

/**
 * Create the SimpleAccounts database.
 *
 * @author matt
 *
 */
public final class SimpleAccountsNewDatabaseCreationFacade
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
