package com.yemen.ums.ak.accounts_management.views;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AlertDialogLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.TokenWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yemen.ums.ak.accounts_management.R;
import com.yemen.ums.ak.accounts_management.models.Account;
import com.yemen.ums.ak.accounts_management.models.DBHelper;
import com.yemen.ums.ak.accounts_management.viewModel.AccountsAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountsFragment newInstance(String param1, String param2) {
        AccountsFragment fragment = new AccountsFragment();
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


    RecyclerView accounts_rv;
    ArrayList<Account> accounts ;
    AccountsAdapter accountsAdapter;
    DBHelper dbHelper ;
    Context context ;
    FloatingActionButton newAccountBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accounts, container, false);

    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onStart() {
        super.onStart();
        context = getContext();
        accounts_rv = getView().findViewById(R.id.accounts_rv);

        loadAccounts();
        newAccountBtn = getView().findViewById(R.id.newAccount_btn);
       if (newAccountBtn !=null){
           newAccountBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   startActivity(new Intent(context,AddAccountActivity.class));
               }
           });
       }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void loadAccounts(){
//
        try {
            dbHelper = new DBHelper(context);
            accounts = (ArrayList<Account>) dbHelper.getAccounts();
            accountsAdapter = new AccountsAdapter(context,accounts);
            LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(context);
            accounts_rv.setLayoutManager(LinearLayoutManager);
            accounts_rv.setAdapter(accountsAdapter);

        }catch (Exception ex){
            throw new RuntimeException();
        }
    }


}