package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import com.example.weatherapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//4efbfe7ef6baf274cc7817b2a6e6f65f
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fetchWeatherData("Udaipur")
        SearchCity()
    }

    private fun SearchCity() {

        val searchView=binding.searchView

        searchView.setOnQueryTextListener(object:android.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                if (query != null) {
                    fetchWeatherData(query)
                }

                return true

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }


        })

    }

    private fun fetchWeatherData(cityName:String) {
        val retrofit=Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)

        val response=retrofit.getWeatherData(cityName,"4efbfe7ef6baf274cc7817b2a6e6f65f","metric")

        response.enqueue(object:Callback<WeatherApp>{
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                val responseBody=response.body()

                if(response.isSuccessful && responseBody!=null){

                    val temperature= responseBody.main.temp.toString()

                    val humidity=responseBody.main.humidity

                    val windspeed=responseBody.wind.speed

                    val sunrise=responseBody.sys.sunrise

                    val sunset=responseBody.sys.sunset

                    val sealevel=responseBody.main.pressure

                    val condition=responseBody.weather.firstOrNull()?.main?:"unknown"

                    val maxtemp=responseBody.main.temp_max

                    val mintemp=responseBody.main.temp_min

                    binding.temp.text="$temperature °C"

                    binding.weather.text=condition

                    binding.maxTemp.text="Max Temp:$maxtemp°C"

                    binding.minTemp.text="Min Temp:$mintemp°C"

                    binding.humidity.text="$humidity %"

                    binding.windSpeed.text="$windspeed m/sec"

                    binding.sunRise.text="$sunrise"

                    binding.sunSet.text="$sunset"

                    binding.sea.text="$sealevel hPa"

                    binding.condition.text=condition

                    binding.day.text=dayName(System.currentTimeMillis())

                        binding.date.text=date()

                        binding.cityName.text="$cityName"

                     changeBackground(condition)

                }

            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                TODO("Not yet implemented")
            }


        })



    }

    private fun changeBackground(conditions:String) {

        when(conditions){

            "Clear Sky","Sunny","Clear"->{
                binding.root.setBackgroundResource(R.drawable.sunnyweather)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }

            "Partly Clouds","Clouds","Overcast","Mist","Foggy","Smoke"->{
                binding.root.setBackgroundResource(R.drawable.hazyweather)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }

            "Light Rain","Drizzle","Moderate Rain","Showers","Heavy Rain"->{
                binding.root.setBackgroundResource(R.drawable.rainyweather)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }

            "Light Snow","Moderate Snow","Heavy Snow"->{
                binding.root.setBackgroundResource(R.drawable.coldweather)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }

        }
        binding.lottieAnimationView.playAnimation()

    }

    private fun date(): String {

        val sdf=SimpleDateFormat("dd MMMM yyyy",Locale.getDefault())

        return sdf.format((Date()))

    }

    fun dayName(timestamp: Long):String{

        val sdf=SimpleDateFormat("EEEE",Locale.getDefault())

        return sdf.format((Date()))

    }
}


