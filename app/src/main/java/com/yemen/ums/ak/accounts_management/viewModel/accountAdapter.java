//package com.yemen.ums.ak.accounts_management.viewModel;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.graphics.Color;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.ColorInt;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.cardview.widget.CardView;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.yemen.ums.ak.accounts_management.R;
//import com.yemen.ums.ak.accounts_management.models.Account;
//import com.yemen.ums.ak.accounts_management.models.DBHelper;
//import com.yemen.ums.ak.accounts_management.models.Transaction;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class accountAdapter extends ArrayAdapter<Account> {
//
//    private final Context context;
//    private final int resource;
//    private final ArrayList<Account> accountList;
//
//    public accountAdapter(@NonNull Context context, int resource, ArrayList<Account> _accountList) {
//        super(context, resource);
//        this.context = context;
//        this.resource = resource;
//        this.accountList = _accountList;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
//        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        @SuppressLint("ViewHolder")
//        View account_list_item = layoutInflater.inflate(resource, parent, false);
//
//        CardView account_card;
//        TextView account_name, account_balance_txt;
//        ImageView account_photo;
//
//        account_card = account_list_item.findViewById(R.id.account_card);
//        account_name = account_list_item.findViewById(R.id.account_name);
//        account_photo = account_list_item.findViewById(R.id.account_img);
//        account_balance_txt = account_list_item.findViewById(R.id.account_balance);
//
//        Account account = accountList.get(position);
//
//        DBHelper dbHelper = new DBHelper(getContext());
//        ArrayList<Transaction> transactions = (ArrayList<Transaction>) dbHelper.getTransactionsByAccount(account.getId());
//
//
//        account_name.setText(account.getName());
//        account_balance_txt.setText(String.valueOf(getAccountBalance(transactions)));
//        if (account.getPhoto() != null) {
//            account_photo.setImageBitmap(account.getPhoto());
//        } else {
//            account_photo.setImageResource(R.mipmap.ic_launcher);
//        }
//
//        account_card.setCardBackgroundColor(account.isDebit()?Color.parseColor("#B2DFDB"):Color.parseColor("#FFB2B2"));
//
//        return account_list_item;
//
//    }
//
//    public static double getAccountBalance(ArrayList<Transaction> transactions) {
//        double account_balance = 0;
//
//        for (int k = 0; k < transactions.size(); k++) {
//            if (transactions.get(k).isWithdraw()) {
//                account_balance += transactions.get(k).getBalance();
//            } else {
//                account_balance -= transactions.get(k).getBalance();
//            }
//        }
//        return account_balance;
//    }
//}
