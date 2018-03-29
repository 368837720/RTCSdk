package com.yaya.demo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yaya.demo.ui.DemoChatBox;
import com.yaya.demo.ui.SetFilterDialog;
import com.yaya.sdk.RTV;
import com.yaya.sdk.YayaRTV;
import com.yaya.sdk.VideoTroopsRespondListener;
import com.yaya.sdk.YayaNetStateListener;
import com.yaya.sdk.tcp.ITcpConnection;
import com.yaya.sdk.tcp.core.YayaTcp;
import com.yaya.sdk.tlv.protocol.message.TextMessageNotify;

public class MainActivity extends AppCompatActivity
        implements OnClickListener, VideoTroopsRespondListener {

    private int mode;
    private int env;
    private String seq;
    private String appId;
    private String tt;

    private void getIntentData() {
        mode = getIntent().getIntExtra("mode", 0);
        env = getIntent().getIntExtra("env", 0);
        seq = getIntent().getStringExtra("seq");
        appId = getIntent().getStringExtra("appId");
        tt = getIntent().getStringExtra("tt");
    }

    private Handler mainHandler = new Handler(Looper.getMainLooper());

    private ProgressDialog mProgressDialog;
    private Dialog mExitDialog;

    private TextView tvMode;
    private TextView tvNetState;
    private TextView tvSeq;
    private TextView tvYunvaId;
    private TextView tvMicState;

    private View recordContainer;
    private ImageView ivRecording;
    private AnimationDrawable recordAnim;

    private Button btnMic;
    private Button btnMessageFilter;

    private Button btnSetLimit;
    private Button btnCancelLimit;

    private String currentMicType = RTV.ACTION_TYPE_CLOSE_MIC; //当前麦状态

    private DemoChatBox mChatBox;

    private long yunvaId = -1;

    private Dialog mSetFilterDialog;

    private boolean hasMicLimit;
    private static final long LimitTime = 8000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getIntentData();
        initViews();

        YayaRTV.Env sdkEnv = null;
        if(env == 0) {
            sdkEnv = RTV.Env.Test;
        } else if(env == 1) {
            sdkEnv = RTV.Env.Product;
        } else if(env == 2) {
            sdkEnv = RTV.Env.Oversea;
        }
        RTV.Mode rtvMode = null;
        if(mode == 0) {
            rtvMode = RTV.Mode.Free;
        } else if(mode == 1) {
            rtvMode = RTV.Mode.Robmic;
        } else {
            rtvMode = RTV.Mode.Robmic;
        }

        mSetFilterDialog = new SetFilterDialog(this);
        mProgressDialog.setMessage("init SDK...");
        mProgressDialog.show();
        //YayaRTV.getInstance().init(this, "1000398", this, sdkEnv);
        YayaRTV.getInstance().init(this, appId, this, sdkEnv, rtvMode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        if(mExitDialog != null && mExitDialog.isShowing()) {
            mExitDialog.dismiss();
        }
        if(mSetFilterDialog != null && mSetFilterDialog.isShowing()) {
            mSetFilterDialog.dismiss();
        }
        try {
            YayaRTV.getInstance().destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if(mExitDialog != null && mExitDialog.isShowing()) {
                mExitDialog.dismiss();
            }
            mExitDialog = new AlertDialog.Builder(this)
                    .setMessage("确定退出房间？")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            YayaRTV.getInstance().logout();
                        }
                    })
                    .show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initViews() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        tvMode = (TextView) findViewById(R.id.tv_mode);
        tvSeq = (TextView) findViewById(R.id.tv_seq);
        tvNetState = (TextView) findViewById(R.id.tv_net);
        tvYunvaId = (TextView) findViewById(R.id.tv_yunvaId);
        tvMicState = (TextView) findViewById(R.id.tv_mic_state);
        recordContainer = findViewById(R.id.record_state_container);
        ivRecording = (ImageView) findViewById(R.id.iv_record_anim);
        btnMic = (Button) findViewById(R.id.btn_mic);
        btnMessageFilter = (Button) findViewById(R.id.btn_filter);
        btnSetLimit = (Button) findViewById(R.id.btn_set_mic_time);
        btnCancelLimit = (Button) findViewById(R.id.btn_remove_mic_time);

        btnMessageFilter.setOnClickListener(this);
        recordAnim = (AnimationDrawable) getResources()
                .getDrawable(R.drawable.live_sdk_record_state_anim);
        ivRecording.setImageDrawable(recordAnim);
        btnMic.setText("上麦");
        btnMic.setOnClickListener(this);

        btnSetLimit.setOnClickListener(this);
        btnCancelLimit.setOnClickListener(this);

        mChatBox = (DemoChatBox) findViewById(R.id.chat_box);
        mChatBox.setSendBtnClickListener(sendTextBtnListener);

        if(mode == 0) {
            tvMode.setText("当前房间模式：自由模式");
        } else if(mode == 1) {
            tvMode.setText("当前房间模式：抢麦模式");
        }

        tvSeq.setText("房间seq：" + seq);
        tvMicState.setText("当前麦状态：关闭");
    }

    private YayaNetStateListener netStateListener = new YayaNetStateListener() {
        @Override
        public void onNetStateUpdate(long send, long recv) {
            final long elapse = recv - send;
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    tvNetState.setText("当前网络状态: " + elapse);
                }
            });
        }
    };

    private DemoChatBox.SendCallback sendTextBtnListener = new DemoChatBox.SendCallback() {

        @Override
        public boolean onSendBtnClicked(DemoChatBox chatBox, String text) {
            if(yunvaId < 0) {
                return false;
            }
            YayaRTV.getInstance().sendTextMessage(text, "demo expand");
            mChatBox.addSelfMsg(yunvaId, text);
            return true;
        }
    };

    @Override
    public void onClick(View v) {
        if(v == btnMic) {
            if(currentMicType.equals(RTV.ACTION_TYPE_OPEN_MIC)) {
                //YayaRTV.getInstance().mic(RTV.ACTION_TYPE_CLOSE_MIC);
                YayaRTV.getInstance().micDown();
                btnMic.setEnabled(false);
            } else {
                if(hasMicLimit) {
                    YayaRTV.getInstance().micUpWithLimit(LimitTime);
                } else {
                    YayaRTV.getInstance().micUp();
                    //YayaRTV.getInstance().mic(RTV.ACTION_TYPE_OPEN_MIC);
                }
                btnMic.setEnabled(false);
            }
        } else if(v == btnMessageFilter) {
            if(mSetFilterDialog != null && !mSetFilterDialog.isShowing()) {
                mSetFilterDialog.show();
            }
        } else if(v == btnSetLimit) {
            hasMicLimit = true;
            Toast.makeText(this, "设置上麦最长8s", Toast.LENGTH_SHORT).show();
        } else if(v == btnCancelLimit) {
            hasMicLimit = false;
            Toast.makeText(this, "取消上麦限制", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void initComplete() {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.setMessage("auth account...");
                if(!mProgressDialog.isShowing()) {
                    mProgressDialog.show();
                }
                if(tt == null) {
                    YayaRTV.getInstance().login(seq);
                } else {
                    YayaRTV.getInstance().loginBinding(tt, seq);
                }
            }
        });
    }

    @Override
    public void onLoginResp(final int result, final String msg, long yunvaId, byte mode) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.dismiss();
                if(result == 0) {
                    Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    YayaRTV.getInstance().setNetStateListener(netStateListener);
                } else {
                    Toast.makeText(MainActivity.this, "登录失败 " + result + "," + msg,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onLogoutResp(final long result, final String msg) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if(result == 0) {
                    Toast.makeText(MainActivity.this, "登出成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "登出异常 " + result + "," + msg,
                            Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }

    @Override
    public void onMicResp(final long result, final String msg, final String actionType) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                btnMic.setEnabled(true);
                if(result != 0) {
                    //失败
                    Toast.makeText(MainActivity.this,
                            "麦请求失败 " + result + "," + msg, Toast.LENGTH_SHORT).show();
                    setUiRecordState(false);
                    return;
                }
                currentMicType = actionType;
                setUiRecordState(currentMicType.equals(RTV.ACTION_TYPE_OPEN_MIC));
            }
        });
    }

    @Override
    public void onModeSettingResp(long result, String msg, RTV.Mode mode) {

    }

    private void setUiRecordState(boolean isRecording) {
        if(isRecording) {
            tvMicState.setText("当前麦状态：打开");
            btnMic.setText("下麦");
            recordContainer.setVisibility(View.VISIBLE);
            ivRecording.setBackgroundDrawable(recordAnim);
            recordAnim.start();
        } else {
            tvMicState.setText("当前麦状态：关闭");
            btnMic.setText("上麦");
            recordAnim.stop();
            recordContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSendRealTimeVoiceMessageResp(long result, String msg) {

    }

    @Override
    public void onTextMessageNotify(final TextMessageNotify textMessageNotify) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                mChatBox.addChatMsg(textMessageNotify);
            }
        });
    }

    @Override
    public void onSendTextMessageResp(final long result, final String msg, String expand) {
        Log.d("MainActivity", "onSendTextMessageResp " + result);
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if(result < 0) {
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRealTimeVoiceMessageNotify(String troopsId, long yunvaId, String expand) {
        Log.i("Listener", "有人说话 " + yunvaId);
    }

    @Override
    public void onTroopsModeChangeNotify(RTV.Mode mode, boolean isLeader) {

    }

    @Override
    public void audioRecordUnavailableNotify(int result, String msg) {

    }

    @Override
    public void onAuthResp(final long result, final String msg, final long yunvaId) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if(result == 0) {
                    tvYunvaId.setText("yunvaId:" + yunvaId);
                    MainActivity.this.yunvaId = yunvaId;
                    mProgressDialog.setMessage("登录队伍房间...");
                    if (!mProgressDialog.isShowing()) {
                        mProgressDialog.show();
                    }
                } else {
                    mProgressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "授权失败 " + result + "," + msg,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    @Override
    public void onGetRoomResp(final long result, final String msg) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if(result != 0) {
                    mProgressDialog.dismiss();
                    Toast.makeText(MainActivity.this,
                            "获取房间失败 " + result + "," + msg,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });
    }

    @Override
    public void onReconnectStart() {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mProgressDialog.isShowing()) {
                    mProgressDialog.setMessage("正在重连...");
                }
                Toast.makeText(MainActivity.this,
                        "开始重连...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onReconnectFail(final int code, final String msg) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                Toast.makeText(MainActivity.this,
                        "重连失败 " + code + "," + msg, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onReconnectSuccess() {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                Toast.makeText(MainActivity.this,
                        "重连成功", Toast.LENGTH_SHORT).show();
                setUiRecordState(false);
                currentMicType = RTV.ACTION_TYPE_CLOSE_MIC;
                YayaRTV.getInstance().setNetStateListener(netStateListener);
            }
        });
    }

}
