package com.yaya.sdk.tlv.protocol;

import yaya.tlv.annotation.TlvMsg;
import yaya.tlv.signal.TlvSignal;

@TlvMsg(moduleId = 0xB400, msgCode = 0x0002)
public class HeartbeatResp extends TlvSignal {
}
