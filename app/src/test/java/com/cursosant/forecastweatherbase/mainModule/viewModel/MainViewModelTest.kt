package com.cursosant.forecastweatherbase.mainModule.viewModel

import com.cursosant.forecastweatherbase.BuildConfig
import com.cursosant.forecastweatherbase.common.dataAccess.WeatherForecastService
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModelTest {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var service: WeatherForecastService

    companion object {
        private lateinit var retrofit : Retrofit

        @BeforeClass
        @JvmStatic
        fun setUpCommon() {
            retrofit = Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

    @Before
    fun setUp() {
        mainViewModel = MainViewModel()
        service = retrofit.create(WeatherForecastService::class.java)
    }

    @Test
    fun checkCurrentWeatherisNotNullTest() {
        runBlocking {
            val result = service.getWeatherForecastByCoordinates(19.4342, -99.1962, BuildConfig.OPEN_WEATHER_KEY,
                "metric", "en")

            assertThat(result.current, `is`(notNullValue()))
        }



    }
}