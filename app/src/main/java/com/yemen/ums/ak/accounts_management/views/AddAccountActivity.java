package com.yemen.ums.ak.accounts_management.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.yemen.ums.ak.accounts_management.R;
import com.yemen.ums.ak.accounts_management.models.Account;
import com.yemen.ums.ak.accounts_management.models.DBHelper;

import java.util.List;

public class AddAccountActivity extends AppCompatActivity {
    EditText name ,mobile, allowmax;
    CheckBox active ;
    Button accountSaveBtn;
    ImageView accountImg;
    DBHelper dbHelper ;
    Account newAccount;
    Spinner accountType_spnr ;
    List<String> accountType ;
    ArrayAdapter<String> arrayAdapter;
    private Uri imagePath;
    private Bitmap imageBitmap;
    private final int PICK_IMAGE_REQUEST = 100;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data !=null){
                imagePath = data.getData();
                imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);

                accountImg.setImageBitmap(imageBitmap);
            }
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        name = findViewById(R.id.accountName_txt);
        mobile = findViewById(R.id.accountMobile_txt);
        allowmax = findViewById(R.id.accountAllowmax_txt);
        accountType_spnr = findViewById(R.id.accountType_spnr);
        active = findViewById(R.id.accountActive_chkbx);
        accountImg = findViewById(R.id.account_img);
        accountSaveBtn = findViewById(R.id.accountSave_btn);

        accountType = Account.ACCOUNT_TYPE;
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,accountType);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        accountType_spnr.setAdapter(arrayAdapter);

        accountImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent();
               intent.setType("image/*");
               intent.setAction(Intent.ACTION_GET_CONTENT);
               startActivityForResult(intent,PICK_IMAGE_REQUEST);
            }
        });




        accountSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper = new DBHelper(getApplicationContext());
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
                    Toast.makeText(getApplicationContext(),"Successfylly",Toast.LENGTH_SHORT).show();
                }catch (Exception ex){
                    Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}