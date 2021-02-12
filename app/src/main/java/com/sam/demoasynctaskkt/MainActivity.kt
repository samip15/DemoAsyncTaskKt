package com.sam.demoasynctaskkt

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import okhttp3.OkHttpClient
import okhttp3.Request

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getQuestions().execute()
    }

    internal inner class getQuestions: AsyncTask<Void,Void,String>(){
        lateinit var progressDialog: ProgressDialog
        var hasInternet = false
        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ProgressDialog(this@MainActivity)
            progressDialog.setMessage("Downloading Questions...")
            progressDialog.setCancelable(false)
            progressDialog.show()
        }
        override fun doInBackground(vararg params: Void?): String {
            if (isNetworkAvailable()){
                hasInternet = true
                val client = OkHttpClient()
                val url = "https://api.github.com/users/hadley/orgs"
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                return response.body()?.string().toString()
            }else{
                return ""
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            progressDialog.dismiss()
            if (hasInternet){
                val tvResult = findViewById<TextView>(R.id.tv_result)
                tvResult.text = result
            }else{
                val tvResult = findViewById<TextView>(R.id.tv_result)
                tvResult.text = "No Internet"
            }
        }

    }

    private fun isNetworkAvailable(): Boolean{
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}