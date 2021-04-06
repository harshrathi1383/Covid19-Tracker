package com.example.covid19tracker

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var  customAdapter: CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        list.addHeaderView(LayoutInflater.from(this).inflate(R.layout.item_header,list,false))

        fetchResult()
    }

    private fun fetchResult() {
        GlobalScope.launch {
            val response = withContext(Dispatchers.IO) { Client.api.execute() }
            if(response.isSuccessful) {
                val data = Gson().fromJson(response.body?.string(),Response::class.java)
               // response.body?.string()?.let { Log.i("info", it) }
                withContext(Dispatchers.Main){
                    bindData(data.statewise[0])
                    bindStatewiseData(data.statewise.subList(1,data.statewise.size-1))
                }
            }
        }

    }

    private fun bindStatewiseData(subList: List<StatewiseItem>): Unit {
        customAdapter = CustomAdapter((subList))
        list.adapter = customAdapter
    }

    private fun bindData(statewiseItem: StatewiseItem): Unit {
        val past = statewiseItem.lastupdatedtime
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        lastUpdatedTv.text = "Last Updated\n ${getTimeAgo(simpleDateFormat.parse(past)!!)}"
        confirmedTv.text = statewiseItem.confirmed
        recoveredTv.text =  statewiseItem.recovered
        activeTv.text = statewiseItem.active
        deceasedTv.text = statewiseItem.deaths
    }
    private fun getTimeAgo(past: Date): String{
        val present = Date()
        val seconds = TimeUnit.MILLISECONDS.toSeconds(present.time - past.time)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(present.time - past.time)
        val hours = TimeUnit.MILLISECONDS.toHours(present.time - past.time)
        return when{
            seconds < 60 ->{
                "Few seconds ago"
            }
            minutes < 60 -> {
                "$minutes minutes ago"
            }
            hours < 24 -> {
                "$hours hours ago"
            }
            else -> {
                SimpleDateFormat("dd/MM/yy, hh:mm a ").format(past).toString()
            }
        }
    }
}