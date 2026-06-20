package com.jyoti.moodbitenew.ai

import android.content.Context
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine

class AgoraConversationManager(
    private val context: Context
) {

    private var rtcEngine: RtcEngine? = null

    fun initialize(appId: String) {
        rtcEngine =
            RtcEngine.create(
                context,
                appId,
                object : IRtcEngineEventHandler() {}
            )

        rtcEngine?.enableAudio()
    }

    fun joinChannel(
        token: String?,
        channelName: String
    ) {
        rtcEngine?.joinChannel(
            token,
            channelName,
            0,
            ChannelMediaOptions()
        )
    }

    fun leaveChannel() {
        rtcEngine?.leaveChannel()
    }

    fun release() {
        rtcEngine?.leaveChannel()
        RtcEngine.destroy()
    }
}