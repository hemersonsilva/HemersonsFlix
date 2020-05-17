package br.com.hemersonsilva.hemersonsflix.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import br.com.hemersonsilva.hemersonsflix.R

class SplashActivity : AppCompatActivity(), Runnable {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setDelay()

    }

    private fun setDelay() {
        val handler = Handler()
        handler.postDelayed(this, 2000)
    }

    override fun run() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
