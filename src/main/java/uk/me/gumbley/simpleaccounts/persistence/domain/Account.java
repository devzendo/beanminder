package uk.me.gumbley.simpleaccounts.persistence.domain;

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
    private final int mBalance;

    /**
     * Create an Account. Note that the balance can only be set on
     * initial creation; it is updated when Transactions are added,
     * modified or removed.
     * <p>
     * This version of the constructor is intended for use by the
     * DAO Layer. Client code should use the other constructor (the
     * one without the primary key) as it does not know the key
     * until the Account is saved.
     * @param id the primary key
     * @param name the name of this Account e.g. "Personal Savings"
     * @param accountCode the bank's code for this, e.g. "22636364"
     * @param with which bank holds this account, e.g. "Lloyd's"
     * @param initialBalance the initial balance
     */
    public Account(final int id, final String name, final String accountCode, final String with, final int initialBalance)
    {
        mId = id;
        mName = name;
        mAccountCode = accountCode;
        mWith = with;
        mBalance = initialBalance;
    }

    /**
     * Create an Account. Note that the balance can only be set on
     * initial creation; it is updated when Transactions are added,
     * modified or removed.
     * <p>
     * This version of the constructor is intended for use by
     * client code.
     * @param name the name of this Account e.g. "Personal Savings"
     * @param accountCode the bank's code for this, e.g. "22636364"
     * @param with which bank holds this account, e.g. "Lloyd's"
     * @param initialBalance the initial balance
     */
    public Account(final String name, final String accountCode, final String with, final int initialBalance)
    {
        this(-1, name, accountCode, with, initialBalance);
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
     * @return the balance
     */
    public int getBalance() {
        return mBalance;
    }
}
