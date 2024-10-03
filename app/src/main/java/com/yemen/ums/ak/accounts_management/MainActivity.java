package com.yemen.ums.ak.accounts_management;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.yemen.ums.ak.accounts_management.models.MySharedPreferences;
import com.yemen.ums.ak.accounts_management.views.AccountsFragment;
import com.yemen.ums.ak.accounts_management.views.FragmentAdapter;
import com.yemen.ums.ak.accounts_management.views.LoginActivity;
import com.yemen.ums.ak.accounts_management.views.TransactionsFragment;

public class MainActivity extends AppCompatActivity {
    FragmentAdapter fragmentAdapter ;
    TabLayout tabLayout ;
    ViewPager viewPager;
    Toolbar toolbar;
    Context context;

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

        fragmentAdapter.addFragment(new AccountsFragment(),"Accounts");
        fragmentAdapter.addFragment(new TransactionsFragment(),"Transactions");
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getTitle().toString()){
            case "logout":
                logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout(){
        MySharedPreferences.logOut(context);
        startActivity(new Intent(context,LoginActivity.class));
        finish();
    }

}