package com.netoloboapps.restaurantsapp.restaurants.presentation.details

import com.netoloboapps.restaurantsapp.restaurants.data.local.LocalRestaurant

data class RestaurantDetailsScreenState(
    val restaurant: LocalRestaurant,
    val isLoading: Boolean,
    val error: String? = null
)