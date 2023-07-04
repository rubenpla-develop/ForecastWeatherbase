package com.cursosant.forecastweatherbase.mainModule.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.cursosant.forecastweatherbase.R
import com.cursosant.forecastweatherbase.common.CommonUtils
import com.cursosant.forecastweatherbase.common.dataAccess.WeatherForecastService
import com.cursosant.forecastweatherbase.databinding.ActivityMainBinding
import com.cursosant.forecastweatherbase.entities.Forecast
import com.cursosant.forecastweatherbase.entities.WeatherForecastEntity
import com.cursosant.forecastweatherbase.mainModule.view.adapters.ForecastAdapter
import com.cursosant.forecastweatherbase.mainModule.view.adapters.OnClickListener
import com.cursosant.forecastweatherbase.mainModule.viewModel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/****
 * Project: Forecast Weatherbase
 * From: com.cursosant.forecastweatherbase.mainModule.view
 * Created by Alain Nicolás Tello on 22/05/23 at 15:09
 * All rights reserved 2023.
 *
 * All my Udemy Courses:
 * https://www.udemy.com/user/alain-nicolas-tello/
 * And Frogames formación:
 * https://cursos.frogamesformacion.com/pages/instructor-alain-nicolas
 *
 * Coupons on my Website:
 * www.alainnicolastello.com
 ***/
class MainActivity : AppCompatActivity() , OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ForecastAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        setupRecyclerView()
        setupViewModel()
    }

    private fun setupAdapter() {
        adapter = ForecastAdapter(this)
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    override fun onStart() {
        super.onStart()
        setupData()
    }

    private fun setupData() {
        lifecycleScope.launch {
            mainViewModel.getWeatherAndForecast(19.4342, -99.1962,
                "6364546cb00c113bff0065ac8aea2438", "metric", "en")
        }
    }
    private fun setupViewModel(){
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mainViewModel.getResult().observe(this){ result ->
            setupUI(result)
        }
    }

    private fun setupUI(data: WeatherForecastEntity) {
        with(binding) {
            tvTimeZone.text = data.timezone.replace("_", " ")
            current.tvTemp.text = getString(R.string.weather_temp, data.current.temp)
            current.tvDt.text = CommonUtils.getFullDate(data.current.dt)
            current.tvHumidity.text = getString(R.string.weather_humidity, data.current.humidity)
            current.tvMain.text = CommonUtils.getWeatherMain(data.current.weather)
            current.tvDescription.text = CommonUtils.getWeatherDescription(data.current.weather)
        }
        adapter.submitList(data.hourly)
    }

    //https://openweathermap.org/api/one-call-api#current
    private suspend fun getHistoricalWeather(): WeatherForecastEntity = withContext(Dispatchers.IO) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        // TODO: 17/12/21 get appId
        val service = retrofit.create(WeatherForecastService::class.java)
        service.getWeatherForecastByCoordinates(19.4342, -99.1962, "",
            "metric", "en")
    }

    override fun onClick(forecast: Forecast) {
        Snackbar.make(binding.root, CommonUtils.getFullDate(forecast.dt), Snackbar.LENGTH_LONG).show()
    }
}