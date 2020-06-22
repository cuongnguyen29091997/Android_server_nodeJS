package com.example.cs_k40.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.cs_k40.R;
import com.example.cs_k40.model.User;
import com.example.cs_k40.service.UserClient;

public class UserInforActivity extends AppCompatActivity {
    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("http://192.168.0.115:3000/")
            .addConverterFactory(GsonConverterFactory.create());
    Retrofit retrofit = builder.build();
    UserClient userClient = retrofit.create(UserClient.class);
    private String token;
    private TextView email;
    private TextView fullname;
    private TextView address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_infor);
        email =  findViewById(R.id.email);
        fullname =  findViewById(R.id.fullname);
        address =  findViewById(R.id.address);

        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        token = settings.getString("token","");
        getUserInfo();
        findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                logout();
            }
        });
    }
    private void logout() {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.remove("token");
        editor.commit();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
    private void getUserInfo() {
        Call<User> call = userClient.getUserInfo(token);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    email.setText(response.body().getEmail());
                    address.setText(response.body().getAddress().equals("") ?
                                    "Bạn chưa có địa chỉ" :
                                    response.body().getAddress());
                    fullname.setText(response.body().getFullname().equals("") ?
                            "Bạn chưa có tên đầy đủ" :
                            response.body().getFullname());
                } else {
                    Toast.makeText(UserInforActivity.this, "Token không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(UserInforActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
