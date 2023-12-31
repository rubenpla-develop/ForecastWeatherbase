package com.cursosant.forecastweatherbase.mainModule.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cursosant.forecastweatherbase.BuildConfig
import com.cursosant.forecastweatherbase.common.dataAccess.WeatherForecastService
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModelTest {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var service: WeatherForecastService

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    /*    @get:Rule
        val mainCoroutinesRule = MainCoroutineRule() */
    @get:Rule
    val mainDispatchersRule = MainDispatcherRule()

    companion object {
        private lateinit var retrofit: Retrofit
        private lateinit var appId: String

        @BeforeClass
        @JvmStatic
        fun setUpCommon() {

            appId = BuildConfig.OPEN_WEATHER_KEY

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
    fun checkCurrentWeatherisNotNullTest() = runTest {
        val result = service.getWeatherForecastByCoordinates(
            19.4342, -99.1962, appId,
            "metric", "en"
        )

        assertThat(result.current, `is`(notNullValue()))
    }

    @Test
    fun checkCurrentTimezoneReturnsMexico() = runTest {
        val result = service.getWeatherForecastByCoordinates(
            19.4342, -99.1962, appId,
            "metric", "en"
        )

        assertThat(result.timezone, `is`("America/Mexico_City"))
    }

    @Test
    fun checkErrorResponseWithOnlyCoordinatesTest() = runTest {
        try {
            val result = service.getWeatherForecastByCoordinates(
                19.4342, -99.1962, "",
                "", ""
            )
        } catch (e: Exception) {
            assertThat(e.localizedMessage, `is`("HTTP 401 Unauthorized"))
        }
    }

    //TODO this test implementation belongs to deprecated Rule 'mainCoroutinesRule' from above
    /*    @Test
        fun checkHourlySizeTest() {
            runBlocking {
               // val observer = Observer<WeatherForecastEntity> {}

                //try {
                   // mainViewModel.getResult().observeForever(observer)
                    mainViewModel.getWeatherAndForecast(19.4342, -99.1962, appId,
                        "metric", "en")

                    val result = mainViewModel.getResult().getOrAwaitValue()
                    assertThat(result.hourly.size, `is`(48))
                //} finally {
                   // mainViewModel.getResult().removeObserver(observer)
                //}
            }
        }  */

    @Test
    fun checkHourlySizeTest() = runTest {
        mainViewModel.getWeatherAndForecast(
            19.4342, -99.1962, appId,
            "metric", "en"
        )

        val result = mainViewModel.getResult().getOrAwaitValue()
        assertThat(result.hourly.size, `is`(48))
    }
}