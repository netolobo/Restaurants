package com.netoloboapps.restaurantsapp.restaurants.domain

import com.netoloboapps.restaurantsapp.restaurants.data.RestaurantRepository
import javax.inject.Inject

class GetInitialRestaurantsUserCase @Inject constructor(
    private val repository: RestaurantRepository,
    private val getSortedRestaurantsUseCase: GetSortedRestaurantsUseCase
) {

    suspend operator fun invoke():List<Restaurant>{

        repository.loadRestaurants()

        return getSortedRestaurantsUseCase()
    }
}