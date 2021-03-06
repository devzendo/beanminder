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

package org.devzendo.beanminder.persistence.impl;

import org.devzendo.beanminder.persistence.BeanMinderDAOFactory;
import org.devzendo.beanminder.persistence.dao.AccountsDao;
import org.devzendo.beanminder.persistence.dao.TransactionsDao;
import org.devzendo.beanminder.persistence.dao.impl.JdbcTemplateAccountsDao;
import org.devzendo.beanminder.persistence.dao.impl.JdbcTemplateTransactionsDao;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;


/**
 * The JdbcTemplate implementation of the BeanMinderDAOFactory.
 *
 * @author matt
 *
 */
public final class JdbcTemplateBeanMinderDAOFactory implements
        BeanMinderDAOFactory {

    private final JdbcTemplateAccountsDao mAccountsDao;
    private final JdbcTemplateTransactionsDao mTransactionsDao;

    /**
     * Construct the DAOFactory.
     *
     * @param jdbcTemplate the Jdbc Template.
     */
    public JdbcTemplateBeanMinderDAOFactory(final SimpleJdbcTemplate jdbcTemplate) {
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
