package uk.me.gumbley.simpleaccounts.persistence;

import org.junit.Test;

import uk.me.gumbley.minimiser.persistence.DAOFactory;
import uk.me.gumbley.minimiser.util.InstanceSet;


/**
 * Tests the correct operation of the DAO Layer.
 * 
 * @author matt
 *
 */
public class DAOLayerTests extends SimpleAccountsDatabaseTest {
    /**
     * 
     */
    @Test
    public void createEmptyAccount() {
        final InstanceSet<DAOFactory> database =
            getPersistencePluginHelper().createDatabase(
                DBNAME, DBPASSWORD);
        database.getInstanceOf(SimpleAccountsDAOFactory.class);
    }
}
