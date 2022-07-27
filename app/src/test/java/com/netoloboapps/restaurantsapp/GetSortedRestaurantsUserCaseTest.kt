package com.netoloboapps.restaurantsapp

import com.netoloboapps.restaurantsapp.restaurants.DummyContent
import com.netoloboapps.restaurantsapp.restaurants.data.RestaurantRepository
import com.netoloboapps.restaurantsapp.restaurants.domain.GetSortedRestaurantsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class GetSortedRestaurantsUserCaseTest {
    private val dispatcher = StandardTestDispatcher()
    private val scope = TestScope(dispatcher)

    @Test
    fun getSortedRestaurantsUserCase_isLoadingRestaurantsInOrder() = scope.runTest {
        //Setup useCase
        val restaurantRepository = RestaurantRepository(
            FakeApiService(),
            FakeRoomDao(),
            dispatcher
        )

        //Preload data
        val useCase = GetSortedRestaurantsUseCase(restaurantRepository).invoke()


        val targetUseCase = useCase[0]

        advanceUntilIdle()

        //Execute useCase
        val restaurants = DummyContent.getDomainRestaurants().sortedBy { it.title }

        val targetRestaurant = restaurants[0]

        //Assertion
        assert(targetUseCase.title == targetRestaurant.title)
    }
}