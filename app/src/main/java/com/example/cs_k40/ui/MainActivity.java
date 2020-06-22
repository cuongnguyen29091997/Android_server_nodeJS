package com.example.cs_k40.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cs_k40.R;
import com.example.cs_k40.model.User;
import com.example.cs_k40.model.Login;
import com.example.cs_k40.service.UserClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("http://192.168.0.115:3000/")
            .addConverterFactory(GsonConverterFactory.create());

    Retrofit retrofit = builder.build();
    UserClient userClient = retrofit.create(UserClient.class);

    private EditText editTextEmail;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editTextEmail = findViewById(R.id.inputEmail);
        editTextPassword = findViewById(R.id.inputPassword);

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login();
            }
        });
        findViewById(R.id.gotoRegister).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gotoRegister();
            }
        });
    }

    private void login() {
        String email = String.valueOf(editTextEmail.getText());
        String password = String.valueOf(editTextPassword.getText());

       // validation;
        if(email.length() < 6 || password.length() < 6 || email.length() > 30 || password.length() > 30) {
            Toast.makeText(MainActivity.this, "Email, mật khẩu nằm trong khoảng 6 đến 30 ký tự." , Toast.LENGTH_SHORT).show();
            return;
        } else if(!email.matches(getResources().getString(R.string.regex_email))) {
            Toast.makeText(MainActivity.this, "Email không đúng định dạng." , Toast.LENGTH_SHORT).show();
            return;
        }

        // pass login
        Login login = new Login(email, password);
        Call<User> call = userClient.login(login);

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
                    // Còn cần phải xử lý lỗi bad request, và tạo 1 phương thức để lấy lỗi
                    Toast.makeText(MainActivity.this, "Email không khớp với mật khẩu." , Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void gotoRegister() {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }
}
