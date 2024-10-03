package com.yemen.ums.ak.accounts_management.models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "accountManagement";
    private static final int VERSION = 1 ;



    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Account.CREATE_TABLE);
        sqLiteDatabase.execSQL(Transaction.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(Account.DROP_TABLE);
        sqLiteDatabase.execSQL(Transaction.DROP_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void insertAccount(Account account){
        SQLiteDatabase database = getWritableDatabase();

        database.insert(
                Account.TABLE_NAME,
                null,
                account.getContentValues()
        );
        database.close();
    }

    public List<Account> getAccounts(){
        List<Account> accountList = new ArrayList<>();

        SQLiteDatabase database = getReadableDatabase();

        Cursor cursor = database.query(
                Account.TABLE_NAME,
                Account.COLUMNS,
                null,
                null,
                null,
                null,
                Account.COLUMN_NAME
        );

        if (cursor.moveToFirst()){
            do {
                Account account = new Account();
                account.setId(cursor.getInt(0));
                account.setName(cursor.getString(1));
                account.setMobile(cursor.getString(2));
                account.setAllow_max(cursor.getDouble(3));
                account.setType(cursor.getString(5));
                account.setActive((cursor.getInt(6)==1)?true:false);
                account.setCreated(cursor.getLong(7));
                account.setUpdated(cursor.getLong(8));

                if (cursor.getBlob(4)!=null){
                    byte[] photoByte = cursor.getBlob(4);
                    Bitmap photoBitmap = BitmapFactory.decodeByteArray(photoByte,0,photoByte.length);
                    account.setPhoto(photoBitmap);
                }

                accountList.add(account);
            }while (cursor.moveToNext());
        }
        return accountList;
    }

    public List<Account> getActiveAccounts(){
        List<Account> accountList = new ArrayList<>();

        SQLiteDatabase database = getReadableDatabase();

        Cursor cursor = database.query(
                Account.TABLE_NAME,
                Account.COLUMNS,
                Account.COLUMN_ACTIVE+" =? ",
                new String[]{"1"},
                null,
                null,
                Account.COLUMN_NAME
        );

        if (cursor.moveToFirst()){
            do {
                Account account = new Account();
                account.setId(cursor.getInt(0));
                account.setName(cursor.getString(1));
                account.setMobile(cursor.getString(2));
                account.setAllow_max(cursor.getDouble(3));
                account.setType(cursor.getString(5));
                account.setActive((cursor.getInt(6)==1)?true:false);
                account.setCreated(cursor.getLong(7));
                account.setUpdated(cursor.getLong(8));

                if (cursor.getBlob(4)!=null){
                    byte[] photoByte = cursor.getBlob(4);
                    Bitmap photoBitmap = BitmapFactory.decodeByteArray(photoByte,0,photoByte.length);
                    account.setPhoto(photoBitmap);
                }

                accountList.add(account);
            }while (cursor.moveToNext());
        }
        return accountList;
    }

    public Account getAccountByID(int accountID){
        SQLiteDatabase database = getReadableDatabase();
        Account account = new Account();
        Cursor cursor = database.rawQuery(
                "SELECT * FROM "+Account.TABLE_NAME+" WHERE "+Account.COLUMN_ID+" =? ",
                new String[]{String.valueOf(accountID)}

        );

        if (cursor.moveToFirst()){
            account.setId(cursor.getInt(0));
            account.setName(cursor.getString(1));
            account.setMobile(cursor.getString(2));
            account.setAllow_max(cursor.getDouble(3));
            account.setType(cursor.getString(5));
            account.setActive((cursor.getInt(6)==1)?true:false);
            account.setCreated(cursor.getLong(7));
            account.setUpdated(cursor.getLong(8));

            if (cursor.getBlob(4)!=null){
                byte[] photoByte = cursor.getBlob(4);
                Bitmap photoBitmap = BitmapFactory.decodeByteArray(photoByte,0,photoByte.length);
                account.setPhoto(photoBitmap);
            }
        }

        return account ;
    }
    public Account updateAccount(Account account){
        SQLiteDatabase database = getWritableDatabase();

        database.update(
                Account.TABLE_NAME,
                account.getContentValues(),
                Account.COLUMN_ID+" =? ",
                new String[]{String.valueOf(account.getId())}
        );
        return this.getAccountByID(account.getId());

    }

    public void deleteAccount(int accountID){
        SQLiteDatabase database = getWritableDatabase();

        database.delete(
                Account.TABLE_NAME,
                Account.COLUMN_ID+" =? ",
                new String[]{String.valueOf(accountID)}
        );
        database.close();
    }

    public void insertTransaction(Transaction transaction){
        SQLiteDatabase database = getWritableDatabase();

        database.insert(
                Transaction.TABLE_NAME,
                null,
                transaction.getContentValues()
        );

        database.close();
    }

    public List<Transaction> getTransactiions(){
        List<Transaction> transactionList = new ArrayList<>();

        SQLiteDatabase database = getReadableDatabase();

        Cursor cursor = database.rawQuery(
                Transaction.SELECT_TRANSACTIONS_JOIN_ACCOUNTS,
                null
        );

        if (cursor.moveToFirst()){
            Transaction newTransaction;
            do {
                newTransaction = new Transaction();
                newTransaction.setId(cursor.getInt(0));
                newTransaction.setAccount(cursor.getInt(1));
                newTransaction.setAccountName(cursor.getString(7));
                newTransaction.setType(cursor.getString(2));
                newTransaction.setBalance(cursor.getDouble(3));
                newTransaction.setNote(cursor.getString(4));
                newTransaction.setCreated(cursor.getLong(5));
                newTransaction.setUpdated(cursor.getLong(6));

                transactionList.add(newTransaction);
            }while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return transactionList;

    }

    public Transaction getTransactionByID(int transactionID){
        SQLiteDatabase database = getReadableDatabase();
        Transaction transaction = new Transaction();
        Cursor cursor = database.rawQuery(
                "SELECT * FROM "+Transaction.TABLE_NAME+" WHERE "+Transaction.COLUMN_ID+" =? ",
                new String[]{String.valueOf(transactionID)}

        );

        if (cursor.moveToFirst()){
            transaction.setId(cursor.getInt(0));
            transaction.setAccount(cursor.getInt(1));
            transaction.setType(cursor.getString(2));
            transaction.setBalance(cursor.getDouble(3));
            transaction.setNote(cursor.getString(4));
            transaction.setCreated(cursor.getLong(5));
            transaction.setUpdated(cursor.getLong(6));
        }

        return transaction ;
    }

    public Transaction updateTransaction(Transaction transaction){
        SQLiteDatabase database = getWritableDatabase();

        database.update(
                Transaction.TABLE_NAME,
                transaction.getContentValues(),
                Transaction.COLUMN_ID+" =? ",
                new String[]{String.valueOf(transaction.getId())}
        );
        return this.getTransactionByID(transaction.getId());

    }

    public void deleteTransaction(int transactionID){
        SQLiteDatabase database = getWritableDatabase();

        database.delete(
                Transaction.TABLE_NAME,
                Transaction.COLUMN_ID+" =? ",
                new String[]{String.valueOf(transactionID)}
        );
        database.close();
    }

    public List<Transaction> getTransactionsByAccount(int accountID){
        List<Transaction> transactions = new ArrayList<>();

        SQLiteDatabase database = getReadableDatabase();

        Cursor cursor = database.query(
                Transaction.TABLE_NAME,
                Transaction.COLUMNS,
                Transaction.COLUMN_ACCOUNT+" =? ",
                new String[]{String.valueOf(accountID)},
                null,
                null,
                Transaction.COLUMN_UPDATED+" DESC "
        );

        if (cursor.moveToFirst()){
            Transaction newTransaction;
            do {
                newTransaction = new Transaction();
                newTransaction.setId(cursor.getInt(0));
                newTransaction.setAccount(cursor.getInt(1));
                newTransaction.setType(cursor.getString(2));
                newTransaction.setBalance(cursor.getDouble(3));
                newTransaction.setNote(cursor.getString(4));
                newTransaction.setCreated(cursor.getLong(5));
                newTransaction.setUpdated(cursor.getLong(6));

                transactions.add(newTransaction);
            }while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return transactions;
    }
}
