package com.yemen.ums.ak.accounts_management.views;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yemen.ums.ak.accounts_management.R;
import com.yemen.ums.ak.accounts_management.models.Account;
import com.yemen.ums.ak.accounts_management.models.DBHelper;
import com.yemen.ums.ak.accounts_management.models.Transaction;
import com.yemen.ums.ak.accounts_management.viewModel.TransactionsAdapter;
import com.yemen.ums.ak.accounts_management.viewModel.ViewModelTransactions;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransactionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TransactionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransactionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionsFragment newInstance(String param1, String param2) {
        TransactionsFragment fragment = new TransactionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    RecyclerView transactions_rv;
    ArrayList<Transaction> transactionsList ;
    TransactionsAdapter transactionsAdapter;
    DBHelper dbHelper ;
    Context context ;
    FloatingActionButton newTransactionBtn;

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

    public static ViewModelTransactions viewModelTransactions;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transactions, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        context = getContext();
        transactions_rv = getView().findViewById(R.id.transactions_rv);
        newTransactionBtn = getView().findViewById(R.id.newTransaction_btn);

        loadTransaction();

        newTransactionBtn.setOnClickListener(view -> showTransactionAddDialog());


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void loadTransaction(){
        try {
            dbHelper = new DBHelper(context);
            transactionsList = (ArrayList<Transaction>) dbHelper.getTransactiions();
            transactionsAdapter = new TransactionsAdapter(context,transactionsList);
            transactions_rv.setLayoutManager(new LinearLayoutManager(context));
            transactions_rv.setAdapter(transactionsAdapter);

            viewModelTransactions = new ViewModelProvider(this).get(ViewModelTransactions.class);
            viewModelTransactions.getTransactions().observe(getViewLifecycleOwner(),transactions -> {
                transactionsAdapter.submitList(transactions);
            });
        }catch (Exception ex){
            throw new RuntimeException();
        }
    }

    private void showTransactionAddDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_transaction,null,false);
        builder.setView(view);
        alertDialog = builder.create();

        initialDialogForm(view);
        alertDialog.show();
    }

    private void initialDialogForm(View view){
        balance = view.findViewById(R.id.balance_txt);
        note = view.findViewById(R.id.note_txt);
        transactionSaveBtn = view.findViewById(R.id.transactionSave_btn);
        account_spnr = view.findViewById(R.id.account_spnr);
        transactionType_spnr = view.findViewById(R.id.transactionType_spnr);

        transactionType = Transaction.TRANSACTION_TYPE;
        transactionTypeAdapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_dropdown_item,transactionType);
        transactionTypeAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        transactionType_spnr.setAdapter(transactionTypeAdapter);

        accounts = dbHelper.getActiveAccounts();
        accountAdapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_dropdown_item,accounts);
        accountAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        account_spnr.setAdapter(accountAdapter);

        transactionSaveBtn.setOnClickListener(view1 -> saveTransaction());
    }

    private void saveTransaction(){

        Account account = accounts.get(account_spnr.getSelectedItemPosition());
        Double the_balance = Double.parseDouble(balance.getText().toString());
        Double balanceToChek = the_balance;
        String type = Transaction.TRANSACTION_TYPE.get(transactionType_spnr.getSelectedItemPosition());
        balanceToChek *= (type.equalsIgnoreCase(Transaction.TRANSACTION_TYPE.get(0)))?1:-1 ;
        if (!account.isAllow(context,balanceToChek)){
            Toast.makeText(context,getResources().getString(R.string.msg_notAllowOver)+String.valueOf(account.getAllow_max()),Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            newTransaction =new Transaction();
            newTransaction.setAccount(account.getId());
            newTransaction.setBalance(the_balance);
            newTransaction.setType(type);
            newTransaction.setNote(note.getText().toString());
            newTransaction.setCreated(System.currentTimeMillis());
            newTransaction.setUpdated(System.currentTimeMillis());

            dbHelper.insertTransaction(newTransaction);
            alertDialog.dismiss();
            viewModelTransactions.setTransactions(dbHelper.getTransactiions());
            AccountsFragment.viewModelAccounts.setAccounts(dbHelper.getAccounts());
            Toast.makeText(context,R.string.msg_save,Toast.LENGTH_SHORT).show();
        }catch (Exception ex){
            Toast.makeText(context,ex.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }
}