package com.yemen.ums.ak.accounts_management.views;

import android.app.Activity;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.yemen.ums.ak.accounts_management.R;
import com.yemen.ums.ak.accounts_management.models.Account;
import com.yemen.ums.ak.accounts_management.models.DBHelper;
import com.yemen.ums.ak.accounts_management.models.MySharedPreferences;
import com.yemen.ums.ak.accounts_management.models.Transaction;
import com.yemen.ums.ak.accounts_management.viewModel.TransactionsAdapter;

import java.util.ArrayList;
import java.util.List;

public class AccountActivity extends AppCompatActivity {
    int accountID;
    DBHelper dbHelper;
    Account account;
    Context context;
    TextView name ,type,mobile,allowmax,balance;
    ImageView photo;
    CheckBox active;
    Button edit,delete;
    ArrayList<Transaction> transactions;
    TransactionsAdapter transactionsAdapter;
    RecyclerView accountTransactions_rv;
    double account_balance = 0;
    Toolbar toolbar;

    EditText d_name ,d_mobile, d_allowmax;
    CheckBox d_active ;
    Button saveEdit_btn;
    ImageView accountImg;
    Account editingAccount;
    Spinner accountType_spnr ;
    List<String> accountType ;
    ArrayAdapter<String> arrayAdapter;
    final int PICK_IMAGE_REQUEST = 100;
    Uri imagePath;
    Bitmap imageBitmap;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        name = findViewById(R.id.accountName_txt);
        type = findViewById(R.id.accountType_txt);
        mobile = findViewById(R.id.accountMobile_txt);
        allowmax = findViewById(R.id.accountAllowmax_txt);
        balance  = findViewById(R.id.accountBalace_txt);
        photo = findViewById(R.id.accountPhoto_img);
        active = findViewById(R.id.accountActive_chk);
        edit = findViewById(R.id.accountEdit_btn);
        delete = findViewById(R.id.accountDelete_btn);
        accountTransactions_rv = findViewById(R.id.accountTransactions_rv);


        accountID = getIntent().getIntExtra("accountID",0);
        context = getApplicationContext();
        dbHelper = new DBHelper(context);
        account = dbHelper.getAccountByID(accountID);
        transactions = (ArrayList<Transaction>) dbHelper.getTransactionsByAccount(accountID);
        transactionsAdapter = new TransactionsAdapter(this,transactions);
        accountTransactions_rv.setLayoutManager(new LinearLayoutManager(context));
        accountTransactions_rv.setAdapter(transactionsAdapter);

        account_balance = Account.getAccountBalance(transactions);

        name.setText(account.getName());
        type.setText(type.getText()+account.getType());
        mobile.setText(mobile.getText()+account.getMobile());
        allowmax.setText(allowmax.getText()+String.valueOf(account.getAllow_max()));
        balance.setText(String.valueOf(account_balance));
        balance.setTextColor(account.isDebit()?Color.GREEN:Color.RED);
        active.setChecked(account.isActive());

        if (account.getPhoto() != null) {
            photo.setImageBitmap(account.getPhoto());
        } else {
            photo.setImageResource(R.mipmap.ic_launcher);
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edit.setOnClickListener(view -> editAccount());
    }

    @Override
    protected void onResume() {
        super.onResume();
        delete.setOnClickListener(view -> deleteAccount());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getTitle().toString()){
            case "logout":
                logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data !=null){
                imagePath = data.getData();
                imageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),imagePath);
                accountImg.setImageBitmap(imageBitmap);
            }
        }catch (Exception ex){
            Toast.makeText(context,ex.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }

    private void deleteAccount(){
        if (Double.parseDouble(balance.getText().toString()) !=0){
            View view = findViewById(R.id.cardView);
            Snackbar.make(view,R.string.msg_cantDeleteAccount,Snackbar.LENGTH_LONG).show();
            return;
        }
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(R.string.txt_delete);
        builder.setMessage(R.string.msg_confirmDelete);

        builder.setPositiveButton(R.string.txt_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dbHelper.deleteAccount(accountID);
                Toast.makeText(context,R.string.msg_delete,Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.txt_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        android.app.AlertDialog alertDialog =builder.create();
        alertDialog.show();
    }

    private void editAccount(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_account,null,false);
        builder.setView(view);

        initiateDialogForm(view);

        alertDialog = builder.create();
        alertDialog.show();
    }

    private void initiateDialogForm(View view){

        d_name = view.findViewById(R.id.accountName_txt);
        d_mobile = view.findViewById(R.id.accountMobile_txt);
        d_allowmax = view.findViewById(R.id.accountAllowmax_txt);
        accountType_spnr = view.findViewById(R.id.accountType_spnr);
        d_active = view.findViewById(R.id.accountActive_chkbx);
        accountImg = view.findViewById(R.id.account_img);
        saveEdit_btn = view.findViewById(R.id.accountSave_btn);

        accountType = Account.ACCOUNT_TYPE;
        arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item,accountType);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountType_spnr.setAdapter(arrayAdapter);


        d_name.setText(account.getName());
        d_mobile.setText(account.getMobile());
        d_allowmax.setText(String.valueOf(account.getAllow_max()));
        d_active.setChecked(account.isActive());
        accountType_spnr.setSelection(arrayAdapter.getPosition(account.getType()));
        if (account.getPhoto() != null) {
            accountImg.setImageBitmap(account.getPhoto());
        } else {
            accountImg.setImageResource(R.mipmap.ic_launcher);
        }
        saveEdit_btn.setText(R.string.txt_update);

        accountImg.setOnClickListener(view1 -> showDialogImages());
        saveEdit_btn.setOnClickListener(view1 -> updateAccount());
    }

    private void showDialogImages(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    private void updateAccount(){
        try {
            editingAccount = new Account();
            editingAccount.setId(accountID);
            editingAccount.setName(d_name.getText().toString());
            editingAccount.setMobile(d_mobile.getText().toString());
            editingAccount.setAllow_max(Double.parseDouble(d_allowmax.getText().toString()));
            editingAccount.setType(accountType.get(accountType_spnr.getSelectedItemPosition()).toString());
            editingAccount.setActive(d_active.isChecked());
            editingAccount.setPhoto((imageBitmap!=null)?imageBitmap:null);
            editingAccount.setCreated(System.currentTimeMillis());
            editingAccount.setUpdated(System.currentTimeMillis());

            dbHelper.updateAccount(editingAccount);
            alertDialog.dismiss();
            Toast.makeText(context,R.string.msg_update,Toast.LENGTH_SHORT).show();
        }catch (Exception ex){
            Toast.makeText(context,ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void logout(){
        MySharedPreferences.logOut(context);
        startActivity(new Intent(context,LoginActivity.class));
        finish();
    }

}