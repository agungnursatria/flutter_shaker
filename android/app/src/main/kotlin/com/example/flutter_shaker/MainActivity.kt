package com.example.flutter_shaker

import android.os.Bundle
import android.util.Log

import io.flutter.app.FlutterActivity
import io.flutter.plugins.GeneratedPluginRegistrant

class MainActivity: FlutterActivity(), ShakeListener.OnShakeListener {

  override fun onShake(count: Int) {
    Log.d("MainActivity", count.toString())
  }

  lateinit var shakeListener: ShakeListener

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    GeneratedPluginRegistrant.registerWith(this)

    shakeListener = ShakeListener(this)
    shakeListener.setOnShakeListener(this)
  }

  override fun onResume() {
    shakeListener.resume()
    super.onResume()
  }

  override fun onPause() {
    shakeListener.onPause()
    super.onPause()
  }
}
