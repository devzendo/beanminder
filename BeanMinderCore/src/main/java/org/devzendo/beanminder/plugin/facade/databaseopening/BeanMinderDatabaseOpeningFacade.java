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

package org.devzendo.beanminder.plugin.facade.databaseopening;

import javax.sql.DataSource;

import org.devzendo.beanminder.persistence.BeanMinderDAOFactory;
import org.devzendo.beanminder.persistence.impl.JdbcTemplateBeanMinderDAOFactory;
import org.devzendo.minimiser.persistence.DAOFactory;
import org.devzendo.minimiser.plugin.facade.opendatabase.DatabaseOpeningFacade;
import org.devzendo.minimiser.util.InstancePair;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;


/**
 * Open the BeanMinder database.
 *
 * @author matt
 *
 */
public final class BeanMinderDatabaseOpeningFacade implements
        DatabaseOpeningFacade {
    /**
     * {@inheritDoc}
     */
    public InstancePair<DAOFactory> createDAOFactory(
            final DataSource dataSource,
            final SimpleJdbcTemplate jdbcTemplate) {
        final BeanMinderDAOFactory daoFactory =
            new JdbcTemplateBeanMinderDAOFactory(jdbcTemplate);
        return new InstancePair<DAOFactory>(BeanMinderDAOFactory.class, daoFactory);
    }
}
