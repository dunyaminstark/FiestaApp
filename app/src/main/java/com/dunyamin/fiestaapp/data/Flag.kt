package com.dunyamin.fiestaapp.data

import androidx.annotation.DrawableRes

data class Flag(
    val countryCode: String,
    val countryName: String,
    @DrawableRes val flagResource: Int
)