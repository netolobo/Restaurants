package com.netoloboapps.restaurantsapp

import com.netoloboapps.restaurantsapp.restaurants.data.local.LocalRestaurant
import com.netoloboapps.restaurantsapp.restaurants.data.local.PartialLocalRestaurant
import com.netoloboapps.restaurantsapp.restaurants.data.local.RestaurantsDao
import kotlinx.coroutines.delay

class FakeRoomDao : RestaurantsDao {

    private val restaurants = HashMap<Int, LocalRestaurant>()

    override suspend fun getAll(): List<LocalRestaurant> {
        return restaurants.values.toList()
    }

    override suspend fun addAll(restaurants: List<LocalRestaurant>) {
        restaurants.forEach {
            this.restaurants[it.id] = it
        }
    }

    override suspend fun update(partialRestaurant: PartialLocalRestaurant) {
        delay(1000)
        updateRestaurant(partialRestaurant)
    }

    override suspend fun updateAll(partialRestaurant: List<PartialLocalRestaurant>) {
        delay(1000)
        partialRestaurant.forEach {
            updateRestaurant(it)
        }
    }

    override suspend fun getAllFavorited(): List<LocalRestaurant> {
        return restaurants.values.toList().filter { it.isFavorite }
    }

    override suspend fun getRestaurante(id: Int): LocalRestaurant {
        TODO("Not yet implemented")
    }

    private fun updateRestaurant(partialRestaurant: PartialLocalRestaurant){
        val restaurant = this.restaurants[partialRestaurant.id]

        if(restaurant != null){
            this.restaurants[partialRestaurant.id] = restaurant.copy(
                isFavorite = partialRestaurant.isFavorite
            )
        }
    }
}