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

package org.devzendo.simpleaccounts.persistence.impl;

import org.devzendo.simpleaccounts.persistence.SimpleAccountsDAOFactory;
import org.devzendo.simpleaccounts.persistence.dao.AccountsDao;
import org.devzendo.simpleaccounts.persistence.dao.TransactionsDao;
import org.devzendo.simpleaccounts.persistence.dao.impl.JdbcTemplateAccountsDao;
import org.devzendo.simpleaccounts.persistence.dao.impl.JdbcTemplateTransactionsDao;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;


/**
 * The JdbcTemplate implementation of the SimpleAccountsDAOFactory.
 *
 * @author matt
 *
 */
public final class JdbcTemplateSimpleAccountsDAOFactory implements
        SimpleAccountsDAOFactory {

    private final JdbcTemplateAccountsDao mAccountsDao;
    private final JdbcTemplateTransactionsDao mTransactionsDao;

    /**
     * Construct the DAOFactory.
     *
     * @param jdbcTemplate the Jdbc Template.
     */
    public JdbcTemplateSimpleAccountsDAOFactory(final SimpleJdbcTemplate jdbcTemplate) {
        mAccountsDao = new JdbcTemplateAccountsDao(jdbcTemplate);
        mTransactionsDao = new JdbcTemplateTransactionsDao(jdbcTemplate);
        // cross-wire since transactionsdao needs to update accounts
        mTransactionsDao.setAccountsDao(mAccountsDao);
    }

    /**
     * {@inheritDoc}
     */
    public AccountsDao getAccountsDao() {
        return mAccountsDao;
    }

    /**
     * {@inheritDoc}
     */
    public TransactionsDao getTransactionsDao() {
        return mTransactionsDao;
    }
}
