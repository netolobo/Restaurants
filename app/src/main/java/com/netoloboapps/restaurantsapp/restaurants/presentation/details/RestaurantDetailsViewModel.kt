package com.netoloboapps.restaurantsapp.restaurants.presentation.details

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netoloboapps.restaurantsapp.restaurants.data.RestaurantRepository
import com.netoloboapps.restaurantsapp.restaurants.data.local.LocalRestaurant
import com.netoloboapps.restaurantsapp.restaurants.data.remote.RestaurantsApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RestaurantDetailsViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    private val repository: RestaurantRepository,
    private val restInterface: RestaurantsApiService
) : ViewModel() {


    private val _state = mutableStateOf(
        RestaurantDetailsScreenState(
            restaurant = LocalRestaurant(
                id = 0,
                title = "",
                description = "",
                isFavorite = false
            ),
            isLoading = true
        )
    )

    val state: State<RestaurantDetailsScreenState> get() = _state

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()

        _state.value = _state.value.copy(
            error = exception.message,
            isLoading = false
        )
    }

    init {

        Log.d("teste", "Entered in init block")

        val id = stateHandle.get<Int>("restaurant_id") ?: 0

//        getRestaurant(id)

        viewModelScope.launch {
            val restaurant = getRemoteRestaurant(id)
            Log.d("teste", restaurant.toString())
            _state.value = restaurant
        }
    }

    private suspend fun getRemoteRestaurant(id: Int): RestaurantDetailsScreenState {
        return withContext(Dispatchers.IO) {
            val response = restInterface.getRestaurant(id)

            return@withContext response.values.first().let {
                RestaurantDetailsScreenState(
                    restaurant = LocalRestaurant(
                        id = it.id,
                        title = it.title,
                        description = it.description,
                    ),
                    isLoading = false
                )
            }
        }
    }

    private fun getRestaurant(id: Int) {

        viewModelScope.launch(errorHandler) {

            val restaurant = repository.getRestaurant(id)

            _state.value = _state.value.copy(
                restaurant = restaurant,
                isLoading = false
            )
        }
    }


}