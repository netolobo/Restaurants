package com.netoloboapps.restaurantsapp

import com.netoloboapps.restaurantsapp.restaurants.DummyContent
import com.netoloboapps.restaurantsapp.restaurants.data.remote.RemoteRestaurant
import com.netoloboapps.restaurantsapp.restaurants.data.remote.RestaurantsApiService
import kotlinx.coroutines.delay
import java.lang.Exception

class FakeApiService() : RestaurantsApiService {

    override suspend fun getRestaurants(): List<RemoteRestaurant> {
        delay(1000)

        return DummyContent.getRemoteRestaurants()
    }

    override suspend fun getRestaurant(id: Int): Map<String, RemoteRestaurant> {
        TODO("Not yet implemented")
    }
}