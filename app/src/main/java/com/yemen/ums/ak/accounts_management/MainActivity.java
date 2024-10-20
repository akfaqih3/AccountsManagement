package com.yemen.ums.ak.accounts_management;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.yemen.ums.ak.accounts_management.models.Account;
import com.yemen.ums.ak.accounts_management.models.DBHelper;
import com.yemen.ums.ak.accounts_management.helpers.MySharedPreferences;
import com.yemen.ums.ak.accounts_management.models.Transaction;
import com.yemen.ums.ak.accounts_management.views.AccountsFragment;
import com.yemen.ums.ak.accounts_management.views.FragmentAdapter;
import com.yemen.ums.ak.accounts_management.views.LoginActivity;
import com.yemen.ums.ak.accounts_management.views.TransactionsFragment;

import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    FragmentAdapter fragmentAdapter ;
    TabLayout tabLayout ;
    ViewPager viewPager;
    Toolbar toolbar;
    Context context;
    MenuItem searchItem;

    List<Account> accountList;
    List<Transaction> transactionList;
    private android.widget.SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        context = getApplicationContext();
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        setSupportActionBar(toolbar);

        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());

        fragmentAdapter.addFragment(new TransactionsFragment(),getResources().getString(R.string.transactions));
        fragmentAdapter.addFragment(new AccountsFragment(),getResources().getString(R.string.accounts));
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        searchItem = menu.findItem(R.id.app_bar_search);
        searchView = (android.widget.SearchView) searchItem.getActionView();

        DBHelper dbHelper = new DBHelper(context);
        accountList = dbHelper.getAccounts();
        transactionList = dbHelper.getTransactiions();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchAccount(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getTitle().toString()){
            case "logout":
                logout();
                break;

            case "search":

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

    }

    private void logout(){
        MySharedPreferences.logOut(context);
        startActivity(new Intent(context,LoginActivity.class));
        finish();
    }

    private void searchAccount(String textSearch){
        List<Account> newAccountsList ;
        List<Transaction> newTransactionList;
        if (textSearch.trim().isEmpty()){
            AccountsFragment.viewModelAccounts.setAccounts(accountList);
            TransactionsFragment.viewModelTransactions.setTransactions(transactionList);
            return;
        }

        if (fragmentAdapter.getPageTitle(viewPager.getCurrentItem()).toString() == getResources().getString(R.string.accounts)){
            newAccountsList = accountList.stream()
                    .filter(account -> account.getName().contains(textSearch)).collect(Collectors.toList());
            AccountsFragment.viewModelAccounts.setAccounts(newAccountsList);
        }
        else if (fragmentAdapter.getPageTitle(viewPager.getCurrentItem()).toString() == getResources().getString(R.string.transactions)) {
            newTransactionList = transactionList.stream()
                    .filter((transaction -> transaction.getAccountName().contains(textSearch))).collect(Collectors.toList());
            TransactionsFragment.viewModelTransactions.setTransactions(newTransactionList);
        }else {
            AccountsFragment.viewModelAccounts.setAccounts(accountList);
            TransactionsFragment.viewModelTransactions.setTransactions(transactionList);
        }

    }
}