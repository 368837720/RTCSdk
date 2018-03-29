package com.yaya.sdk;

import com.yaya.sdk.tlv.protocol.message.TextMessageNotify;

/**
 * 房间内的消息过滤器（抢麦模式有效）<br/>
 * 用于过滤语音、文字消息，例如可过滤掉某个yunvaId的文字消息<br/>
 * 方法返回true表示拦截，返回false表示不拦截 <br/>
 * 过滤器中也可加入一些需要的处理逻辑，不建议进行耗时操作
 */
public interface MessageFilter {
    /**
     * 拦截语音消息
     * @param voiceYunvaId 发送者的yunvaId
     * @param expand 消息的透传字段
     * @return true则sdk不再处理该语音消息 例如{@link VideoTroopsRespondListener#onRealTimeVoiceMessageNotify(String, long, String)}则不再响应;
     *         false则sdk继续处理该消息
     * */
    boolean filterVoiceMsg(Long voiceYunvaId, String expand);

    /**
     * 拦截文字消息
     * @param messageYunvaId 发送者的yunvaId
     * @param expand 消息的透传字段
     * @return true则sdk不再处理该文字消息 例如{@link VideoTroopsRespondListener#onTextMessageNotify(TextMessageNotify)} 则不再响应;
     *         false则sdk继续处理该消息
     * */
    boolean filterTextMsg(Long messageYunvaId, String text, String expand);
}
