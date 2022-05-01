package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.util.Log;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.MainActivity;

public class PersistentDemoExpenseManager extends ExpenseManager{
    private Context context;

    public PersistentDemoExpenseManager(Context context) throws ExpenseManagerException {
        this.context=context;
        setup();
    }

    @Override
    public void setup() throws ExpenseManagerException {
        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(this.context);
        setTransactionsDAO(persistentTransactionDAO);

        AccountDAO persistentAccountDAO = new PersistentAccountDAO(this.context);
        setAccountsDAO(persistentAccountDAO);

        Account ac1 = new Account("1123","Bank A","Sara",12000.0);
        Account ac2 = new Account("112","Bank B","Clay",1500.0);
        getAccountsDAO().addAccount(ac1);
        getAccountsDAO().addAccount(ac2);
    }
}
