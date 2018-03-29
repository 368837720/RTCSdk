package com.yaya.sdk;

import android.content.Context;

import com.yaya.sdk.tlv.protocol.message.TextMessageNotify;

/**
 * 实时语音SDK的主要接口 <br/>
 * 可通过YayaRtv.getInstance()获取单实例
 * @version 230
 * @author ober
 */
public interface RTV {

    /**
     *  语音SDK的服务环境
     */
    enum Env {
        /**
         * 测试环境
         */
        Test,
        /**
         * 正事环境
         */
        Product,
        /**
         * 海外环境
         */
        Oversea
    }

    /**
     * 语音模式
     */
    enum Mode {
        /**
         * 自由模式<br/>
         * 所有用户可自由上下麦
         */
        Free,
        /**
         * 抢麦模式<br/>
         * 所有用户可自由请求上麦/下麦，同一时间最多只有一个用户能取得麦权
         */
        Robmic,
        /**
         * 队长模式<br/>
         * 队长可以自由上下麦
         */
        Leader
    }

    /**
     * 初始化SDK <br/>
     * 初始化完成会触发回调{@link VideoTroopsRespondListener#initComplete()}<br/>
     * SDK其它接口都需要在初始化成功之后，才能调用<br/>
     * 初始化默认为异步操作，也可设置为同步操作{@link InitParams#setInitMethodAsynchronous(boolean)}
     *
     * @param context 调用init的Activity的Context,或者Application的Context, 不可以为null
     * @param appId 从Yunva官方申请的appId， 不可以为null
     * @param listener 回调接口，大部分回调都在这里面， 可以为null。
     * @param env 服务环境， 如果为null则默认为测试环境
     * @param params 初始化参数，null表示默认参数，详见{@link InitParams}
     * @param mode 语音模式，null表示默认自由模式
     *
     * @throws NullPointerException 如果context或者appId为空
     */
    void init(Context context, String appId,
              VideoTroopsRespondListener listener,
              Env env, InitParams params, Mode mode);

    /**
     * 初始化SDK <br/>
     * 初始化完成会触发回调{@link VideoTroopsRespondListener#initComplete()}<br/>
     * SDK其它接口都需要在初始化成功之后，才能调用<br/>
     * 初始化默认为异步操作，也可设置为同步操作{@link InitParams#setInitMethodAsynchronous(boolean)}
     *
     * @param context 调用init的Activity的Context,或者Application的Context, 不可以为null
     * @param appId 从Yunva官方申请的appId， 不可以为null
     * @param listener 回调接口，大部分回调都在这里面， 可以为null。
     * @param env 服务环境， 如果为null则默认为测试环境
     * @param mode 语音模式，null表示默认自由模式
     *
     * @throws NullPointerException 如果context或者appId为空
     */
    void init(Context context, String appId, VideoTroopsRespondListener listener, Env env, Mode mode);

    /**
     * 设置网络状态监听器<br/>
     * 该监听器将周期性返回（客户端-YaYa服务端）之间的网络状况
     *
     * @param listener （客户端-YaYa服务端）网络状态监听回调，设为null则取消监听
     *
     */
    void setNetStateListener(YayaNetStateListener listener);

    /**
     * 销毁SDK/释放资源<br/>
     * 建议当不再使用sdk时调用, 下次再使用时需要重新调用sdk的init方法<br/>
     * 也可以在任何情况下强制销毁sdk，停止sdk的功能，包括正在初始化中
     */
    void destroy();

    /**
     * 返回当前SDK的版本
     * @return 当前SDK版本
     */
    String getSdkVersion();

    /**
     * 返回初始化时传入的appId
     * @return 用户的appId
     */
    String getAppId();

