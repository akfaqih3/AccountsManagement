package com.yemen.ums.ak.accounts_management.models;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Account {

    public static final String TABLE_NAME = "accounts";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_MOBILE = "mobile";
    public static final String COLUMN_ALLOW_MAX = "allow_max";
    public static final String COLUMN_PHOTO = "photo";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_ACTIVE = "active";
    public static final String COLUMN_CREATED = "created";
    public static final String COLUMN_UPDATED = "updated";

    public static final ArrayList<String> ACCOUNT_TYPE = new ArrayList<>(Arrays.asList("debit","credit"));
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_MOBILE + " TEXT,"
            + COLUMN_ALLOW_MAX + " REAL,"
            + COLUMN_PHOTO + " BLOB,"
            + COLUMN_TYPE + " TEXT,"
            + COLUMN_ACTIVE + " INTEGER,"
            + COLUMN_CREATED + " INTEGER,"
            + COLUMN_UPDATED + " INTEGER"
            + ")";

    // جملة حذف الجدول (للاستخدام بحذر)
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    public static final String[] COLUMNS = {COLUMN_ID,COLUMN_NAME,COLUMN_MOBILE,COLUMN_ALLOW_MAX,COLUMN_PHOTO,COLUMN_TYPE,COLUMN_ACTIVE,COLUMN_CREATED,COLUMN_UPDATED};

    public ContentValues getContentValues(){
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NAME,this.name);
        contentValues.put(COLUMN_MOBILE,this.mobile);
        contentValues.put(COLUMN_ALLOW_MAX,this.allow_max);
        if (this.photo!=null){
            Bitmap imageBitmap = this.getPhoto();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
            byte[] photoByteArray = outputStream.toByteArray();

            contentValues.put(COLUMN_PHOTO,photoByteArray);
        }
        contentValues.put(COLUMN_TYPE,this.type);
        contentValues.put(COLUMN_ACTIVE,(this.active==true)?1:0);
        contentValues.put(COLUMN_CREATED,this.created);
        contentValues.put(COLUMN_UPDATED,this.updated);

        return contentValues;
    }

    private int id ;
    private String name ;
    private String mobile ;
    private double allow_max ;
    private Bitmap photo ;
    private String type ;
    private boolean active ;
    private long created ;
    private long updated ;

    public Account() {

    }

    public Account(String name, String mobile, double allow_max,Bitmap photo, String type, boolean active, long created, long updated) {
        this.name = name;
        this.mobile = mobile;
        this.allow_max = allow_max;
        this.photo = photo;
        this.type = type;
        this.active = active;
        this.created = created;
        this.updated = updated;
    }

    public Account(int id, String name, String mobile, double allow_max, Bitmap photo, String type, boolean active, long created, long updated) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.allow_max = allow_max;
        this.photo = photo;
        this.type = type;
        this.active = active;
        this.created = created;
        this.updated = updated;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public double getAllow_max() {
        return allow_max;
    }

    public void setAllow_max(double allow_max) {
        this.allow_max = allow_max;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    @Override
    public String toString() {
        return name ;
    }

    public Boolean isDebit(){
        return this.getType().equalsIgnoreCase(ACCOUNT_TYPE.get(0));
    }

    public static double getAccountBalance(ArrayList<Transaction> transactions) {
        double account_balance = 0;

        for (int k = 0; k < transactions.size(); k++) {
            if (transactions.get(k).isWithdraw()) {
                account_balance += transactions.get(k).getBalance();
            } else {
                account_balance -= transactions.get(k).getBalance();
            }
        }
        return account_balance;
    }


    public ArrayList<Transaction> getTransactions(Context context){
        ArrayList<Transaction> transactionList ;
        DBHelper dbHelper = new DBHelper(context);
        transactionList =(ArrayList<Transaction>) dbHelper.getTransactionsByAccount(this.getId());
        return transactionList;
    }

    public Boolean isAllow(Context context,double balance){
        ArrayList<Transaction> transactionList = this.getTransactions(context);
        Double allBalance = Account.getAccountBalance(transactionList);
        Double allowMax = this.getAllow_max();

        return (balance+allBalance) <= allowMax ;
    }

}
