package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import android.util.Log
import android.widget.TextView
import coil.load
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapp.databinding.ActivityMainBinding
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private lateinit var city: String
    private var humidity by Delegates.notNull<Int>()
    private lateinit var wind: String
    private var temp by Delegates.notNull<Double>()
    private var minTemp_c by Delegates.notNull<Double>()
    private var maxTemp_c by Delegates.notNull<Double>()
    private lateinit var last_updated: String
    private lateinit var local_time: String

    private var pressure_mb by Delegates.notNull<Double>()
    private lateinit var sunrise: String
    private lateinit var sunset: String




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val requestQueue = Volley.newRequestQueue(this)
        binding.searchBtn.setOnClickListener {
            val url =
                "http://api.weatherapi.com/v1/forecast.json?Key=542c36dc84064b1fa22132223230810&q=${binding.address.text}"
            val request = JsonObjectRequest(url, object : Response.Listener<JSONObject> {
                override fun onResponse(response: JSONObject?) {


                    val location = response!!.getJSONObject("location")
                    val forecast = response.getJSONObject("current")
                    val weather_condition = response.getJSONObject("current").getJSONObject("condition")
                    city = location.getString("name")
                    val sun_info = response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("astro")
                    val tempInfo = response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("day")


                    humidity = forecast.getInt("humidity")
                    temp = forecast.getDouble("temp_c")
                    local_time = location.getString("localtime")
                    last_updated = forecast.getString("last_updated")
                    minTemp_c = tempInfo.getDouble("mintemp_c")
                    maxTemp_c = tempInfo.getDouble("maxtemp_c")

                    wind = forecast.getString("wind_mph")
                    pressure_mb = forecast.getDouble("pressure_mb")



                    var h24_rise = sun_info.getString("sunrise").subSequence(0, 2)
                    var m24_rise = sun_info.getString("sunrise").subSequence(3, 5)
                    sunrise = "$h24_rise:$m24_rise"

                    var h24_set = sun_info.getString("sunset").subSequence(0, 2).toString().toInt()+12
                    var m24_set = sun_info.getString("sunset").subSequence(3, 5)
                    sunset = "$h24_set:$m24_set"


                    binding.lastUpdated.text = last_updated
                    binding.localDate.text = local_time
                    binding.temp.text = temp.toString() + "°C"

                    binding.humidity.text = humidity.toString()
                    binding.wind.text = wind
                    binding.sunrise.text = sunrise
                    binding.pressure.text = pressure_mb.toString()
                    binding.sunset.text = sunset
                    binding.tempMax.text = "Max Temp: " + maxTemp_c.toString() + " °C"
                    binding.tempMin.text = "Min Temp: " + minTemp_c.toString() + " °C"


                }
            }, object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    Log.d("Error", "onResponse: $error")
                }
            })

            requestQueue.add(request)

        }






    }
}