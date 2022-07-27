package com.netoloboapps.restaurantsapp.restaurants.data.local

import androidx.room.*

@Dao
interface RestaurantsDao {

    @Query("SELECT * FROM restaurants")
    suspend fun getAll() : List<LocalRestaurant>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(restaurants: List<LocalRestaurant>)

    @Update(entity = LocalRestaurant::class)
    suspend fun update(partialRestaurant: PartialLocalRestaurant)

    @Update(entity = LocalRestaurant::class)
    suspend fun updateAll(partialRestaurant: List<PartialLocalRestaurant>)

    @Query("SELECT * FROM restaurants WHERE is_favorite = 1")
    suspend fun getAllFavorited() : List<LocalRestaurant>

    @Query("SELECT * FROM restaurants WHERE r_id = :id")
    suspend fun getRestaurante(id: Int) : LocalRestaurant
}