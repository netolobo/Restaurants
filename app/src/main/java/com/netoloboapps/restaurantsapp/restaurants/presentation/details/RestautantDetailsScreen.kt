package com.netoloboapps.restaurantsapp.restaurants.presentation.details

import RestaurantDetails
import RestaurantIcon
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.netoloboapps.restaurantsapp.restaurants.presentation.details.RestaurantDetailsViewModel
import androidx.compose.ui.unit.dp as dp

@Composable
fun RestaurantDetailsScreen(state: RestaurantDetailsScreenState) {


    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            RestaurantIcon(
                icon = Icons.Filled.Place,
                modifier = Modifier.padding(
                    top = 32.dp,
                    bottom = 32.dp
                )
            )
            RestaurantDetails(
                title = state.restaurant.title,
                description = state.restaurant.description,
                modifier = Modifier.padding(
                    bottom = 32.dp
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
            )

            Text("More info coming soon!")
        }
    }

    if (state.isLoading) {
        CircularProgressIndicator()
    }
    if (state.error != null) {
        Text(text = state.error)
    }


}