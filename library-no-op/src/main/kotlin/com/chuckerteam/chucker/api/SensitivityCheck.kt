package com.chuckerteam.chucker.api

/**
 * Created by putu.permadi on 03/08/22.
 */

public class SensitivityCheck(private val builder: Builder) {

    internal fun isCheckHostSensitive(host: String?): Boolean {
        return false
    }

    internal fun isCheckResponseBodySensitive(response: String?): Boolean {
        return false
    }

    internal fun isCheckPathSensitive(path: String?): Boolean {
        return false
    }

    internal fun isRequestBodySensitive(requestBody: String?): Boolean {
        return false
    }

    private fun isSensitive(text: CharSequence?): Boolean {
        return false
    }

    public class Builder {
        public fun isCheckHostSensitivity(enable: Boolean): Builder = this
        public fun isCheckPathSensitivity(enable: Boolean): Builder = this
        public fun isCheckResponseBodySensitivity(enable: Boolean): Builder = this
        public fun isCheckRequestBodySensitivity(enable: Boolean): Builder = this
        public fun build(): SensitivityCheck = SensitivityCheck(this)
    }
}