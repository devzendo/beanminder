package uk.me.gumbley.simpleaccounts.persistence.domain;

import java.sql.Date;

/**
 * A Transaction POJO.
 *
 * @author matt
 *
 */
public final class Transaction {
    private final int mId;
    private final int mAccountId;
    private int mAmount;
    private boolean mCredit;
    private boolean mReconciled;
    private Date mTransactionDate;

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
     * @param amount the amount of this transaction, always
     * positive
     * @param credit true if a credit, false if a debit from the
     * account
     * @param reconciled whether this transaction has been
     * reconciled
     * @param transactionDate the date that this transaction was
     * made
     */
    public Transaction(final int id, final int accountId, final int amount, final boolean credit,
            final boolean reconciled, final Date transactionDate) {
        mId = id;
        mAccountId = accountId;
        if (amount < 0) {
            throw new IllegalArgumentException("Transaction amounts must be positive; " + amount + " is negative");
        }
        mAmount = amount;
        mCredit = credit;
        mReconciled = reconciled;
        mTransactionDate = transactionDate;
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
        this(-1, -1, amount, credit, reconciled, transactionDate);
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
        mTransactionDate = transactionDate;
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
}
