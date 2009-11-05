package uk.me.gumbley.simpleaccounts.persistence;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import uk.me.gumbley.minimiser.persistence.DAOFactory;
import uk.me.gumbley.minimiser.persistence.MiniMiserDAOFactory;
import uk.me.gumbley.minimiser.util.InstanceSet;


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
