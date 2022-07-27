package com.netoloboapps.restaurantsapp.restaurants.domain

import com.netoloboapps.restaurantsapp.restaurants.data.RestaurantRepository
import javax.inject.Inject

class ToogleRestaurantUserCase @Inject constructor(
    private val repository: RestaurantRepository,
    private val getSortedRestaurantsUseCase: GetSortedRestaurantsUseCase
) {


    suspend operator fun invoke(
        id: Int,
        oldValue: Boolean
    ): List<Restaurant>{

        val newFav = oldValue.not()

        repository.toggleFavoriteRestaurant(id, newFav)

        return getSortedRestaurantsUseCase()
    }
}