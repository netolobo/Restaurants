package com.netoloboapps.restaurantsapp

import RestaurantScreen
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.netoloboapps.restaurantsapp.restaurants.DummyContent
import com.netoloboapps.restaurantsapp.restaurants.presentation.Description
import com.netoloboapps.restaurantsapp.restaurants.presentation.list.RestaurantsScreenState
import com.netoloboapps.restaurantsapp.ui.theme.RestaurantsTheme
import org.junit.Rule
import org.junit.Test

class RestaurantsScreenTest {

    @get: Rule
    val testRule = createComposeRule()

    @Test
    fun initialState_isRendered(){
        testRule.setContent {

            RestaurantsTheme {
                RestaurantScreen(
                    state = RestaurantsScreenState(
                        restaurants = emptyList(),
                        isLoading = true
                    ),
                    onFavoriteClick = {_, _-> },
                    onItemClick = {}
                )

            }

        }

        testRule.onNodeWithContentDescription(
            Description.RESTAURANTS_LOADING
        ).assertIsDisplayed()
    }

    @Test
    fun stateWithContent_isRendered(){

        val restaurants = DummyContent.getDomainRestaurants()

            testRule.setContent {
                RestaurantsTheme {
                    RestaurantScreen(
                        state = RestaurantsScreenState(
                            restaurants = restaurants,
                            isLoading = false,

                        ),
                        onFavoriteClick = {_, _ -> },
                        onItemClick = {}
                    )
                }
            }

            testRule.onNodeWithText(restaurants[0].title).assertIsDisplayed()

            testRule.onNodeWithText(restaurants[0].description).assertIsDisplayed()

            testRule.onNodeWithContentDescription(
                Description.RESTAURANTS_LOADING
            ).assertDoesNotExist()

    }

    @Test
    fun stateWithError_isRendered(){

        testRule.setContent {
            RestaurantsTheme {
                RestaurantScreen(
                    state = RestaurantsScreenState(
                        restaurants = emptyList(),
                        isLoading = false,
                        error = "fatal error"

                        ),
                    onFavoriteClick = {_, _ -> },
                    onItemClick = {}
                )
            }
        }

        testRule.onNodeWithText("fatal error").assertIsDisplayed()

        testRule.onNodeWithContentDescription(
            Description.RESTAURANTS_LOADING
        ).assertDoesNotExist()
    }

    @Test
    fun stateWithContent_ClickOnItem_isRegistered(){

        val restaurants = DummyContent.getDomainRestaurants()
        val targetRestaurant = restaurants[0]

        testRule.setContent {
            RestaurantsTheme {
                RestaurantScreen(
                    state = RestaurantsScreenState(
                        restaurants = restaurants,
                        isLoading = false,

                    ),
                    onFavoriteClick = {_, _ -> },
                    onItemClick = {id ->
                        assert(id == targetRestaurant.id)
                    }
                )
            }
        }

        testRule.onNodeWithText(targetRestaurant.title).performClick()

    }
}