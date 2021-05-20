package com.example.myapplication

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    val CITY: String = "Madurai,IN"
            val API: String = "8118ed6ee68db2debfaaa5a44c832918" // Use your own API key

            override fun onCreate(savedInstanceState: Bundle?)
            {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_main)
                weatherTask().execute()

            }

            @SuppressLint("StaticFieldLeak")
            inner class weatherTask() : AsyncTask<String, Void, String>() {
                override fun onPreExecute() {
                    super.onPreExecute()
                    /* Showing the ProgressBar, Making the main design GONE */
                    findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
                    findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
                    findViewById<TextView>(R.id.errorText).visibility = View.GONE

                }

                override fun doInBackground(vararg params: String?): String?
                {
                    var response:String?
                    try{
                        response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API").readText(
                            Charsets.UTF_8
                        )
                    }catch (e: Exception){
                        response = null
                    }
                    return response
                }

                override fun onPostExecute(response1: String?) {
                    super.onPostExecute(response1)
                    try {
                        /* Extracting JSON returns from the API */
                        val jsonObj = JSONObject(response1)
                        val main = jsonObj.getJSONObject("main")
                        val system = jsonObj.getJSONObject("sys")
                        val wind_type = jsonObj.getJSONObject("wind")
                        val weather = jsonObj.getJSONArray("weather").getJSONObject(0)

                        val updatedAt:Long = jsonObj.getLong("dt")
                        val updatedAtText = "Updated at: "+ SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedAt*1000))
                        val temp = main.getString("temp")+"°C"
                        val tempMin = "Min Temp: " + main.getString("temp_min")+"°C"
                        val tempMax = "Max Temp: " + main.getString("temp_max")+"°C"
                        val pressure = main.getString("pressure")
                        val humidity = main.getString("humidity")

                        val sunrise:Long = system.getLong("sunrise")
                        val sunset:Long = system.getLong("sunset")
                        val windSpeed = wind_type.getString("speed")
                        val weatherDescription = weather.getString("description")

                        val address = jsonObj.getString("name")+", "+system.getString("country")

                        /* Populating extracted data into our views */
                        findViewById<TextView>(R.id.address).text = address
                        findViewById<TextView>(R.id.updated_at).text =  updatedAtText
                        findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()
                        findViewById<TextView>(R.id.temp).text = temp
                        findViewById<TextView>(R.id.temp_min).text = tempMin
                        findViewById<TextView>(R.id.temp_max).text = tempMax
                        findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))
                        findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))
                        findViewById<TextView>(R.id.wind).text = windSpeed
                        findViewById<TextView>(R.id.pressure).text = pressure
                        findViewById<TextView>(R.id.humidity).text = humidity

                        /* Views populated, Hiding the loader, Showing the main design */
                        findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                        findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE

                    } catch (e: Exception) {
                        findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                        findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
                    }

                }
            }
        }