    /**
     * 三方绑定Yunva帐号登录语音房间<br/>
     * 这是一个链式登录过程, 在上一步成功之后才会进入下一步, 某一步失败之后则不再进行下去。<br/>
     * 下面是登录链的3个步骤和对应的回调: <br/>
     * 1.生成yunvaId并授权 {@link VideoTroopsRespondListener#onAuthResp(long, String, long)}<br/>
     * 2.获取房间信息 {@link VideoTroopsRespondListener#onGetRoomResp(long, String)}<br/>
     * 3.建立连接登录 {@link VideoTroopsRespondListener#onLoginResp(int, String, long, byte)}<br/>
     *
     * @param tt 三方透传字段 <br/>
     *           默认为json格式{"uid":"jacky123", "nickname": "大兄弟"} <br/>
     *           其中uid不可为空，默认的协议保证uid与YunvaId是一一对应关系<br/>
     *           如有特殊需求，也可和Yunva商定三方绑定协议。
     *
     * @param seq 唯一标识(在同一个appId的情况下)该房间的序号，不能为空
     *
     * @throws NullPointerException 如果seq为空
     *
     */
    void loginBinding(String tt, String seq);

    /**
     * 注册Yunva帐号并登录语音房间<br/>
     * 这是一个链式登录过程, 在上一步成功之后才会进入下一步, 某一步失败之后则不再进行下去。<br/>
     * 下面是登录链的3个步骤和对应的回调: <br/>
     * 1.生成yunvaId并授权 {@link VideoTroopsRespondListener#onAuthResp(long, String, long)}<br/>
     * 2.获取房间信息 {@link VideoTroopsRespondListener#onGetRoomResp(long, String)}<br/>
     * 3.建立连接登录 {@link VideoTroopsRespondListener#onLoginResp(int, String, long, byte)}<br/>
     *
     * @param seq 唯一标识(在同一个appId的情况下)该房间的序号，不能为空
     *
     * @throws NullPointerException 如果seq为空
     *
     */
    void login(String seq);

    /**
     * 用Yunva帐号登录语音房间<br/>
     * 这是一个链式登录过程, 在上一步成功之后才会进入下一步, 某一步失败之后则不再进行下去。<br/>
     * 下面是登录链的3个步骤和对应的回调: <br/>
     * 1.生成yunvaId并授权 {@link VideoTroopsRespondListener#onAuthResp(long, String, long)}<br/>
     * 2.获取房间信息 {@link VideoTroopsRespondListener#onGetRoomResp(long, String)}<br/>
     * 3.建立连接登录 {@link VideoTroopsRespondListener#onLoginResp(int, String, long, byte)}<br/>
     *
     * @param yunvaId Yunva帐号
     * @param password 当前帐号对应的密码
     * @param seq 唯一标识(在同一个appId的情况下)该房间的序号，不能为空
     * @throws NullPointerException 如果seq为空
     *
     */
    void login(long yunvaId, String password, String seq);

    /**
     * 用三方绑定帐号登录语音房间<br/>
     * 这是一个链式登录过程, 在上一步成功之后才会进入下一步, 某一步失败之后则不再进行下去。<br/>
     * 下面是登录链的3个步骤和对应的回调: <br/>
     * 1.生成yunvaId并授权 {@link VideoTroopsRespondListener#onAuthResp(long, String, long)}<br/>
     * 2.获取房间信息 {@link VideoTroopsRespondListener#onGetRoomResp(long, String)}<br/>
     * 3.建立连接登录 {@link VideoTroopsRespondListener#onLoginResp(int, String, long, byte)}<br/>
     *
     * @param yunvaId Yunva帐号
     * @param tt 三方透传字段 <br/>
     *           默认为json格式{"uid":"jacky123", "nickname": "大兄弟"} <br/>
     *           其中uid不可为空，默认的协议保证uid与YunvaId是一一对应关系<br/>
     *           如有特殊需求，也可和Yunva商定三方绑定协议。     * @param thirdId 三方帐号id
     * @param thirdUserName 三方帐号名
     * @param seq 唯一标识(在同一个appId的情况下)该房间的序号，不能为空
     * @throws NullPointerException 如果seq为空
     *
     */
    void thirdAuth(long yunvaId, String tt, String thirdId, String thirdUserName, String seq);

    /**
     * 登出房间（停止录音、发送、收听等）<br/>
     * 超时时间为{@link InitParams#getLogoutTimeout()}，如果登出超时，则强制退出
     */
    void logout();

    /**
     * 上麦
     */
    String ACTION_TYPE_OPEN_MIC = "1";

