package com.yaya.sdk;

import com.yaya.sdk.tlv.protocol.message.TextMessageNotify;

/**
 * SDK的主要回调接口
 */
public interface VideoTroopsRespondListener {

	/**
	 * 初始化完成回调<br/>
	 * 初始化完成后，才能调用sdk其它接口
	 */
	void initComplete();

	/**
	 * 登录的第三步（最后一步）响应，若返回成功则表示登录完成<br/>
	 * 在登录流程中，在{@link VideoTroopsRespondListener#onGetRoomResp(long, String)}成功之后才会触发<br/>
	 * 断线重连时也可能会回调<br/>
	 * @param result 0成功，其它为失败
	 * @param msg 错误信息,可能为空
	 * @param yunvaId 成功后返回yunvaId
	 */
	void onLoginResp(int result, String msg, long yunvaId, byte mode);

	/**
	 * 登出回调<br/>
	 * 
	 * @param result 0成功，其它为失败
	 * @param msg 错误信息，可能为空
	 */
	void onLogoutResp(long result, String msg);

	/**
	 * 上/下麦请求回应<br/>
	 *
	 * @param result 0成功，其它为失败
	 * @param msg 错误信息，可能为空
	 * @param actionType 标识当前回应的麦请求类型，上麦回应:{@link RTV#ACTION_TYPE_OPEN_MIC};
	 *            关麦:{@link RTV#ACTION_TYPE_CLOSE_MIC}
	 */
	void onMicResp(long result, String msg, String actionType);

	void onModeSettingResp(long result, String msg, RTV.Mode mode);

	/**
	 * 发送实时语音消息失败的回调，成功不会回调
	 * @param result 错误码
	 * @param msg 错误信息，可能为空
     */
	void onSendRealTimeVoiceMessageResp(long result, String msg);

	/**
	 * 接收到文字消息的回调
	 * @param textMessageNotify 文字消息实体类;
	 *                             textMessageNotify.getYunvaId() 可获取发送者yunvaId;
	 *                             textMessageNotify.getText() 可获取文字消息
     */
	void onTextMessageNotify(TextMessageNotify textMessageNotify);

	/**
	 * 发送文字消息失败的回调，发送成功不会回调
	 * @param result 错误码
	 * @param msg 错误信息，可能为空
	 * @param expand 透传字段
     */
	void onSendTextMessageResp(long result, String msg, String expand);

	/**
	 * 接收实时语音消息的通知<br/>
	 * 在接收实时语音消息的过程中会周期性回调，间隔大约100ms
	 *
	 * @param troopsId 当前房间的id
	 * @param yunvaId 说话人的yunvaId，抢麦模式有效
	 * @param expand 语音消息扩展字段，抢麦模式有效
	 */
	void onRealTimeVoiceMessageNotify(String troopsId, long yunvaId, String expand);

	/**
	 * 接受到房间麦模式改变的通知
	 *
	 * @param mode 房间模式已经更改为该模式
	 * @param isLeader 自己是否是leader （队长模式有效）
     */
	void onTroopsModeChangeNotify(RTV.Mode mode, boolean isLeader);

	/**
	 * 录音设备不可用通知，例如录音没有权限、录音设备故障。
	 * @param result 错误码
	 * @param msg 错误信息，可能为空
	 */
	void audioRecordUnavailableNotify(int result, String msg);

	/**
	 * 帐号授权回调，登录的第一步响应，成功时才会进行下一步。
	 * @param result 0成功，其它失败
	 * @param msg 错误信息，可能为空
	 * @param yunvaId 成功时会返回当前帐号yunvaId
     */
	void onAuthResp(long result, String msg, long yunvaId);

	/**
	 * 获取房间信息回调，登录的第二部响应，成功时才会进行下一步
	 * @param result 0成功，其它失败
	 * @param msg 错误信息，可能为空
     */
	void onGetRoomResp(long result, String msg);

	/**
	 * 开始重连的回调, SDK会重新走一遍登录的流程<br/>
	 * @see
	 * {@link #onAuthResp(long, String, long)};
	 * {@link #onGetRoomResp(long, String)};
	 * {@link #onLoginResp(int, String, long, byte)}
	 */
	void onReconnectStart();

	/**
	 * 重连失败的回调（不会每次重连失败都回调，重连次数用完时，会最终回调到该方法）<br/>
	 * 进入该回调表示SDK不会再继续尝试重连，请用户处理接下来的流程。<br/>
	 * 例如可以在检查网络之后，重新调用login，或者在
	 * @param code 最后一次重连失败的错误码
	 * @param msg 错误信息，可能为空
     */
	void onReconnectFail(int code, String msg);

	/**
	 * 重连成功的回调，表示用户已经重新登录成功
	 */
	void onReconnectSuccess();
}
