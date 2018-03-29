package com.yaya.demo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.yaya.demo.ui.SetAppIdDialog;
import com.yaya.demo.ui.SetTTDialog;
import com.yaya.sdk.YayaRTV;

import yaya.tlv.util.StringUtils;

/**
 * 该Activity只传递数据，调用sdk都在MainActivity
 *
 * Created by ober on 2016/11/13.
 */
public class EntryActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvEnvTest;
    private TextView tvEnvPro;
    private TextView tvEnvOver;

    private TextView tvModeFree;
    private TextView tvModeRob;

    private AutoCompleteTextView tvRoomSeq;

    private Button btnConfirm;

    private TextView tvAppId;
    private TextView tvSetAppId;
    private TextView tvThird;
    private TextView tvSetThird;
    private TextView tvClearThird;

    private int env;
    private int mode;

    private String mAppId = "100041";
    private String mThirdUid = null;
    private String mThirdNickname = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        initViews();

        setUpEnv(0);
        setUpMode(0);
    }

    private void initViews() {
        tvEnvTest = (TextView) findViewById(R.id.tv_en_test);
        tvEnvPro = (TextView) findViewById(R.id.tv_en_pro);
        tvEnvOver = (TextView) findViewById(R.id.tv_en_over);
        tvModeFree = (TextView) findViewById(R.id.tv_mode_free);
        tvModeRob = (TextView) findViewById(R.id.tv_mode_rob);
        tvRoomSeq = (AutoCompleteTextView) findViewById(R.id.room_seq);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);

        tvEnvTest.setOnClickListener(this);
        tvEnvPro.setOnClickListener(this);
        tvEnvOver.setOnClickListener(this);
        tvModeRob.setOnClickListener(this);
        tvModeFree.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        tvAppId = (TextView) findViewById(R.id.tv_appId);
        tvSetAppId = (TextView) findViewById(R.id.tv_set_appId);
        tvSetAppId.setOnClickListener(this);
        tvThird = (TextView) findViewById(R.id.tv_third);
        tvSetThird = (TextView) findViewById(R.id.tv_set_third);
        tvSetThird.setOnClickListener(this);
        tvClearThird = (TextView) findViewById(R.id.tv_clear_third);
        tvClearThird.setOnClickListener(this);
    }

    private void setUpEnv(int env) {
        this.env = env;
        final int bgSelected = R.drawable.shape_big_btn_select;
        final int bgNormal = R.drawable.shape_big_btn;
        tvEnvTest.setBackgroundResource(env == 0 ? bgSelected : bgNormal);
        tvEnvPro.setBackgroundResource(env == 1 ? bgSelected : bgNormal);
        tvEnvOver.setBackgroundResource(env == 2 ? bgSelected : bgNormal);
    }

    private void setUpMode(int mode) {
        this.mode = mode;
        final int bgSelected = R.drawable.shape_big_btn_select;
        final int bgNormal = R.drawable.shape_big_btn;
        tvModeFree.setBackgroundResource(mode == 0 ? bgSelected :bgNormal);
        tvModeRob.setBackgroundResource(mode == 1 ? bgSelected : bgNormal);
    }

    private void jumpNextPage() {
        String roomSeq = tvRoomSeq.getText().toString();
        if(TextUtils.isEmpty(roomSeq)) {
            tvRoomSeq.setError("房间号不能为空");
            tvRoomSeq.requestFocus();
            return;
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("env", env);
        intent.putExtra("mode", mode);
        intent.putExtra("seq", roomSeq);
        intent.putExtra("appId", mAppId);
        if(mThirdUid != null) {
            intent.putExtra("tt", getTT());
        }
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if(id == R.id.tv_en_test) {
            setUpEnv(0);
        } else if(id == R.id.tv_en_pro) {
            setUpEnv(1);
        } else if(id == R.id.tv_en_over) {
            setUpEnv(2);
        } else if(id == R.id.tv_mode_free) {
            setUpMode(0);
        } else if(id == R.id.tv_mode_rob) {
            setUpMode(1);
        } else if(id == R.id.btn_confirm) {
            jumpNextPage();
        } else if(id == R.id.tv_set_appId) {
            setAppId();
        } else if(id == R.id.tv_set_third) {
            setTT();
        } else if(id == R.id.tv_clear_third) {
            clearTT();
        }
    }

    private SetAppIdDialog mSetAppIdDialog;
    private void setAppId() {
        if(mSetAppIdDialog == null) {
            mSetAppIdDialog = new SetAppIdDialog(this, new SetAppIdDialog.Callback() {
                @Override
                public void appIdReturned(Dialog d, String appId) {
                    mAppId = appId;
                    tvAppId.setText("appId = " + mAppId);
                }
            });
        }
        mSetAppIdDialog.show();
        mSetAppIdDialog.setDefaultAppId(mAppId);
    }

    private SetTTDialog mSetTTDialog;
    private void setTT() {
        if(mSetTTDialog == null) {
            mSetTTDialog = new SetTTDialog(this, new SetTTDialog.Callback() {
                @Override
                public void ttReturned(Dialog d, String uid, String nickname) {
                    mThirdUid = uid;
                    mThirdNickname = nickname;
                    tvThird.setText("三方绑定帐号tt = " + getTT());
                }
            });
        }
        mSetTTDialog.show();
        if(mThirdUid != null) {
            mSetTTDialog.setDefaultTT(mThirdUid, mThirdNickname);
        }
    }

    private void clearTT() {
        mThirdUid = null;
        mThirdNickname = null;
        tvThird.setText("三方绑定帐号tt = null");
    }

    private String getTT() {
        String tt = "{\"uid\":\"{uid}\",\"nickname\":\"{nickname}\"}";
        return tt.replace("{uid}", mThirdUid).replace("{nickname}", mThirdNickname);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mSetAppIdDialog != null && mSetAppIdDialog.isShowing()) {
            mSetAppIdDialog.dismiss();
        }
        if(mSetTTDialog != null && mSetTTDialog.isShowing()) {
            mSetTTDialog.dismiss();
        }
    }
}
