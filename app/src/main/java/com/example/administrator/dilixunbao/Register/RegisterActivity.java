package com.example.administrator.dilixunbao.register;

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
import android.widget.Button;
import android.widget.EditText;

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

public class RegisterActivity extends AppCompatActivity implements RegisterView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_Username)
    EditText etUsername;
    @BindView(R.id.et_Password)
    EditText etPassword;
    @BindView(R.id.et_Confirm)
    EditText etConfirm;
    @BindView(R.id.btn_Register)
    Button btnRegister;
    private String mUserName;
    private String mPassWord;
    private String mConfirm;
    private ActivityUtils utils;
    private Unbinder unbinder;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        unbinder = ButterKnife.bind(this);
        utils = new ActivityUtils(this);
        //设置Toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            //显示返回箭头
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //设置标题
            getSupportActionBar().setTitle(R.string.register);
        }
        etUsername.addTextChangedListener(mTextWatcher);
        etPassword.addTextChangedListener(mTextWatcher);
        etConfirm.addTextChangedListener(mTextWatcher);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        //改变之前
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        //变化中
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        //改变之后
        @Override
        public void afterTextChanged(Editable editable) {
            mUserName = etUsername.getText().toString();
            mPassWord = etPassword.getText().toString();
            mConfirm = etConfirm.getText().toString();
            boolean canRegister = TextUtils.isEmpty(mUserName) || TextUtils.isEmpty(mPassWord) || TextUtils.isEmpty(mConfirm);
            btnRegister.setEnabled(!canRegister);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //点击注册
    @OnClick(R.id.btn_Register)
    public void onViewClicked() {
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
        if (!mPassWord.equals(mConfirm)) {
            AlertDialogFragment.getInstance(getString(R.string.password_error), "密码不一致")
                    .show(getSupportFragmentManager(), "password_error");
            return;
        }
        new RegisterPresenter(this).register(new User(mUserName, mPassWord));
        etUsername.setText("");
        etPassword.setText("");
        etConfirm.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    //---------------------------------------------------实现自视图接口的方法---------------------------------------------------------
    @Override
    public void showProgress() {
        mProgressDialog = ProgressDialog.show(RegisterActivity.this, "注册", "正在注册中，请稍候......");
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
        LocalBroadcastManager.getInstance(RegisterActivity.this).sendBroadcast(new Intent(MainActivity.ACTION_MAIN));
    }
}
