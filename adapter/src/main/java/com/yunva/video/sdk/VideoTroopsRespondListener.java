package com.yunva.video.sdk;

import com.yunva.video.sdk.interfaces.logic.model.AudioRecordUnavailableNotify;
import com.yunva.video.sdk.interfaces.logic.model.InviteUserVideoNotify;
import com.yunva.video.sdk.interfaces.logic.model.InviteUserVideoRespNotify;
import com.yunva.video.sdk.interfaces.logic.model.LoginResp;
import com.yunva.video.sdk.interfaces.logic.model.ModeSettingResp;
import com.yunva.video.sdk.interfaces.logic.model.QueryBlackUserResp;
import com.yunva.video.sdk.interfaces.logic.model.QueryHistoryMsgResp;
import com.yunva.video.sdk.interfaces.logic.model.QueryUserListResp;
import com.yunva.video.sdk.interfaces.logic.model.RealTimeVoiceMessageNotify;
import com.yunva.video.sdk.interfaces.logic.model.SendRichMessageResp;
import com.yunva.video.sdk.interfaces.logic.model.SpeechDiscernResp;
import com.yunva.video.sdk.interfaces.logic.model.TextMessageNotify;
import com.yunva.video.sdk.interfaces.logic.model.TextMessageResp;
import com.yunva.video.sdk.interfaces.logic.model.TroopsModeChangeNotify;
import com.yunva.video.sdk.interfaces.logic.model.TroopsUser;
import com.yunva.video.sdk.interfaces.logic.model.UploadPicResp;
import com.yunva.video.sdk.interfaces.logic.model.UploadVoiceResp;
import com.yunva.video.sdk.interfaces.logic.model.UserLoginNotify;
import com.yunva.video.sdk.interfaces.logic.model.UserStateNotify;
import com.yunva.video.sdk.interfaces.logic.model.VideoStateNotify;
import com.yunva.video.sdk.interfaces.logic.model.VoiceMessageNotify;

import java.util.List;

public interface VideoTroopsRespondListener {

	/**
	 * 初始化完成
	 */
	public void initComplete();


	public void onLoginResp(LoginResp resp);

	/**
	 * 登出回应
	 * 
	 * @param result
	 *            结果码:VideoTroopsConstants.REQUEST_OK 成功，其它为失败
	 * @param msg
	 *            错误信息
	 */
	public void onLogoutResp(int result, String msg);

	/**
	 * 麦请求回应
	 * 
	 * @param result
	 *            结果码:VideoTroopsConstants.REQUEST_OK 成功，其它为失败
	 * @param msg
	 *            错误信息
	 * @param actionType
	 *            开麦:MicActionType.ACTION_TYPE_OPEN_MIC;
	 *            关麦:MicActionType.ACTION_TYPE_CLOSE_MIC
	 */
	public void onMicResp(int result, String msg, String actionType);

	/**
	 * 发送消息回应
	 * 
	 * @param resp
	 */
	public void onSendTextMessageResp(TextMessageResp resp);

	/**
	 * 上传语音留言消息回应
	 * 
	 * @param result
	 *            结果码:VideoTroopsConstants.REQUEST_OK 成功，其它为失败
	 * @param msg
	 *            错误信息
	 * @param voiceUrl
	 *            语音留言下载地址
	 * @param voiceDuration
	 *            语音留言时长(单位：毫秒)
	 * @param filePath
	 *            本地文件路径
	 * @param expand
	 *            扩展内容
	 */
	public void onUploadVoiceMessageResp(int result, String msg, String voiceUrl, long voiceDuration, String filePath, String expand);

	/**
	 * 发送实时语音聊天消息错误回应(只有消息发送失败才会收到回调)
	 * 
	 * @param result
	 *            结果码
	 * @param msg
	 *            错误信息
	 */
	public void onSendRealTimeVoiceMessageResp(int result, String msg);

	/**
	 * 文本聊天消息通知
	 * 
	 * @param textMessageNotify
	 */
	public void onTextMessageNotify(TextMessageNotify textMessageNotify);

	/**
	 * 语音留言消息通知
	 * 
	 * @param voiceMessageNotify
	 */
	public void onVoiceMessageNotify(VoiceMessageNotify voiceMessageNotify);

	/**
	 * 实时语音消息通知
	 * 
	 * @param realTimeVoiceMessageNotify
	 */
	public void onRealTimeVoiceMessageNotify(RealTimeVoiceMessageNotify realTimeVoiceMessageNotify);

