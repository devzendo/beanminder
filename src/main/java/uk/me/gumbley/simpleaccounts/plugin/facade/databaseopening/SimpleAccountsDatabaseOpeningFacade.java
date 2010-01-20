package uk.me.gumbley.simpleaccounts.plugin.facade.databaseopening;

import org.devzendo.minimiser.persistence.DAOFactory;
import org.devzendo.minimiser.plugin.facade.opendatabase.DatabaseOpeningFacade;
import org.devzendo.minimiser.util.InstancePair;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import uk.me.gumbley.simpleaccounts.persistence.SimpleAccountsDAOFactory;
import uk.me.gumbley.simpleaccounts.persistence.impl.JdbcTemplateSimpleAccountsDAOFactory;

/**
 * Open the SimpleAccounts database.
 *
 * @author matt
 *
 */
public final class SimpleAccountsDatabaseOpeningFacade implements
        DatabaseOpeningFacade {
    /**
     * {@inheritDoc}
     */
    public InstancePair<DAOFactory> createDAOFactory(
            final SimpleJdbcTemplate jdbcTemplate,
            final SingleConnectionDataSource dataSource) {
        final SimpleAccountsDAOFactory daoFactory =
            new JdbcTemplateSimpleAccountsDAOFactory(jdbcTemplate);
        return new InstancePair<DAOFactory>(SimpleAccountsDAOFactory.class, daoFactory);
    }
}
