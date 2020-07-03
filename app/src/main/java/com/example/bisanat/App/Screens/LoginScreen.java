package com.example.bisanat.App.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bisanat.App.Utility;
import com.example.bisanat.DAL.Entites.Person;
import com.example.bisanat.DAL.Network.HttpServices;
import com.example.bisanat.R;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginScreen extends AppCompatActivity {

    EditText etUserName, etPassword;
    CheckBox chbKeepLogged;
    LovelyProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_login);

        etUserName = findViewById(R.id.et_login_email);
        etPassword = findViewById(R.id.et_login_password);
        chbKeepLogged = findViewById(R.id.chb_login_keep_logged);

    }


    public void OnClickScreenLogin(View v) {
        switch (v.getId()) {
            case R.id.btn_login_login:
                Login();
                break;
            /*case R.id.btn_login_signup:
                startActivity(new Intent(LoginScreen.this, SignupScreen.class));
                break;*/
        }
    }

    private void Login() {
        progressDialog = new LovelyProgressDialog(this)
                .setIcon(R.drawable.ic_cloud_light)
                .setMessage("Giriş Yapılıyor...")
                .setTopColorRes(R.color.colorPrimaryDark);
        progressDialog.show();
        HttpServices._currentUser = new Person();
        HttpServices._currentUser.Email = etUserName.getText().toString().trim();
        HttpServices._currentUser.Password = etUserName.getText().toString().trim();
        Call<List<Person>> login = HttpServices.personService.GetFilter(HttpServices._currentUser);
        login.enqueue(new Callback<List<Person>>() {
            @Override
            public void onResponse(Call<List<Person>> call, Response<List<Person>> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null || response.body().size() != 1) {
                        Utility.ShowToast(LoginScreen.this,"Kullanıcı Bilgileriniz Hatalı!");
                    } else {
                        if (chbKeepLogged.isChecked()) {
                            HttpServices._currentUser = response.body().get(0);
                            HttpServices._currentUser.DeviceToken = Utility.GetDeviceToken(LoginScreen.this);
                            Call<Person> update = HttpServices.personService.Update(HttpServices._currentUser);
                            update.enqueue(new Callback<Person>() {
                                @Override
                                public void onResponse(Call<Person> call, Response<Person> response) {
                                    if (response.isSuccessful()) {
                                        HttpServices._currentUser = response.body();
                                        if (HttpServices._currentUser == null || HttpServices._currentUser.Id <= 0)
                                            return;
                                        Utility.SaveUser(LoginScreen.this, HttpServices._currentUser);
                                        Utility.ShowToast(LoginScreen.this,"Başarıyla Giriş Yapıldı!");
                                        startActivity(new Intent(LoginScreen.this, MainScreen.class));
                                    } else {
                                        Utility.ShowToast(LoginScreen.this,"Beklenmedik bir hata oluştu! Lütfen tekrar deneyin!");
                                    }
                                }

                                @Override
                                public void onFailure(Call<Person> call, Throwable t) {
                                    Utility.ShowToast(LoginScreen.this,t.getMessage());
                                }
                            });
                        } else {
                            HttpServices._currentUser = response.body().get(0);
                            Utility.ShowToast(LoginScreen.this,"Başarıyla Giriş Yapıldı!");
                            startActivity(new Intent(LoginScreen.this, MainScreen.class));
                        }
                    }
                }else{
                    if(response.code() == 404) Utility.ShowToast(LoginScreen.this,"Upps! Böyle bir kullanıcı yok! Lütfen bilgilerinizi kontrol edin!");
                    else  Utility.ShowToast(LoginScreen.this,"Sunucuya bağlanılamadı! Lütfen tekrar deneyiniz!");
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Person>> call, Throwable t) {
                Utility.ShowToast(LoginScreen.this,t.getMessage());
                progressDialog.dismiss();
            }
        });
    }
    
}
