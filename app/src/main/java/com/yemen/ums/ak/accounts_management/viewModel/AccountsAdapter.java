package com.yemen.ums.ak.accounts_management.viewModel;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.yemen.ums.ak.accounts_management.R;
import com.yemen.ums.ak.accounts_management.models.Account;
import com.yemen.ums.ak.accounts_management.models.DBHelper;
import com.yemen.ums.ak.accounts_management.models.Transaction;
import com.yemen.ums.ak.accounts_management.views.AccountActivity;

import java.util.ArrayList;

public class AccountsAdapter  extends RecyclerView.Adapter<AccountsAdapter.ViewHandler> {

    private Context context;
    private ArrayList<Account> accounts;

    public AccountsAdapter(Context context_,ArrayList<Account> accounts_) {
        this.context = context_;
        this.accounts = accounts_;
    }

    @NonNull
    @Override
    public ViewHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.account_list_item,parent,false);

        return new ViewHandler(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHandler holder, int position) {
        Account account = accounts.get(position);
        DBHelper dbHelper = new DBHelper(context);
        ArrayList<Transaction> transactions = (ArrayList<Transaction>) dbHelper.getTransactionsByAccount(account.getId());

        holder.account_name.setText(account.getName());
        holder.account_balance_txt.setText(String.valueOf(Account.getAccountBalance(transactions)));
        if (account.getPhoto() != null) {
            holder.account_photo.setImageBitmap(account.getPhoto());
        } else {
            holder.account_photo.setImageResource(R.mipmap.ic_launcher);
        }

        holder.account_card.setCardBackgroundColor(account.isDebit()? Color.parseColor("#B2DFDB"):Color.parseColor("#FFB2B2"));

        holder.account_card.setOnClickListener(view -> showAccount(account.getId()));
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    class ViewHandler extends RecyclerView.ViewHolder{
        CardView account_card;
        TextView account_name, account_balance_txt;
        ImageView account_photo;

        public ViewHandler(@NonNull View itemView) {
            super(itemView);

            account_card = itemView.findViewById(R.id.account_card);
            account_name = itemView.findViewById(R.id.account_name);
            account_photo = itemView.findViewById(R.id.account_img);
            account_balance_txt = itemView.findViewById(R.id.account_balance);
        }
    }

    private void showAccount(int accountID){
        Intent toAccountActivity = new Intent(context, AccountActivity.class);
        toAccountActivity.putExtra("accountID",accountID);
        startActivity(context,toAccountActivity,null);
    }
}
