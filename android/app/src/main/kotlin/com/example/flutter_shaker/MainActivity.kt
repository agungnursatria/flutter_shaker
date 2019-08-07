package com.example.flutter_shaker

import android.os.Bundle
import android.util.Log

import io.flutter.app.FlutterActivity
import io.flutter.plugins.GeneratedPluginRegistrant
import io.flutter.plugin.common.EventChannel

class MainActivity: FlutterActivity(), ShakeListener.OnShakeListener {
  private val SHAKE_CHANNEL = "com.payfazz.Fazzcard/shakeDebug"
  
  override fun onShake() {
    Log.d("MainActivity", "Shake shake shake!")
    EventChannel(flutterView, SHAKE_CHANNEL).setStreamHandler(
                  object : EventChannel.StreamHandler {
                    override fun onListen(args: Any, events: EventChannel.EventSink) {
                      events.success(1)
                    }
                    override fun onCancel(args: Any) {
                      Log.d("MainActivity", "cancel")
                      }
                  }
          )

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
