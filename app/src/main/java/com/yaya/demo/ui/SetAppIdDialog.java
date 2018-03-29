package com.yaya.demo.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.yaya.demo.R;

/**
 * Created by ober on 2016/12/1.
 */
public class SetAppIdDialog extends Dialog {

    public interface Callback {
        void appIdReturned(Dialog d, String appId);
    }

    private Callback mCallback;

    AutoCompleteTextView tvAppId;

    public SetAppIdDialog(Context context, Callback callback) {
        super(context);
        mCallback = callback;
    }

    public void setDefaultAppId(String appId) {
        if(tvAppId != null) {
            tvAppId.setText(appId);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_set_appid);
        setTitle("设置AppId");
        tvAppId = (AutoCompleteTextView) findViewById(R.id.et_input);
        final Button btn = (Button) findViewById(R.id.btn_confirm);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appId = tvAppId.getText().toString();
                if(appId.equals("")) {
                    dismiss();
                } else {
                    if(mCallback != null) {
                        mCallback.appIdReturned(SetAppIdDialog.this, appId);
                    }
                    dismiss();
                }

            }
        });
    }
}
