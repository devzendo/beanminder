package org.devzendo.simpleaccounts.plugin.facade.databaseopening;

import org.devzendo.minimiser.persistence.DAOFactory;
import org.devzendo.minimiser.plugin.facade.opendatabase.DatabaseOpeningFacade;
import org.devzendo.minimiser.util.InstancePair;
import org.devzendo.simpleaccounts.persistence.SimpleAccountsDAOFactory;
import org.devzendo.simpleaccounts.persistence.impl.JdbcTemplateSimpleAccountsDAOFactory;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;


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
