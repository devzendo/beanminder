package uk.me.gumbley.simpleaccounts.persistence.domain;

import java.sql.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.devzendo.commoncode.datetime.SQLDateUtils;


/**
 * A Transaction POJO.
 *
 * @author matt
 *
 */
public final class Transaction {
    private final int mId;
    private final int mAccountId;
    private final int mIndex;
    private int mAmount;
    private boolean mCredit;
    private boolean mReconciled;
    private Date mTransactionDate;
    private int mAccountBalance;

    /**
     * Construct a Transaction.
     * <p>
     * This version of the constructor is intended for use by the
     * DAO Layer. Client code should use the other constructor (the
     * one without the keys) as it does not know the primary key
     * until the Transaction is saved, and the DAO Layer fills in
     * the foreign key to Account.
     * @param id the primary key
     * @param accountId the foreign key to the Account
     * @param index the transaction index, starts at 0, increments monotonically
     * @param amount the amount of this transaction, always
     * positive
     * @param credit true if a credit, false if a debit from the
     * account
     * @param reconciled whether this transaction has been
     * reconciled
     * @param transactionDate the date that this transaction was
     * made; this will be normalised to an SQL Date
     * @param accountBalance the balance of the account after this transaction
     * has been applied to it
     */
    public Transaction(final int id, final int accountId, final int index,
            final int amount, final boolean credit,
            final boolean reconciled, final Date transactionDate,
            final int accountBalance) {
        mId = id;
        mAccountId = accountId;
        mIndex = index;
        if (amount < 0) {
            throw new IllegalArgumentException("Transaction amounts must be positive; " + amount + " is negative");
        }
        mAmount = amount;
        mCredit = credit;
        mReconciled = reconciled;
        mTransactionDate = SQLDateUtils.normalise(transactionDate);
        mAccountBalance = accountBalance;
    }

    /**
     * Construct a Transaction.
     * <p>
     * This version of the constructor is intended for use by
     * client code.
     * @param amount the amount of this transaction, always
     * positive
     * @param credit true if a credit, false if a debit from the
     * account
     * @param reconciled whether this transaction has been
     * reconciled
     * @param transactionDate the date that this transaction was
     * made
     */
    public Transaction(final int amount, final boolean credit,
            final boolean reconciled, final Date transactionDate) {
        this(-1, -1, -1, amount, credit, reconciled, transactionDate, -1);
    }

    /**
     * @return the amount
     */
    public int getAmount() {
        return mAmount;
    }
    
    /**
     * @param amount the amount to set
     */
    public void setAmount(final int amount) {
        mAmount = amount;
    }
    
    /**
     * @return the credit
     */
    public boolean isCredit() {
        return mCredit;
    }
    
    /**
     * @param credit the credit to set
     */
    public void setCredit(final boolean credit) {
        mCredit = credit;
    }
    
    /**
     * @return the reconciled
     */
    public boolean isReconciled() {
        return mReconciled;
    }
    
    /**
     * @param reconciled the reconciled to set
     */
    public void setReconciled(final boolean reconciled) {
        mReconciled = reconciled;
    }
    
    /**
     * @return the transactionDate
     */
    public Date getTransactionDate() {
        return mTransactionDate;
    }
    
    /**
     * @param transactionDate the transactionDate to set
     */
    public void setTransactionDate(final Date transactionDate) {
        mTransactionDate = SQLDateUtils.normalise(transactionDate);
    }
    
    /**
     * @return the id
     */
    public int getId() {
        return mId;
    }
    
    /**
     * @return the accountId
     */
    public int getAccountId() {
        return mAccountId;
    }

    /**
     * @return the account balance after this transaction has been applied
     */
    public int getAccountBalance() {
        return mAccountBalance;
    }

    /**
     * @return the transaction index
     */
    public int getIndex() {
        return mIndex;
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
        final Transaction other = (Transaction) obj;
        return new EqualsBuilder()
            .append(this.mId, other.mId)
            .append(this.mAccountId, other.mAccountId)
            .append(this.mIndex, other.mIndex)
            .append(this.mAmount, other.mAmount)
            .append(this.mCredit, other.mCredit)
            .append(this.mReconciled, other.mReconciled)
            // SQL Dates don't compare well :(
            .append(this.mTransactionDate.toString(), other.mTransactionDate.toString())
            .append(this.mAccountBalance, other.mAccountBalance)
            .isEquals();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(1, 31)
            .append(mId)
            .append(mAccountId)
            .append(mIndex)
            .append(mAmount)
            .append(mCredit)
            .append(mReconciled)
            .append(mTransactionDate)
            .append(mAccountBalance)
            .toHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("id %d account id %d index %d amount %d credit? %s reconciled? %s date %s a/c balance %d",
            mId, mAccountId, mIndex, mAmount, mCredit, mReconciled, mTransactionDate, mAccountBalance);
    }
}
