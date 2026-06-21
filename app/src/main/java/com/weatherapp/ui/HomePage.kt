package com.weatherapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.weatherapp.R
import com.weatherapp.model.MainViewModel
import com.weatherapp.model.Weather

@Composable
fun HomePage(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    val cityName = viewModel.city ?: viewModel.cities.firstOrNull()?.name

    if (cityName == null) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(32.dp))
            Text(text = "Nenhuma cidade adicionada", fontSize = 18.sp)
        }
        return
    }

    val weather = viewModel.weather(cityName)

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = weather.imgUrl,
                modifier = modifier.size(140.dp),
                error = painterResource(id = R.drawable.loading),
                contentDescription = "Imagem"
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = cityName, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = weather.desc, fontSize = 16.sp)
                Text(text = "${weather.temp}°C", fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Text(text = weather.date, fontSize = 12.sp)
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
        Text(text = "Previsão", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.size(8.dp))
        LazyRow {
            items(viewModel.cities) { city ->
                ForecastItem(forecast = viewModel.weather(city.name), label = city.name)
            }
        }
    }
}

@Composable
fun ForecastItem(
    forecast: Weather,
    label: String = "",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, fontSize = 12.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = forecast.imgUrl,
                modifier = modifier.size(70.dp),
                error = painterResource(id = R.drawable.loading),
                contentDescription = "Imagem"
            )
        }
        Text(text = "${forecast.temp}°C", fontSize = 14.sp)
        Text(text = forecast.desc, fontSize = 12.sp)
    }
}
