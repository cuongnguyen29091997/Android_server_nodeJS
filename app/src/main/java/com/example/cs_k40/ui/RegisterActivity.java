package com.example.cs_k40.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.cs_k40.R;
import com.example.cs_k40.model.User;
import com.example.cs_k40.service.UserClient;

import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtFullname;
    private EditText edtAddress;
    private  EditText edtEmail;
    private EditText edtPassword;
    private  EditText edtRepass;
    private Button btnRegister;
    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("http://192.168.0.115:3000/")
            .addConverterFactory(GsonConverterFactory.create());

    Retrofit retrofit = builder.build();
    UserClient userClient = retrofit.create(UserClient.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtFullname = findViewById(R.id.regisInputFullname);
        edtEmail = findViewById(R.id.regisInputEmail);
        edtPassword = findViewById(R.id.regisInputPassword);
        edtRepass = findViewById(R.id.regisInputCheckPassword);
        edtAddress = findViewById(R.id.regisInputAddress);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }


    private void register() {
        btnRegister.setEnabled(false);
        String email = String.valueOf(edtEmail.getText());
        String password = String.valueOf(edtPassword.getText());
        String repass = String.valueOf(edtRepass.getText());
        String fullname = String.valueOf(edtFullname.getText());
        String address = String.valueOf(edtAddress.getText());
        // validation;
        if(email.length() < 6 || password.length() < 6 || email.length() > 30 || password.length() > 30
        ) {
            Toast.makeText(RegisterActivity.this, "Email và password phải từ 6 đến 30 ký tự" , Toast.LENGTH_SHORT).show();
            btnRegister.setEnabled(true);
            return;
        } else if(!email.matches(getResources().getString(R.string.regex_email))) {
            Toast.makeText(RegisterActivity.this, "Email không đúng định dạng." , Toast.LENGTH_SHORT).show();
            btnRegister.setEnabled(true);
            return;
        } else if(!password.equals(repass)) {
            Toast.makeText(RegisterActivity.this, "Mật khẩu không khớp với nhau !." , Toast.LENGTH_SHORT).show();
            btnRegister.setEnabled(true);
            return;
        }

        // pass register;
        User register = new User(email,fullname, address, password);
        Call<User> call = userClient.register(register);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    String token = response.body().getToken();
                    SharedPreferences settings = PreferenceManager
                            .getDefaultSharedPreferences(getBaseContext());
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("token", token);
                    editor.commit();
                    Intent intent = new Intent(getApplicationContext(), UserInforActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, "Đã có lỗi xảy ra, vui lòng thử lại" , Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
