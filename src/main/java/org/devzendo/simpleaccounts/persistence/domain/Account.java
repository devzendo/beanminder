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

package org.devzendo.simpleaccounts.persistence.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * An Account POJO.
 *
 * @author matt
 *
 */
public final class Account {
    private final int mId;
    private String mName;
    private String mAccountCode;
    private String mWith;
    private final int mInitialBalance;
    private final int mCurrentBalance;

    /**
     * Create an Account. Note that the balance can only be set on
     * initial creation; it is updated when Transactions are added,
     * modified or removed.
     * <p>
     * This version of the constructor is intended for use by the
     * DAO Layer. Client code should use the other constructor (the
     * one without the primary key) as it does not know the key
     * until the Account is saved. Also, the current balance cannot
     * be altered directly by client code (instead, Transactions are added).
     * @param id the primary key
     * @param name the name of this Account e.g. "Personal Savings"
     * @param accountCode the bank's code for this, e.g. "22636364"
     * @param with which bank holds this account, e.g. "Lloyd's"
     * @param initialBalance the initial balance
     * @param currentBalance the current balance
     */
    public Account(final int id, final String name, final String accountCode,
            final String with, final int initialBalance, final int currentBalance) {
        mId = id;
        mName = name;
        mAccountCode = accountCode;
        mWith = with;
        mInitialBalance = initialBalance;
        mCurrentBalance = currentBalance;
    }

    /**
     * Create an Account. Note that the balance can only be set on
     * initial creation; it is updated when Transactions are added,
     * modified or removed.
     * <p>
     * This version of the constructor is intended for use by
     * client code, when an Account is first created.
     * @param name the name of this Account e.g. "Personal Savings"
     * @param accountCode the bank's code for this, e.g. "22636364"
     * @param with which bank holds this account, e.g. "Lloyd's"
     * @param initialBalance the initial balance
     */
    public Account(final String name, final String accountCode, final String with,
            final int initialBalance) {
        this(-1, name, accountCode, with, initialBalance, initialBalance);
    }

    /**
     * @return the name
     */
    public String getName() {
        return mName;
    }

    /**
     * @param name the name to set
     */
    public void setName(final String name) {
        mName = name;
    }

    /**
     * @return the accountCode
     */
    public String getAccountCode() {
        return mAccountCode;
    }

    /**
     * @param accountCode the accountCode to set
     */
    public void setAccountCode(final String accountCode) {
        mAccountCode = accountCode;
    }

    /**
     * @return the with
     */
    public String getWith() {
        return mWith;
    }

    /**
     * @param with the with to set
     */
    public void setWith(final String with) {
        mWith = with;
    }

    /**
     * @return the id
     */
    public int getId() {
        return mId;
    }

    /**
     * @return the current account balance, after all Transactions have been applied
     */
    public int getCurrentBalance() {
        return mCurrentBalance;
    }

    /**
     * @return the initial account balance, before all Transactions have been applied
     */
    public int getInitialBalance() {
        return mInitialBalance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Account other = (Account) obj;
        return new EqualsBuilder()
            .append(this.mId, other.mId)
            .append(this.mName, other.mName)
            .append(this.mAccountCode, other.mAccountCode)
            .append(this.mWith, other.mWith)
            .append(this.mCurrentBalance, other.mCurrentBalance)
            .append(this.mInitialBalance, other.mInitialBalance)
            .isEquals();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(1, 31)
            .append(mId)
            .append(mName)
            .append(mAccountCode)
            .append(mWith)
            .append(mCurrentBalance)
            .append(mInitialBalance)
            .toHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("id %d name %s account code %s with %s initial balance %d current balance %d",
            mId, mName, mAccountCode, mWith, mInitialBalance, mCurrentBalance);
    }
}
