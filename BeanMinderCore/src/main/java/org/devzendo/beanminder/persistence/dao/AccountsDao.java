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

package org.devzendo.beanminder.persistence.dao;

import java.util.List;

import org.devzendo.beanminder.persistence.domain.Account;


/**
 * The Data Access Object for Accounts.
 *
 * @author matt
 *
 */
public interface AccountsDao {
    /**
     * Find all the accounts sorted by name.
     * @return all the accounts, sorted on name.
     */
    List<Account> findAllAccounts();

    /**
     * Save (insert or update) an account.
     * @param account the account to insert or update
     * @return the saved account; if inserted, the id will contain
     * the primary key.
     */
    Account saveAccount(Account account);

    /**
     * Delete an account, cascading the deletion of all its
     * transactions.
     * @param account the account to delete
     */
    void deleteAccount(Account account);
}
