package com.yemen.ums.ak.accounts_management.viewModel;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.yemen.ums.ak.accounts_management.R;
import com.yemen.ums.ak.accounts_management.models.Account;
import com.yemen.ums.ak.accounts_management.models.DBHelper;
import com.yemen.ums.ak.accounts_management.models.Transaction;
import com.yemen.ums.ak.accounts_management.views.AccountActivity;
import com.yemen.ums.ak.accounts_management.views.TransactionsFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {

    Context context ;
    ArrayList<Transaction> transactions ;

    //declare variables for dialog

    EditText balance,note;
    Button transactionSaveBtn;
    Transaction newTransaction;
    Spinner account_spnr ;
    Spinner transactionType_spnr;
    List<String> transactionType ;
    List<Account> accounts ;
    ArrayAdapter<Account> accountAdapter;
    ArrayAdapter<String> transactionTypeAdapter;
    AlertDialog alertDialog;
    DBHelper dbHelper;


    public TransactionsAdapter(Context context_, ArrayList<Transaction> transactions_) {
        this.context = context_;
        this.transactions = transactions_;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.transaction_list_item,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Transaction transaction = transactions.get(position);
        holder.account_name.setText(transaction.getAccountName());

        holder.balance.setText(String.valueOf(transaction.getBalance()));
        holder.note.setText(transaction.getNote());
        holder.type.setText(transaction.getType());

        holder.balance.setTextColor(transaction.isWithdraw()? Color.parseColor("#ff0000"):Color.parseColor("#00ff00"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        holder.date.setText(dateFormat.format(transaction.getUpdated()));

        holder.account_name.setOnClickListener(view -> showAccount(transaction.getAccount()));
        holder.cardView.setOnClickListener(view -> showTransactionPopup(transaction));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public void submitList(List<Transaction> transactions_){
        transactions.clear();
        transactions.addAll(transactions_);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView account_name,balance,note,type,date;
        CardView cardView ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.transaction_cv);
            account_name = itemView.findViewById(R.id.transaction_accountName);
            balance = itemView.findViewById(R.id.transaction_balance);
            note = itemView.findViewById(R.id.transaction_note);
            type = itemView.findViewById(R.id.transaction_type);
            date = itemView.findViewById(R.id.transaction_date);
        }
    }

    private void showAccount(int accountID){
        Intent toAccountActivity = new Intent(context, AccountActivity.class);
        toAccountActivity.putExtra("accountID",accountID);
        startActivity(context,toAccountActivity,null);
    }

    private void showTransactionPopup(Transaction transaction){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(transaction.getAccountName()+"         "+transaction.getBalance());
        builder.setMessage(transaction.getNote());

        builder.setNegativeButton(R.string.txt_update, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showTransactionDialog(transaction);
            }
        });

        builder.setNeutralButton(R.string.txt_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dbHelper = new DBHelper(context);
                dbHelper.deleteTransaction(transaction.getId());
                TransactionsFragment.viewModelTransactions.setTransactions(dbHelper.getTransactiions());
                AccountActivity.viewModelTransactions.setTransactions(dbHelper.getTransactiions());
                Toast.makeText(context,R.string.msg_delete,Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    private void showTransactionDialog(Transaction transaction){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_transaction,null,false);
        builder.setView(view);
        alertDialog = builder.create();
        initialDialogForm(view,transaction);
        alertDialog.show();
    }

    private void initialDialogForm(View view,Transaction transaction){
        balance = view.findViewById(R.id.balance_txt);
        note = view.findViewById(R.id.note_txt);
        transactionSaveBtn = view.findViewById(R.id.transactionSave_btn);
        account_spnr = view.findViewById(R.id.account_spnr);
        transactionType_spnr = view.findViewById(R.id.transactionType_spnr);

        transactionType = Transaction.TRANSACTION_TYPE;
        transactionTypeAdapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_dropdown_item,transactionType);
        transactionTypeAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        transactionType_spnr.setAdapter(transactionTypeAdapter);

        dbHelper = new DBHelper(context);
        accounts = dbHelper.getActiveAccounts();
        accountAdapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_dropdown_item,accounts);
        accountAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        account_spnr.setAdapter(accountAdapter);

        balance.setText(String.valueOf(transaction.getBalance()));
        note.setText(String.valueOf(transaction.getNote()));

        account_spnr.setSelection(accountAdapter.getPosition(
                accounts.stream().filter(account -> account.getId() == transaction.getAccount())
                        .findAny().orElse(accounts.get(0))
                )
        );

        transactionType_spnr.setSelection(transactionTypeAdapter.getPosition(transaction.getType()));
        transactionSaveBtn.setText(R.string.transaction_edit);

        transactionSaveBtn.setOnClickListener(view1 -> updateTransaction(transaction.getId()));
    }

    private void updateTransaction(int transactionID){
        try {
            newTransaction =new Transaction();
            newTransaction.setId(transactionID);
            newTransaction.setAccount(accounts.get(account_spnr.getSelectedItemPosition()).getId());
            newTransaction.setBalance(Double.parseDouble(balance.getText().toString()));
            newTransaction.setType(Transaction.TRANSACTION_TYPE.get(transactionType_spnr.getSelectedItemPosition()));
            newTransaction.setNote(note.getText().toString());
            newTransaction.setCreated(System.currentTimeMillis());
            newTransaction.setUpdated(System.currentTimeMillis());

            dbHelper.updateTransaction(newTransaction);
            alertDialog.dismiss();
            TransactionsFragment.viewModelTransactions.setTransactions(dbHelper.getTransactiions());
            AccountActivity.viewModelTransactions.setTransactions(dbHelper.getTransactiions());
            Toast.makeText(context,R.string.msg_update,Toast.LENGTH_SHORT).show();
        }catch (Exception ex){
            Toast.makeText(context,ex.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }

}
