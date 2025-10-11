package com.example.expensetracker

import android.app.Application
import android.util.Log
import com.example.expensetracker.data.Room.Graph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)

    }

}