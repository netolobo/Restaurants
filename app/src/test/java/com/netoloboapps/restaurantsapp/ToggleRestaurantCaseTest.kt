package com.netoloboapps.restaurantsapp

import com.netoloboapps.restaurantsapp.restaurants.DummyContent
import com.netoloboapps.restaurantsapp.restaurants.data.RestaurantRepository
import com.netoloboapps.restaurantsapp.restaurants.domain.GetSortedRestaurantsUseCase
import com.netoloboapps.restaurantsapp.restaurants.domain.ToogleRestaurantUserCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class ToggleRestaurantCaseTest {
    private val dispatcher = StandardTestDispatcher()
    private val scope = TestScope(dispatcher)

    @Test
    fun toggleRestaurant_IsUpdatingFavoriteField() = scope.runTest {
            //Setup useCase
            val restaurantRepository =
                RestaurantRepository(
                    FakeApiService(),
                    FakeRoomDao(),
                    dispatcher
                )

            val getSortedRestaurantsUseCase = GetSortedRestaurantsUseCase(restaurantRepository)

            val useCase =
                ToogleRestaurantUserCase(
                    restaurantRepository,
                    getSortedRestaurantsUseCase
                )

            //Preload data
            restaurantRepository.loadRestaurants()

            advanceUntilIdle()

            //Execute useCase
            val restaurants = DummyContent.getDomainRestaurants()
            val targetItem = restaurants[0]
            val isFavorite = targetItem.isFavorite
            val updatedRestaurants =
                useCase(
                    targetItem.id,
                    isFavorite
                )
            advanceUntilIdle()

            //Assertation
            restaurants[0] = targetItem.copy(isFavorite = !isFavorite)
            assert(updatedRestaurants == restaurants)


        }
}