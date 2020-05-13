package br.com.hemersonsilva.hemersonsflix.retrofit.service

import br.com.hemersonsilva.hemersonsflix.model.MovieResult

interface OnResult {
    fun onSuccess(result: MovieResult?)
    fun onFailure(error: String?)
    fun onComplete()
}