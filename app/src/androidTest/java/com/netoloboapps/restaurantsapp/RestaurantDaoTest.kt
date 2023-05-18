package com.netoloboapps.restaurantsapp

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.netoloboapps.restaurantsapp.restaurants.data.local.LocalRestaurant
import com.netoloboapps.restaurantsapp.restaurants.data.local.RestaurantsDb
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RestaurantDaoTest {
    private lateinit var database: RestaurantsDb

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            RestaurantsDb::class.java
        ).allowMainThreadQueries().build()
    }

    // Given: An empty database
    // When: A restaurant is inserted and we start observing the restaurants stream
    // Then: The first item in the restaurants stream matches the restaurant which was inserted

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertRestaurantAndGetRestaurants() = runTest {
        val restaurant = LocalRestaurant(
            id = 0,
            title = "Teste",
            description = "testing",
            isFavorite = false
        )

        database.dao.addAll(arrayListOf(restaurant))

        val restaurants = database.dao.getAll()

        assertEquals(1, restaurants.size)
        assertEquals(restaurant, restaurants[0])
    }
}