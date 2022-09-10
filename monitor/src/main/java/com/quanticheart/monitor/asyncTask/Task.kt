package com.quanticheart.monitor.asyncTask

import android.content.Context
import android.os.AsyncTask

fun <T> Context.startTask(listener: TaskListener<T>) {
    TaskDetails(listener).execute(this)
}

interface TaskListener<T> {
    fun background(context: Context): T
    fun foreground(data: T)
}

internal class TaskDetails<T>(private val listener: TaskListener<T>) :
    AsyncTask<Context, Unit, T>() {

    override fun doInBackground(vararg context: Context): T {
        return listener.background(context.first())
    }

    override fun onPostExecute(result: T) {
        super.onPostExecute(result)
        listener.foreground(result)
    }
}