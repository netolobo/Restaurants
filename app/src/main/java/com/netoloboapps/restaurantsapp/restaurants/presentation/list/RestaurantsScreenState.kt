package com.netoloboapps.restaurantsapp.restaurants.presentation.list

import com.netoloboapps.restaurantsapp.restaurants.domain.Restaurant

data class RestaurantsScreenState(
    val restaurants: List<Restaurant>,
    val isLoading: Boolean,
    val error: String? = null
)
