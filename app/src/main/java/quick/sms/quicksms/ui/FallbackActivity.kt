package quick.sms.quicksms.ui

import android.content.Intent
import android.os.Bundle
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import quick.sms.quicksms.BaseActivity
import quick.sms.quicksms.R

class FallbackActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val exception = intent.extras.get("exception") as Throwable
        FallbackLayout(::restart, ::resetApp, { sendReport(exception) }).setContentView(this)
    }

    private fun restart() {
        val i = baseContext.packageManager
                .getLaunchIntentForPackage(baseContext.packageName)
        i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(i)
    }

    private fun sendReport(exception: Throwable) {
        startActivity<BugReportActivity>("exception" to exception)
    }
}

private class FallbackLayout(val restart: () -> Unit, val reset: () -> Unit, val send: () -> Unit)
    : AnkoComponent<FallbackActivity> {

    override fun createView(ui: AnkoContext<FallbackActivity>) = with(ui) {
        verticalLayout {
            textView(R.string.fallback_text)
            button(R.string.restart_app) {
                onClick { restart() }
            }
            button(R.string.reset_app) {
                onClick { reset() }
            }
            button(R.string.send_bug_report) {
                onClick { send() }
            }
        }
    }
}