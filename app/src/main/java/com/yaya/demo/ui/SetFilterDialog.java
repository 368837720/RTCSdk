package com.yaya.demo.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.yaya.demo.R;
import com.yaya.sdk.MessageFilter;
import com.yaya.sdk.YayaRTV;

/**
 * Created by ober on 2016/11/24.
 */
public class SetFilterDialog extends Dialog {

    public SetFilterDialog(Context context) {
        super(context);
    }

    private AutoCompleteTextView tvFilter;
    private Button btnConfirm;

    private long lastFilteredId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_set_filter);
        setTitle("FilterSetting");
        tvFilter = (AutoCompleteTextView) findViewById(R.id.et_input);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String yunvaId = tvFilter.getText().toString();
                if(TextUtils.isEmpty(yunvaId)) {
                    boolean result = YayaRTV.getInstance().setMessageFilter(null);
                    if(!result) {
                        Toast.makeText(getContext(), "设置失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    final long toFilterId = Long.parseLong(yunvaId);
                    lastFilteredId = toFilterId;
                    boolean result = YayaRTV.getInstance().setMessageFilter(new MessageFilter() {
                        @Override
                        public boolean filterVoiceMsg(Long voiceYunvaId, String expand) {
                            return voiceYunvaId != null && voiceYunvaId == toFilterId;
                        }

                        @Override
                        public boolean filterTextMsg(Long messageYunvaId, String text, String expand) {
                            return messageYunvaId != null && messageYunvaId == toFilterId;
                        }
                    });
                    if(!result) {
                        Toast.makeText(getContext(), "设置失败", Toast.LENGTH_SHORT).show();
                    }
                }
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        super.show();
        if(lastFilteredId != 0) {
            tvFilter.setText(String.valueOf(lastFilteredId));
        }
    }
}
