package co.prandroid.wishbirthday.model

import java.util.*

/**
 * Created by dharmakshetri on 9/6/17.
 */
data class Wisher(
        val name: String = "",
        val mobile: String = "",
        val email: String="",
        val isFavourite:Boolean=false,
        val category: String="",
        val month:String="",
        val day:String="",
        var uuid: String = ""
)