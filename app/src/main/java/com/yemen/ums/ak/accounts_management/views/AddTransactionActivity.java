package com.yemen.ums.ak.accounts_management.views;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.yemen.ums.ak.accounts_management.R;
import com.yemen.ums.ak.accounts_management.models.Account;
import com.yemen.ums.ak.accounts_management.models.DBHelper;
import com.yemen.ums.ak.accounts_management.models.Transaction;

import java.util.ArrayList;
import java.util.List;

public class AddTransactionActivity extends AppCompatActivity {

    Context context;
    EditText balance,note;
    Button transactionSaveBtn;
    DBHelper dbHelper ;
    Transaction newTransaction;
    Spinner account_spnr ;
    Spinner transactionType_spnr;
    List<String> transactionType ;
    List<Account> accounts ;
    ArrayAdapter<Account> accountAdapter;
    ArrayAdapter<String> transactionTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_transaction);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        context = getApplicationContext();
        dbHelper = new DBHelper(context);
        balance = findViewById(R.id.balance_txt);
        note = findViewById(R.id.note_txt);
        transactionSaveBtn = findViewById(R.id.transactionSave_btn);
        account_spnr = findViewById(R.id.account_spnr);
        transactionType_spnr = findViewById(R.id.transactionType_spnr);

        transactionType = Transaction.TRANSACTION_TYPE;
        transactionTypeAdapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_dropdown_item,transactionType);
        transactionTypeAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        transactionType_spnr.setAdapter(transactionTypeAdapter);

        accounts = dbHelper.getAccounts();
        accountAdapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_dropdown_item,accounts);
        accountAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        account_spnr.setAdapter(accountAdapter);

        transactionSaveBtn.setOnClickListener(view -> {saveTransaction();});

    }

    private void saveTransaction(){
        try {
            newTransaction =new Transaction();
            newTransaction.setAccount(accounts.get(account_spnr.getSelectedItemPosition()).getId());
            newTransaction.setBalance(Double.parseDouble(balance.getText().toString()));
            newTransaction.setType(Transaction.TRANSACTION_TYPE.get(transactionType_spnr.getSelectedItemPosition()));
            newTransaction.setNote(note.getText().toString());
            newTransaction.setCreated(System.currentTimeMillis());
            newTransaction.setUpdated(System.currentTimeMillis());

            dbHelper.insertTransaction(newTransaction);
            Toast.makeText(context,"Successfylly",Toast.LENGTH_SHORT).show();
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_SHORT).show();
        }


    }
}