package com.weatherapp.ui

import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import androidx.compose.runtime.key
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.weatherapp.R
import com.weatherapp.model.MainViewModel
import com.weatherapp.model.Weather

@Composable
fun MapPage(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    val camPosState = rememberCameraPositionState()

    val context = LocalContext.current
    val hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    Column(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = camPosState,
            onMapClick = {
                viewModel.addCity(it)
            },
            properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
            uiSettings = MapUiSettings(myLocationButtonEnabled = true)
        ) {
            val cities = viewModel.cities.collectAsStateWithLifecycle(emptyMap()).value
            val weatherMap = viewModel.weather.collectAsStateWithLifecycle(emptyMap()).value

            cities.values.forEach { city ->
                val location = city.location
                if (location != null) {
                    val weather = weatherMap[city.name] ?: Weather.LOADING

                    LaunchedEffect(city.name) {
                        viewModel.loadWeather(city.name)
                    }

                    LaunchedEffect(weather) {
                        viewModel.loadBitmap(city.name)
                    }

                    key(city.name) {
                        val image = weather.bitmap
                            ?: ContextCompat.getDrawable(context, R.drawable.loading)!!.toBitmap()

                        val marker = BitmapDescriptorFactory.fromBitmap(
                            Bitmap.createScaledBitmap(image, 120, 120, true)
                        )

                        val desc = if (weather == Weather.LOADING) "Carregando clima..." else weather.desc

                        Marker(
                            state = rememberMarkerState(position = location),
                            icon = marker,
                            title = city.name,
                            snippet = desc
                        )
                    }
                }
            }
        }
    }
}
