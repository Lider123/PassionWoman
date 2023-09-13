package ru.babaetskv.passionwoman.app.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.babaetskv.passionwoman.domain.usecase.UpdateOrderStatusUseCase
import java.lang.Exception

class OrderStatusUpdateService : Service() {
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.IO)
    private val updateOrderStatusUseCase: UpdateOrderStatusUseCase by inject()

    @Deprecated("Deprecated in Java")
    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
        scope.launch {
            val orderId = intent?.extras?.getLong(EXTRA_ORDER_ID, -1)
                ?.takeIf { it >= 0 }
                ?: run {
                    stopSelf()
                    return@launch
                }

            updateOrderStatus(orderId)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private suspend fun updateOrderStatus(orderId: Long) {
        delay(UPDATE_DELAY)
        try {
            val updated = updateOrderStatusUseCase.execute(orderId)
            if (updated) {
                updateOrderStatus(orderId)
            } else {
                stopSelf()
            }
        } catch (e: Exception) {
            stopSelf()
        }
    }

    companion object {
        private const val EXTRA_ORDER_ID = "extra_order_id"
        private const val UPDATE_DELAY = 20_000L

        fun start(context: Context, orderId: Long) {
            val intent = Intent(context, OrderStatusUpdateService::class.java).apply {
                putExtra(EXTRA_ORDER_ID, orderId)
            }
            context.startService(intent)
        }
    }
}
