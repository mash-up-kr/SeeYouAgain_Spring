package com.mashup.shorts.logger

interface ThirdPartyLogger {
    fun log(msg: String, logType: LogType)
}
