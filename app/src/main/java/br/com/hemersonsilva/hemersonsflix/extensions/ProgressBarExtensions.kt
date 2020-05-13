package br.com.hemersonsilva.hemersonsflix.extensions

import android.view.View
import android.widget.ProgressBar

fun showProgress(progressBar: ProgressBar) {
    progressBar.visibility = View.VISIBLE
}

fun hideProgressBar(progressBar: ProgressBar) {
    progressBar.visibility = View.GONE
}