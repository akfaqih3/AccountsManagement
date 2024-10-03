package com.yemen.ums.ak.accounts_management.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.TokenWatcher;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yemen.ums.ak.accounts_management.R;
import com.yemen.ums.ak.accounts_management.models.Account;
import com.yemen.ums.ak.accounts_management.models.DBHelper;
import com.yemen.ums.ak.accounts_management.viewModel.AccountsAdapter;

import java.util.ArrayList;
import java.util.List;

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

    EditText name ,mobile, allowmax;
    CheckBox active ;
    Button accountSaveBtn;
    ImageView accountImg;
    Account newAccount;
    Spinner accountType_spnr ;
    List<String> accountType ;
    ArrayAdapter<String> arrayAdapter;
    final int PICK_IMAGE_REQUEST = 100;
    Uri imagePath;
    Bitmap imageBitmap;
    AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
        newAccountBtn.setOnClickListener(view -> showAccountAddDialog());
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

    private void showAccountAddDialog(){
        AlertDialog.Builder builder = new  AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_account,null,false);
        builder.setView(view);
        alertDialog = builder.create();

        initiateDialogForm(view);

        alertDialog.show();

    }

    private void initiateDialogForm(View view){

        name = view.findViewById(R.id.accountName_txt);
        mobile = view.findViewById(R.id.accountMobile_txt);
        allowmax = view.findViewById(R.id.accountAllowmax_txt);
        accountType_spnr = view.findViewById(R.id.accountType_spnr);
        active = view.findViewById(R.id.accountActive_chkbx);
        accountImg = view.findViewById(R.id.account_img);
        accountSaveBtn = view.findViewById(R.id.accountSave_btn);

        accountType = Account.ACCOUNT_TYPE;
        arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item,accountType);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountType_spnr.setAdapter(arrayAdapter);

        accountImg.setOnClickListener(view1 -> showDialogImages());
        accountSaveBtn.setOnClickListener(view1 -> saveAccount());
    }

    private void showDialogImages(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    private void saveAccount(){
        try {
            newAccount = new Account();
            newAccount.setName(name.getText().toString());
            newAccount.setMobile(mobile.getText().toString());
            newAccount.setAllow_max(Double.parseDouble(allowmax.getText().toString()));
            newAccount.setType(accountType.get(accountType_spnr.getSelectedItemPosition()).toString());
            newAccount.setActive(active.isChecked());
            newAccount.setPhoto((imageBitmap!=null)?imageBitmap:null);
            newAccount.setCreated(System.currentTimeMillis());
            newAccount.setUpdated(System.currentTimeMillis());

            dbHelper.insertAccount(newAccount);
            alertDialog.dismiss();
            Toast.makeText(context,"Successfylly",Toast.LENGTH_SHORT).show();
        }catch (Exception ex){
            Toast.makeText(context,ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data !=null){
                imagePath = data.getData();
                imageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),imagePath);
                accountImg.setImageBitmap(imageBitmap);
            }
        }catch (Exception ex){
            Toast.makeText(context,ex.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }
}