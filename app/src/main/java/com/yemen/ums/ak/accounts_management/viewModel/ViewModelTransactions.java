package com.yemen.ums.ak.accounts_management.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.yemen.ums.ak.accounts_management.models.Transaction;

import java.util.List;

public class ViewModelTransactions extends ViewModel {

    private MutableLiveData<List<Transaction>> transactions = new MutableLiveData<>();
    private MutableLiveData<List<Transaction>> accountTransactions = new MutableLiveData<>();

    public LiveData<List<Transaction>> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions_) {
       transactions.setValue(transactions_);
    }

}
