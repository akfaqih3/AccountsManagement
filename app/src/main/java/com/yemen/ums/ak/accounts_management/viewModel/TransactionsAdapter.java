package com.yemen.ums.ak.accounts_management.viewModel;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.yemen.ums.ak.accounts_management.R;
import com.yemen.ums.ak.accounts_management.models.Transaction;
import com.yemen.ums.ak.accounts_management.views.AccountActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {

    Context context ;
    ArrayList<Transaction> transactions ;

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

        holder.cardView.setCardBackgroundColor(transaction.isWithdraw()? Color.parseColor("#FFB2B2"):Color.parseColor("#B2DFDB"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        holder.date.setText(dateFormat.format(transaction.getUpdated()));

        holder.account_name.setOnClickListener(view -> showAccount(transaction.getAccount()));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
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
}
