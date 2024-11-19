package com.yemen.ums.ak.accounts_management.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.yemen.ums.ak.accounts_management.models.Account;

import java.util.ArrayList;
import java.util.List;

public class ViewModelAccounts extends ViewModel {

    private MutableLiveData<List<Account>> accounts = new MutableLiveData<>();

    public LiveData<List<Account>> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts_) {
        accounts.setValue(accounts_);
    }

}
