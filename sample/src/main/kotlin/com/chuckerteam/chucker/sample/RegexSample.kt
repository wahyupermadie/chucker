package com.chuckerteam.chucker.sample

/**
 * Created by putu.permadi on 24/06/22.
 */

object LogPattern {

    val combinedRegex: Regex by lazy {
        combined()
            .toRegex(RegexOption.IGNORE_CASE)
    }

    const val numericValue: String = "-{0,1}\\d{1,4}\\.{0,1}\\d{0,10}"
    const val xsLimit: String = "{1,5}"
    const val sLimit: String = "{1,10}"
    const val mLimit: String = "{1,25}"
    const val lLimit: String = "{1,50}"
    const val xlLimit: String = "{1,150}"
    const val addressLimit: String = "{10,150}"
    const val separator: String = "|"

    sealed class Geo {
        companion object Companion {
            private val list = listOf(
                "addr.$addressLimit",
                "lat.$mLimit$numericValue",
                "lon.$mLimit$numericValue",
                "lng.$mLimit$numericValue",
                "latitude.$mLimit$numericValue",
                "longitude.$mLimit$numericValue",
                "co{1,}rd.$mLimit$numericValue",
                "location.$xlLimit",
                "poi.$xlLimit",
                "place.$xlLimit",
                "position.$xlLimit",
                "tta.$xsLimit$numericValue",
                "eta.$xsLimit$numericValue",
                "city.$xlLimit",
                "postcode.$mLimit",
                "street.$lLimit",
                "pick.{0,1}up.$xlLimit",
                "drop.{0,1}off.$xlLimit"
            )

            fun combined(): String = list.joinToString(separator)
        }
    }

    private fun combined(): String = Geo.combined()
}
