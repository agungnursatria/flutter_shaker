package com.example.flutter_shaker

import android.os.Bundle
import android.util.Log

import io.flutter.app.FlutterActivity
import io.flutter.plugins.GeneratedPluginRegistrant
import io.flutter.plugin.common.EventChannel

class MainActivity: FlutterActivity() {
  private val SHAKE_CHANNEL = "com.payfazz.Fazzcard/shakeDebug"

  lateinit var shakeListener: ShakeListener

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    GeneratedPluginRegistrant.registerWith(this)

    shakeListener = ShakeListener(this)

    EventChannel(flutterView, SHAKE_CHANNEL).setStreamHandler(
            object : EventChannel.StreamHandler {
              override fun onListen(p0: Any?, p1: EventChannel.EventSink?) {
                shakeListener.setOnShakeListener(object : ShakeListener.OnShakeListener {
                  override fun onShake() {
                    p1?.success("Shake!")
                    Log.d("MainActivity", "Shake!")
                  }
                })
              }

              override fun onCancel(p0: Any?) {
                Log.d("MainActivity", "Canceling")
              }
            }
    )
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
