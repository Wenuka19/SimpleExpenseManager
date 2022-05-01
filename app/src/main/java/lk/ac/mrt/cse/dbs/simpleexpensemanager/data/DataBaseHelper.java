package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(@Nullable Context context) {
        super(context, "expenses.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableAccounts= "CREATE TABLE ACCOUNTS ( ACCOUNT_NO TEXT PRIMARY KEY, BANK_NAME TEXT, ACCOUNT_HOLDER_NAME TEXT ,BALANCE DOUBLE)";
        String createTransactionTable = "CREATE TABLE TRANSACTIONS (TRANSACTION_ID INTEGER PRIMARY KEY AUTOINCREMENT,TRANSACTION_DATE TEXT, ACCOUNT_NO TEXT, EXPENSE_TYPE TEXT, AMOUNT DOUBLE)";
        db.execSQL(createTableAccounts);
        db.execSQL(createTransactionTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
