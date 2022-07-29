package com.chuckerteam.chucker.internal.toolkit

import com.chuckerteam.chucker.internal.data.entity.HttpTransaction

public class SensitivityCheck(private val builder: Builder) {
    private val regex = builder.regexValidator

    internal fun isCheckHostSensitive(host: String?): Boolean {
        return builder.isCheckHostSensitivity && isSensitive(host)
    }

    internal fun isCheckResponseBodySensitive(response: String?): Boolean {
        return builder.isCheckResponseBodySensitivity && isSensitive(response)
    }

    internal fun isCheckPathSensitive(path: String?): Boolean {
        return builder.isCheckPathSensitivity && isSensitive(path)
    }

    internal fun isRequestBodySensitive(requestBody: String?): Boolean {
        return builder.isCheckRequestBodySensitivity && isSensitive(requestBody)
    }

    private fun isSensitive(text: CharSequence?): Boolean {
        if (text.isNullOrBlank()) return false
        return regex.containsMatchIn(text)
    }

    public class Builder(regex: Regex) {
        internal var isCheckHostSensitivity: Boolean = false
        internal var isCheckPathSensitivity: Boolean = false
        internal var isCheckResponseBodySensitivity: Boolean = false
        internal var isCheckRequestBodySensitivity: Boolean = false
        internal var regexValidator = regex

        public fun isCheckHostSensitivity(enable: Boolean): Builder = apply {
            isCheckHostSensitivity = enable
        }

        public fun isCheckPathSensitivity(enable: Boolean): Builder = apply {
            isCheckPathSensitivity = enable
        }

        public fun isCheckResponseBodySensitivity(enable: Boolean): Builder = apply {
            isCheckResponseBodySensitivity = enable
        }

        public fun isCheckRequestBodySensitivity(enable: Boolean): Builder = apply {
            isCheckRequestBodySensitivity = enable
        }

        public fun build(): SensitivityCheck = SensitivityCheck(this)
    }
}
