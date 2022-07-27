package com.netoloboapps.restaurantsapp

import com.netoloboapps.restaurantsapp.restaurants.DummyContent
import com.netoloboapps.restaurantsapp.restaurants.data.RestaurantRepository
import com.netoloboapps.restaurantsapp.restaurants.domain.GetInitialRestaurantsUserCase
import com.netoloboapps.restaurantsapp.restaurants.domain.GetSortedRestaurantsUseCase
import com.netoloboapps.restaurantsapp.restaurants.domain.ToogleRestaurantUserCase
import com.netoloboapps.restaurantsapp.restaurants.presentation.list.RestaurantsScreenState
import com.netoloboapps.restaurantsapp.restaurants.presentation.list.RestaurantsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class RestaurantsViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val scope = TestScope(dispatcher)

    @Test
    fun initialState_IsProduced() =

        scope.runTest{
            val viewModel = getViewModel()

            val initialState = viewModel.state.value

            assert(
                initialState == RestaurantsScreenState(
                    restaurants = emptyList(),
                    isLoading = true,
                    error = null
                )
            )
        }

    @Test
    fun stateWithContent_isProduced() =

        scope.runTest {
            val testVM = getViewModel()

            advanceUntilIdle()

            val currentState = testVM.state.value

            assert(
                currentState == RestaurantsScreenState(
                    restaurants = DummyContent.getDomainRestaurants(),
                    isLoading = false,
                    error = null
                )
            )
        }




    private fun getViewModel() : RestaurantsViewModel{
        val restaurantsRepository = RestaurantRepository(
            FakeApiService(),
            FakeRoomDao(),
            dispatcher
        )

        val getSortedRestaurantsUseCase = GetSortedRestaurantsUseCase(restaurantsRepository)

        val getInitialRestaurantsUserCase = GetInitialRestaurantsUserCase(
            restaurantsRepository,
            getSortedRestaurantsUseCase
        )

        val toggleRestaurantsUseCase = ToogleRestaurantUserCase(
            restaurantsRepository,
            getSortedRestaurantsUseCase
        )

        return RestaurantsViewModel(
            getInitialRestaurantsUserCase,
            toggleRestaurantsUseCase,
            dispatcher
        )
    }
}