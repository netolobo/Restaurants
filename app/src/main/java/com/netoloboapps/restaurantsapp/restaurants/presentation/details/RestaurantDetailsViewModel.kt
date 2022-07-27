package com.netoloboapps.restaurantsapp.restaurants.presentation.details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netoloboapps.restaurantsapp.*
import com.netoloboapps.restaurantsapp.restaurants.data.RestaurantRepository
import com.netoloboapps.restaurantsapp.restaurants.data.local.LocalRestaurant
import com.netoloboapps.restaurantsapp.restaurants.data.remote.RestaurantsApiService
import com.netoloboapps.restaurantsapp.restaurants.domain.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
            isFavorite = false),
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

        val id = stateHandle.get<Int>("restaurant_id") ?: 0

//        getRestaurant(id)

        viewModelScope.launch {
            val restaurant = getRemoteRestaurant(id)
            _state.value = restaurant
        }
    }

    private suspend fun getRemoteRestaurant(id: Int) : RestaurantDetailsScreenState {
        return withContext(Dispatchers.IO){
            val response = restInterface.getRestaurant(id)

            return@withContext response.values.first().let{
                RestaurantDetailsScreenState(
                    restaurant = LocalRestaurant( id = it.id,
                    title = it.title,
                    description = it.description,
                    ),
                isLoading = false
                )
            }
        }
    }

    private fun getRestaurant(id: Int){

        viewModelScope.launch(errorHandler) {

            val restaurant = repository.getRestaurant(id)

            _state.value = _state.value.copy(
                restaurant = restaurant,
                isLoading = false
            )
        }
    }


}