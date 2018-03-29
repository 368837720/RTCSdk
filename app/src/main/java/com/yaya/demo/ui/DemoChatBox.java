package com.yaya.demo.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputBinding;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.yaya.demo.R;
import com.yaya.sdk.tlv.protocol.message.TextMessageNotify;

/**
 * Created by ober on 2016/11/17.
 */
public class DemoChatBox extends FrameLayout {

    public interface SendCallback {
        boolean onSendBtnClicked(DemoChatBox chatBox, String text);
    }

    private ListView lvChat;
    private Button btnSend;
    private EditText etInput;

    private SimpleChatAdapter mChatAdapter;

    public DemoChatBox(Context context) {
        super(context);
        init();
    }

    public DemoChatBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DemoChatBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.layout_chat_box, this);
        lvChat = (ListView) findViewById(R.id.list_chat);
        btnSend = (Button) findViewById(R.id.btn_send);
        etInput = (EditText) findViewById(R.id.et_input);
        mChatAdapter = new SimpleChatAdapter();
        lvChat.setAdapter(mChatAdapter);
    }

    public void setSendBtnClickListener(final SendCallback callback) {
        btnSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String text = etInput.getText().toString();
                if(callback.onSendBtnClicked(DemoChatBox.this, text)) {
                    etInput.setText("");
                }
            }
        });
    }

    public void addChatMsg(TextMessageNotify textMsg) {
        mChatAdapter.addData(textMsg);
        scrollToBottom();
    }

    public void addSelfMsg(long yunvaId, String msg) {
        TextMessageNotify textMessageNotify = new TextMessageNotify();
        textMessageNotify.setYunvaId(yunvaId);
        textMessageNotify.setText(msg);
        addChatMsg(textMessageNotify);
    }

    private void scrollToBottom() {
        lvChat.setSelection(mChatAdapter.getCount() - 1);
    }


}
