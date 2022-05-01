package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DataBaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO extends DataBaseHelper implements TransactionDAO {

    public PersistentTransactionDAO(@Nullable Context context) {
        super(context);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv_1 = new ContentValues();
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        String strDate = calender.get(Calendar.YEAR) + "," + calender.get(Calendar.MONTH) +","+ calender.get(Calendar.DAY_OF_MONTH);
        cv_1.put("ACCOUNT_NO",accountNo);
        cv_1.put("COLUMN_DATE",strDate);
        cv_1.put("AMOUNT",amount);
        cv_1.put("EXPENSE_TYPE",String.valueOf(expenseType));
        db.insert("TRANSACTIONS",null,cv_1);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> allTrans = new ArrayList<>();
        String queryString = "SELECT * FROM TRANSACTIONS";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);
        if (cursor.moveToFirst()){
            do{
            String[] date_str = cursor.getString(1).split(",");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.parseInt(date_str[0]),Integer.parseInt(date_str[1]), Integer.parseInt(date_str[2]));
            Date date_tr = calendar.getTime();
            String accNo = cursor.getString(2);
            String expenseType_str = cursor.getString(3);
            ExpenseType expenseType = ExpenseType.valueOf(expenseType_str.toUpperCase());
            double amount = cursor.getDouble(4);
            Transaction tr = new Transaction(date_tr,accNo,expenseType,amount);
            allTrans.add(tr);

            }while(cursor.moveToNext());
        }
        else{}
        cursor.close();
        db.close();
        return allTrans;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List <Transaction> tr= getAllTransactionLogs();
        int size = tr.size();

        if (size <= limit) {
            return tr;
        }

        return tr.subList(size - limit, size);
    }
}
