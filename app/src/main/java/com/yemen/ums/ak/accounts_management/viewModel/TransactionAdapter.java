package com.yemen.ums.ak.accounts_management.viewModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.yemen.ums.ak.accounts_management.R;
import com.yemen.ums.ak.accounts_management.models.Account;
import com.yemen.ums.ak.accounts_management.models.DBHelper;
import com.yemen.ums.ak.accounts_management.models.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class TransactionAdapter extends ArrayAdapter<Transaction> {

    private final Context context;
    private final int resource ;
    private final ArrayList<Transaction> transactions ;
    public TransactionAdapter(@NonNull Context context, int resource, ArrayList<Transaction> transactions) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.transactions = transactions;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @SuppressLint("ViewHolder")
        View transaction_list_item = layoutInflater.inflate(resource,parent,false);

        TextView account_name,balance,type,date;
        CardView cardView ;

        cardView = transaction_list_item.findViewById(R.id.transaction_cv);
        account_name = transaction_list_item.findViewById(R.id.transaction_accountName);
        balance = transaction_list_item.findViewById(R.id.transaction_balance);
        type = transaction_list_item.findViewById(R.id.transaction_type);
        date = transaction_list_item.findViewById(R.id.transaction_date);

        Transaction transaction = transactions.get(position);
        DBHelper dbHelper = new DBHelper(getContext());
        Account account = dbHelper.getAccountByID(transaction.getAccount());
        account_name.setText(account.getName());
        balance.setText(String.valueOf(transaction.getBalance()));
        type.setText(transaction.getType());

        cardView.setCardBackgroundColor(transaction.isWithdraw()? Color.parseColor("#FFB2B2"):Color.parseColor("#B2DFDB"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        date.setText(dateFormat.format(transaction.getUpdated()));


        return transaction_list_item ;
    }

}
