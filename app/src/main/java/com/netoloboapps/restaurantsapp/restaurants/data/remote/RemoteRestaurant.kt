package com.netoloboapps.restaurantsapp.restaurants.data.remote

import com.google.gson.annotations.SerializedName

class RemoteRestaurant(
    @SerializedName("r_id")
    val id: Int,
    @SerializedName("r_title")
    val title: String,
    @SerializedName("r_description")
    val description : String
)