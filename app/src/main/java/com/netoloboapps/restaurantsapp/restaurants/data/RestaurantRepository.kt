package com.netoloboapps.restaurantsapp.restaurants.data

import com.netoloboapps.restaurantsapp.restaurants.data.di.IoDispatcher
import com.netoloboapps.restaurantsapp.restaurants.data.local.LocalRestaurant
import com.netoloboapps.restaurantsapp.restaurants.data.local.PartialLocalRestaurant
import com.netoloboapps.restaurantsapp.restaurants.data.local.RestaurantsDao
import com.netoloboapps.restaurantsapp.restaurants.data.remote.RestaurantsApiService
import com.netoloboapps.restaurantsapp.restaurants.domain.Restaurant
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RestaurantRepository @Inject constructor(
    private val restInterface: RestaurantsApiService,
    private val restaurantsDao: RestaurantsDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
){


    suspend fun loadRestaurants(){
        return withContext(dispatcher) {
            try {
                refreshCache()
            } catch (e: Exception) {
                when (e) {
                    is UnknownHostException,
                    is ConnectException,
                    is HttpException -> {
                        if (restaurantsDao.getAll().isEmpty())
                            throw Exception("Something went wrong. We have no data")
                    }
                    else -> throw e
                }
            }
        }
    }

    suspend fun getRestaurant(id: Int) : LocalRestaurant = restaurantsDao.getRestaurante(id)



    suspend fun toggleFavoriteRestaurant(id: Int, value: Boolean) =
        withContext(dispatcher) {

            restaurantsDao.update(
                PartialLocalRestaurant(
                    id = id,
                    isFavorite = value
                )
            )



        }

    private suspend fun refreshCache() {
        val remoteRestaurants = restInterface.getRestaurants().map {
            LocalRestaurant(
                it.id,
                it.title,
                it.description,
                isFavorite = false
            )
        }

        val favoriteRestaurants = restaurantsDao.getAllFavorited()
        restaurantsDao.addAll(remoteRestaurants)
        restaurantsDao.updateAll(
            favoriteRestaurants.map {
                PartialLocalRestaurant(it.id,
                    true)
            }
        )
    }

    suspend fun getRestaurants() : List<Restaurant>{
        return withContext(dispatcher){
            return@withContext restaurantsDao.getAll().map {
                Restaurant(
                    it.id,
                    it.title,
                    it.description,
                    it.isFavorite
                )
            }
        }
    }
}