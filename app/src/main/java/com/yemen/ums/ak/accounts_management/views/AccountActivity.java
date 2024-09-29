package com.yemen.ums.ak.accounts_management.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yemen.ums.ak.accounts_management.R;
import com.yemen.ums.ak.accounts_management.models.Account;
import com.yemen.ums.ak.accounts_management.models.DBHelper;
import com.yemen.ums.ak.accounts_management.models.Transaction;
import com.yemen.ums.ak.accounts_management.viewModel.TransactionsAdapter;

import java.util.ArrayList;

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
        transactionsAdapter = new TransactionsAdapter(context,transactions);
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

    }
}