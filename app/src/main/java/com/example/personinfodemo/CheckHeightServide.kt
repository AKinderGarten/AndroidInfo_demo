package com.example.personinfodemo

import android.app.Service
import android.content.ContentValues
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CheckHeightService : Service() {
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        scope.launch {
            while (true) {
                // 直接使用 URI
                val cursor = contentResolver.query(
                    android.net.Uri.parse("content://com.example.personinfodemo.provider/info"),
                    null, null, null, null
                )

                cursor?.use {
                    while (it.moveToNext()) {
                        val id = it.getInt(0)
                        val h = it.getFloat(3)

                        // 身高不合法：负数 或 超过3米 → 置空
                        if (h < 0 || h > 3) {
                            val values = ContentValues().apply {
                                put("height", 0f)
                            }
                            contentResolver.update(
                                android.net.Uri.parse("content://com.example.personinfodemo.provider/info"),
                                values,
                                "id=?",
                                arrayOf(id.toString())
                            )
                        }
                    }
                }
                delay(5000) // 5秒检查一次
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}