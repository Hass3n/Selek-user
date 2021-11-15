package minya.salek.salekapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PrivacyActivity extends AppCompatActivity {

    Button acceptPrivacy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privcy);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        acceptPrivacy = findViewById(R.id.accept_privacy_btn);

        SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        alertDialog.setContentText("يجب الموافقة على هذة الشروط لكي تصبح سالكا");
        alertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setConfirmText("حسنا");
        alertDialog.setConfirmClickListener(Dialog::dismiss);
        alertDialog.show();

        acceptPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showCustomDialog();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog dialog = new Dialog(PrivacyActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.accept_privacy_dialoge);
                        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                        startActivity(new Intent(PrivacyActivity.this, HomeActivity.class));
                        dialog.dismiss();
                        finish();
                    }
                },2000);
            }
        });
    }

    void showCustomDialog() {
        final Dialog dialog = new Dialog(PrivacyActivity.this);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.accept_privacy_dialoge);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }



}