	/**
	 * 视频状态的通知
	 * 
	 * @param videoStateNotify
	 */
	public void onVideoStateNotify(VideoStateNotify videoStateNotify);

	/**
	 * 邀请视频通知
	 * 
	 * @param inviteUserVideoNotify
	 */
	public void onInviteUserVideoNotify(InviteUserVideoNotify inviteUserVideoNotify);

	/**
	 * 邀请视频回复通知
	 * 
	 * @param inviteUserVideoRespNotify
	 */
	public void onInviteUserVideoRespNotify(InviteUserVideoRespNotify inviteUserVideoRespNotify);

	/**
	 * 用户视频管理(拉黑名单、取消黑名单)通知
	 * 
	 * @param type
	 *            0表示禁掉(拉黑名单)，1表示取消禁掉(取消黑名单), 2表示警告
	 */
	public void onUserVideoManageNotify(int type);

	/**
	 * 所有用户视频状态通知
	 * 
	 * @param users
	 */
	public void onUsersVideoStateNotify(List<TroopsUser> users);

	/**
	 * 用户登录、登出通知
	 * 
	 * @param notify
	 */
	public void onUserLoginNotify(UserLoginNotify notify);

	/**
	 * 查询视频用户列表回应
	 * 
	 * @param resp
	 */
	public void onQueryUserListResp(QueryUserListResp resp);

	/**
	 * 查询用户黑名单状态回应
	 * 
	 * @param resp
	 */
	public void onQueryBlackUserResp(QueryBlackUserResp resp);

	/**
	 * 用户状态通知
	 * 
	 * @param notify
	 */
	public void onUserStateNotify(UserStateNotify notify);

	/**
	 * 被踢出队伍通知
	 * 
	 * @param msg
	 */
	public void onTroopsKickOutNotify(String msg);

	/**
	 * 队伍模式设置
	 * 
	 * @param resp
	 */
	public void onModeSettingResp(ModeSettingResp resp);

	/**
	 * 队伍模式改变通知
	 * 
	 * @param notify
	 */
	public void onTroopsModeChangeNotify(TroopsModeChangeNotify notify);

	/**
	 * 上传语音留言消息回应
	 * 
	 * @param result
	 *            结果码:VideoTroopsConstants.REQUEST_OK 成功，其它为失败
	 * @param msg
	 *            错误信息
	 * @param voiceUrl
	 *            语音留言下载地址
	 * @param voiceDuration
	 *            语音留言时长(单位：毫秒)
	 * @param filePath
	 *            本地文件路径
	 * @param expand
	 *            扩展内容
	 * @param text
	 *            识别文本
	 */
	public void onUploadBdVoiceMessageResp(int result, String msg, String voiceUrl, long voiceDuration, String filePath, String expand, String text);

	/**
	 * 百度语音识别后自己的语音消息
	 * 
	 * @param text
	 */
	public void onBdMineTxtMsg(String text);

	/**
	 * 单独上传语音
	 * 
	 * @param resultCode
	 *            返回码：0表示上传成功，非0表示上传失败
	 * @param voiceUrl
	 *            返回语音url
	 */
	public void onOnlyUploadVoiceMessageResp(int resultCode, String voiceUrl);

	/**
	 * 上传语音文件回应
	 * 
	 * @param resp
	 */
	public void onUploadVoiceResp(UploadVoiceResp resp);

	/**
	 * 上传图片文件回应
	 * 
	 * @param resp
	 */
	public void onUploadPicResp(UploadPicResp resp);

	/**
	 * 查询历史消息返回
	 * 
	 * @param resp
	 */
	public void onQueryHistoryMsgResp(QueryHistoryMsgResp resp);

	/**
	 * 语音识别回应
	 * 
	 * @param text
	 * @param voiceDuration
	 */
	public void onVoicePathOrTextResp(int result, String msg,
									  int errorStatus,
									  int errorSubStatus, boolean isVoiceAndTxt, String text, String voicePath, Long voiceDuration);

	/**
	 * 发送文本+语音消息回应
	 * 
	 * @param resp
	 */
	public void onSendRichMessageResp(SendRichMessageResp resp);

	/**
	 * 录音设备不可用通知
	 * @param notify
	 */
	public void AudioRecordUnavailableNotify(AudioRecordUnavailableNotify notify);

	/**
	 * 语音识别回应
	 * @param resp
	 */
	public void httpVoiceRecognizeResp(SpeechDiscernResp resp);

	public void onAuthResp(int result, String msg);
	
	public void onTroopsIsDisconnectNotify();
	
	public void onBeginAutoReLoginWithTryTimes(int tryReLoginTimes);
}
