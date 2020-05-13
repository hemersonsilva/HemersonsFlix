package br.com.hemersonsilva.hemersonsflix.asynctask

import android.os.AsyncTask

class BaseAsyncTask<T>(
    private val whenStart: () -> T,
    private val whenFinish: (result: T) -> Unit) : AsyncTask<Void, Void, T>() {

    override fun doInBackground(vararg params: Void?) = whenStart()

    override fun onPostExecute(result: T) {
        super.onPostExecute(result)
        whenFinish(result)
    }
}