package hr.algebra.android_api_manager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import hr.algebra.android_api_manager.framework.startActivity

class APIManagerReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        context.startActivity<HostActivity>()
    }
}