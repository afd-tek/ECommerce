package com.example.bisanat.App.Screens;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bisanat.App.Utility;
import com.example.bisanat.DAL.Entites.Person;
import com.example.bisanat.DAL.Network.HttpServices;
import com.example.bisanat.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {

    TextView tvLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_splash);

        tvLogo = findViewById(R.id.tv_splash_logo);

        Animation logoAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_logo_text);
        logoAnimation.reset();
        tvLogo.clearAnimation();
        tvLogo.startAnimation(logoAnimation);
        Call<List<Person>> personList = HttpServices.personService.GetAll();
        personList.enqueue(new Callback<List<Person>>() {
            @Override
            public void onResponse(Call<List<Person>> call, Response<List<Person>> response) {
                if(response.isSuccessful()){
                    if(response.body() != null && response.body().size() > 0){
                        Utility._people = response.body();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Person>> call, Throwable t) {

            }
        });
        logoAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Person login = Utility.GetSavedUser(SplashScreen.this);
                if(login == null){
                    startActivity(new Intent(SplashScreen.this,LoginScreen.class));
                }else{
                    Call<List<Person>> personCall = HttpServices.personService.GetFilter(login);
                    personCall.enqueue(new Callback<List<Person>>() {
                        @Override
                        public void onResponse(Call<List<Person>> call, Response<List<Person>> response) {
                            if (response.isSuccessful()) {
                                if (response.body() == null || response.body().size() != 1) {
                                    Utility.ShowToast(SplashScreen.this,"Kullanıcı Bilgileriniz Hatalı!");
                                } else {
                                    HttpServices._currentUser = response.body().get(0);
                                    if (HttpServices._currentUser == null || HttpServices._currentUser.Id <= 0)
                                        return;
                                    Utility.SaveUser(SplashScreen.this, HttpServices._currentUser);
                                    Utility.ShowToast(SplashScreen.this,"Başarıyla Giriş Yapıldı!");
                                    startActivity(new Intent(SplashScreen.this, MainScreen.class));

                                }
                            }else{
                                Utility.ShowToast(SplashScreen.this,"Kullanıcı bilgileriniz değişmiş gibi gözüküyor. Lütfen tekrar giriş yapın!");
                                Utility.ClearSavedUser(SplashScreen.this);
                                startActivity(new Intent(SplashScreen.this,LoginScreen.class));
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Person>> call, Throwable t) {
                                Utility.ShowToast(SplashScreen.this,"Sunucuya bağlanılamadı! Lütfen tekrar deneyiniz!");
                        }
                    });
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
