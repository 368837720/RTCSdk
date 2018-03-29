package com.yaya.demo.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.yaya.demo.R;

/**
 * Created by ober on 2016/12/1.
 */
public class SetTTDialog extends Dialog {

    public interface Callback {
        void ttReturned(Dialog d, String uid, String nickname);
    }

    private Callback mCallback;
    private AutoCompleteTextView tvUid;
    private AutoCompleteTextView tvNickname;

    public SetTTDialog(Context context, Callback callback) {
        super(context);
        mCallback = callback;
    }

    public void setDefaultTT(String uid, String nickname) {
        if(tvUid != null && tvNickname != null) {
            tvUid.setText(uid);
            tvNickname.setText(nickname);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_set_tt);
        setTitle("绑定三方帐号");
        tvUid = (AutoCompleteTextView) findViewById(R.id.et_input_uid);
        tvNickname = (AutoCompleteTextView) findViewById(R.id.et_input_nickname);
        Button btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uuid = tvUid.getText().toString();
                String nickname = tvNickname.getText().toString();
                if(uuid.equals("")) {
                    dismiss();
                } else {
                    mCallback.ttReturned(SetTTDialog.this, uuid, nickname);
                    dismiss();
                }
            }
        });
    }
}
