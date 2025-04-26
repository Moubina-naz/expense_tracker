package com.example.expensetracker

import android.app.Application
import com.example.expensetracker.Room.Graph

class TransactionApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }

}