    /**
     * 下麦
     */
    String ACTION_TYPE_CLOSE_MIC = "0";

    /**
     * 建议使用 {@link #micUp()}
     *          {@link #micDown()}
     *          {@link #micUp(String)}
     *          {@link #micDown(String)}
     *          {@link #micUpWithLimit(long)}
     *          {@link #micUpWithLimit(long, String)}
     *
     * @param actionType "1" 上麦 {@link #ACTION_TYPE_OPEN_MIC}; "0" 下麦 {@link #ACTION_TYPE_CLOSE_MIC};
     * @param expand 透传字段，仅对抢麦模式有效<br/>
     *               例如:A抢麦时带上expand="I'm vip"，并且抢麦成功了，
     *               这时B,C,D收到了A的语音消息，这时B,C,D可以读取到A的语音消息中expand="I'm vip"
     *               @see {@link VideoTroopsRespondListener#onRealTimeVoiceMessageNotify(String, long, String)}
     *
     */
    void mic(String actionType, String expand);

    /**
     * 透传字段为空，同{@link #mic(String, String)}
     */
    void mic(String actionType);

    /**
     * 上麦操作,对应回调{@link VideoTroopsRespondListener#onMicResp(long, String, String)}<br/>
     * 在登录状态下调用有效
     * 超时时间为{@link InitParams#micUpTimeout}，上麦超时会进行重连
     *
     * @param expand 透传字段
     */
    void micUp(String expand);

    /**
     * 同{@link #mic(String)}，透传为空
     */
    void micUp();

    /**
     * 上麦,并增加最大时间限制<br/>
     * 其它同{@link #micUp(String)}<br/>
     * 该方法主要用于避免，由于用户程序异常而没有调用下麦造成的异常后果。
     * 尽量不要用该方法来控制上麦时间和自动下麦，该方法的到时自动下麦不保证时间准确。
     * 控制上麦时间应该在上麦的回调中开启定时任务或者以其他方式处理
     * 建议limitMillis设为稍大于想要上麦的时间，下麦的时间尽量自己控制<br/>
     * 例如每次上麦5000ms，可将limitMillis设为8000(ms)。
     * @param limitMillis 上麦最长时间
     */
    void micUpWithLimit(long limitMillis, String expand);

    /**
     * 同{@link #micUpWithLimit(long, String)}, 透传为空
     */
    void micUpWithLimit(long limitMillis);

    /**
     * 下麦操作，对应回调{@link VideoTroopsRespondListener#onMicResp(long, String, String)} <br/>
     * 在登录状态和上麦状态调用有效（否则会触发失败的回调）<br/>
     * 下麦超时{@link InitParams#micDownTimeout}会强制退出重连
     * @param expand 透传字段
     */
    void micDown(String expand);

    /**
     * 同{@link #micDown(String)}，透传为空
     */
    void micDown();

    /**
     * 发送文字消息，对应回调{@link VideoTroopsRespondListener#onSendTextMessageResp(long, String, String)}<br/>
     * 注：发送成功没有回调，发送失败会回调。<br/>
     * 需要在登录状态下有效，否则触发失败回调
     * @param text 发送的文字消息，没有长度限制，建议勿发送长度超过200的字符串
     * @param expand 透传字段。对任意语音模式有效<br/>
     *               例子：A发送文字消息，带上expand="I'm vip"，这时B,C,D收到A的文字消息，
     *               可从A的文字消息中读取到expand="I'm vip"。
     *               @see {@link VideoTroopsRespondListener#onTextMessageNotify(TextMessageNotify)}
     */
    void sendTextMessage(String text, String expand);

    /**
     * 同{@link #sendTextMessage(String, String)}，透传为空
     */
    void sendTextMessage(String text);

    void modeSettingReq(Mode mode);

    /**
     * 设置房间内消息的过滤器<br/>
     * 在登录状态下设置有效，过滤器只对当次连接有效(如断线重连，退出房间，都会失效)
     * @param filter 过滤器
     * @return true设置成功, false设置失败
     */
    boolean setMessageFilter(MessageFilter filter);
}
