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

package org.devzendo.beanminder.persistence;

import org.devzendo.beanminder.persistence.dao.AccountsDao;
import org.devzendo.beanminder.persistence.dao.TransactionsDao;
import org.devzendo.minimiser.persistence.DAOFactory;


/**
 * The DAO Factory for accessing the BeanMinder database.
 * @author matt
 *
 */
public interface BeanMinderDAOFactory extends DAOFactory {
    /**
     * @return the AccountsDao
     */
    AccountsDao getAccountsDao();

    /**
     * @return the TransactionsDao
     */
    TransactionsDao getTransactionsDao();
}
