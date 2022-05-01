package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DataBaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.MainActivity;

public class PersistentAccountDAO extends DataBaseHelper implements AccountDAO {

    public PersistentAccountDAO(@Nullable Context context) {
        super(context);
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accNumList = new ArrayList<>();
        String queryString = "SELECT ACCOUNT_NO FROM ACCOUNTS";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);
        if (cursor.moveToFirst()){
            do{
                accNumList.add(cursor.getString(0));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return accNumList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accountList = new ArrayList<>();
        String queryString = "SELECT * FROM ACCOUNTS";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);
        if (cursor.moveToFirst()){
            do {
                String accountNo = cursor.getString(0);
                String bankName = cursor.getString(1);
                String accHolderName = cursor.getString(2);
                double balance = cursor.getShort(3);

                Account account = new Account(accountNo,bankName,accHolderName,balance);
                accountList.add(account);
            } while (cursor.moveToNext());
        }
        else{

        }
        cursor.close();
        db.close();
        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account acc;
        String query = "SELECT * FROM ACCOUNTS WHERE ACCOUNT_NO = '"+accountNo+"';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        String bankName = cursor.getString(1);
        String accHolderName = cursor.getString(2);
        Double balance = cursor.getDouble(3);
        acc = new Account(accountNo,bankName,accHolderName,balance);
        cursor.close();
        db.close();
        return acc;
    }

    @Override
    public void addAccount(Account account){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ACCOUNT_NO",account.getAccountNo());
        cv.put("BANK_NAME",account.getBankName());
        cv.put("ACCOUNT_HOLDER_NAME",account.getAccountHolderName());
        cv.put("BALANCE",account.getBalance());
        db.insert("ACCOUNTS",null,cv);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM ACCOUNTS WHERE ACCOUNT_NO = '"+accountNo+"';";
        db.execSQL(query);

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryUpdate = "SELECT BALANCE FROM ACCOUNTS WHERE ACCOUNT_NO = '"+accountNo+"';";
        Cursor cursor = db.rawQuery(queryUpdate,null);
        if (!cursor.moveToFirst()){
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        else{
            double balance = cursor.getDouble(0);
            switch (expenseType){
                case EXPENSE:
                    balance = balance-amount;
                    break;
                case INCOME:
                    balance = balance+amount;
                    break;
            }
            String query = "UPDATE ACCOUNTS SET BALANCE= "+ balance +" WHERE ACCOUNT_NO = '"+accountNo+"';";
            db.execSQL(query);
            cursor.close();
            db.close();
        }

    }
}
