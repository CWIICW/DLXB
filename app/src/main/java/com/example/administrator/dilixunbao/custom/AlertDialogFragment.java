package com.example.administrator.dilixunbao.custom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.example.administrator.dilixunbao.R;

/**
 * Created by Administrator on 2017/7/31.
 */

public class AlertDialogFragment extends DialogFragment {
    private static final String KEY_TITLE = "key_title";
    private static final String KEY_MESSAGE = "key_message";

    public static AlertDialogFragment getInstance(String title, String message) {
        AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
        //将参数传递给onCreateDialog（）方法
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        bundle.putString(KEY_MESSAGE, message);
        alertDialogFragment.setArguments(bundle);
        return alertDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title = getArguments().getString(KEY_TITLE);
        String message = getArguments().getString(KEY_MESSAGE);

        return new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setTitle(title)
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }
}
