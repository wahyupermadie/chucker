package com.chuckerteam.chucker.api

/**
 * Created by putu.permadi on 03/08/22.
 */

@Suppress("UnusedPrivateMember")
public class SensitivityCheck(private val builder: Builder) {

    @Suppress("UnusedPrivateMember")
    internal fun isCheckHostSensitive(host: String?): Boolean  = false

    @Suppress("UnusedPrivateMember")
    internal fun isCheckResponseBodySensitive(response: String?): Boolean = false

    @Suppress("UnusedPrivateMember")
    internal fun isCheckPathSensitive(path: String?): Boolean = false

    @Suppress("UnusedPrivateMember")
    internal fun isRequestBodySensitive(requestBody: String?): Boolean = false

    @Suppress("UnusedPrivateMember")
    private fun isSensitive(text: CharSequence?): Boolean = false

    /**
     * No-op implementation.
     */
    public class Builder(regex: Regex) {
        public fun isCheckHostSensitivity(enable: Boolean): Builder = this

        public fun isCheckPathSensitivity(enable: Boolean): Builder = this

        public fun isCheckResponseBodySensitivity(enable: Boolean): Builder = this

        public fun isCheckRequestBodySensitivity(enable: Boolean): Builder = this

        public fun build(): SensitivityCheck = SensitivityCheck(this)
    }
}
