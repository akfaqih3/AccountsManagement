package com.yemen.ums.ak.accounts_management.models;

import android.content.ContentValues;

import java.util.ArrayList;
import java.util.Arrays;

public class Transaction {

    public static final String TABLE_NAME = "transactions";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ACCOUNT = "account";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_BALANCE = "balance";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_CREATED = "created";
    public static final String COLUMN_UPDATED = "updated";

    public static final ArrayList<String> TRANSACTION_TYPE = new ArrayList<>(Arrays.asList("deposit","withdraw"));
    public static  final String[] COLUMNS = {COLUMN_ID,COLUMN_ACCOUNT,COLUMN_TYPE,COLUMN_BALANCE,COLUMN_NOTE,COLUMN_CREATED,COLUMN_UPDATED};
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_ACCOUNT + " INTEGER,"
            + COLUMN_TYPE + " TEXT,"
            + COLUMN_BALANCE + " REAL,"
            + COLUMN_NOTE + " TEXT,"
            + COLUMN_CREATED + " TEXT,"
            + COLUMN_UPDATED + " TEXT"
            + ")";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String SELECT_TRANSACTIONS_JOIN_ACCOUNTS = "SELECT "
            +TABLE_NAME+".*"
            +","+Account.TABLE_NAME+"."+Account.COLUMN_NAME
            +" FROM "+TABLE_NAME+" JOIN "+Account.TABLE_NAME+" ON "+Account.TABLE_NAME+"."+Account.COLUMN_ID+" = "+TABLE_NAME+"."+COLUMN_ACCOUNT ;

    public ContentValues getContentValues(){
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_ACCOUNT,this.account);
        contentValues.put(COLUMN_TYPE,this.type);
        contentValues.put(COLUMN_BALANCE,this.balance);
        contentValues.put(COLUMN_NOTE,this.note);
        contentValues.put(COLUMN_CREATED,this.created);
        contentValues.put(COLUMN_UPDATED,this.updated);

        return contentValues;
    }
    private int id ;
    private int account ;
    private String accountName ;
    private String type ;
    private double balance ;
    private String note ;
    private long created ;
    private long updated ;

    public Transaction() {

    }

    public Transaction(int account, String type, double balance, String note, long created, long updated) {
        this.account = account;
        this.type = type;
        this.balance = balance;
        this.note = note;
        this.created = created;
        this.updated = updated;
    }

    public Transaction(int id, int account, String type, double balance, String note, long created, long updated) {
        this.id = id;
        this.account = account;
        this.type = type;
        this.balance = balance;
        this.note = note;
        this.created = created;
        this.updated = updated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public Boolean isWithdraw(){
        return this.getType().equalsIgnoreCase(TRANSACTION_TYPE.get(1));
    }
}
