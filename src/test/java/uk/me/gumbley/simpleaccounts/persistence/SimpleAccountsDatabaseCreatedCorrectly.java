package uk.me.gumbley.simpleaccounts.persistence;

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
