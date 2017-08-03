package com.example.administrator.dilixunbao.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.dilixunbao.MainActivity;
import com.example.administrator.dilixunbao.R;
import com.example.administrator.dilixunbao.User;
import com.example.administrator.dilixunbao.commons.ActivityUtils;
import com.example.administrator.dilixunbao.commons.RegexUtils;
import com.example.administrator.dilixunbao.custom.AlertDialogFragment;
import com.example.administrator.dilixunbao.map.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginActivity extends AppCompatActivity implements LoginView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_Username)
    EditText etUsername;
    @BindView(R.id.et_Password)
    EditText etPassword;
    @BindView(R.id.tv_forgetPassword)
    TextView tvForgetPassword;
    @BindView(R.id.btn_Login)
    Button btnLogin;
    private Unbinder unbinder;
    private ActivityUtils utils;
    private String mUserName;
    private String mPassWord;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);
        utils = new ActivityUtils(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.login);
        }
        etUsername.addTextChangedListener(mTextWatcher);
        etPassword.addTextChangedListener(mTextWatcher);

    }

    public TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            mUserName = etUsername.getText().toString();
            mPassWord = etPassword.getText().toString();
            boolean canLogin = TextUtils.isEmpty(mUserName) || TextUtils.isEmpty(mPassWord);
            btnLogin.setEnabled(!canLogin);
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.tv_forgetPassword, R.id.btn_Login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_forgetPassword:
                utils.showToast("忘记密码");
                break;
            case R.id.btn_Login:
                if (RegexUtils.verifyUsername(mUserName) != RegexUtils.VERIFY_SUCCESS) {
                    AlertDialogFragment.getInstance(getString(R.string.username_error), getString(R.string.username_rules))
                            .show(getSupportFragmentManager(), "username_error");
                    return;
                }
                if (RegexUtils.verifyPassword(mPassWord) != RegexUtils.VERIFY_SUCCESS) {
                    AlertDialogFragment.getInstance(getString(R.string.password_error), getString(R.string.password_rules))
                            .show(getSupportFragmentManager(), "password_error");
                    return;
                }
                new LoginPresenter(this).login(new User(mUserName,mPassWord));
        }
    }
//------------------------------实现自视图接口的方法--------------------------

    @Override
    public void showProgress() {
        mProgressDialog = ProgressDialog.show(LoginActivity.this, "登录", "正在登陆中，请稍候......");
    }

    @Override
    public void hideProgress() {
        mProgressDialog.dismiss();
    }

    @Override
    public void showMessage(String message) {
        utils.showToast(message);
    }

    @Override
    public void navigateToHomeActivity() {
        utils.startActivity(HomeActivity.class);
        finish();
        LocalBroadcastManager.getInstance(LoginActivity.this).sendBroadcast(new Intent(MainActivity.ACTION_MAIN));
    }
}
