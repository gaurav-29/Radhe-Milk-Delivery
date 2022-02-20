package com.example.radhegausala.Utils


import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Toast
import androidx.annotation.StringRes
import com.example.radhegausala.R

private var dialog: Dialog? = null

fun displayToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun displayToast(context: Context, @StringRes message: Int) {
    Toast.makeText(context, context.resources.getString(message), Toast.LENGTH_SHORT).show()
}


fun showLoader(context: Context) {
    dismissLoader()

    dialog = Dialog(context, R.style.ProgressHUD).apply {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setContentView(R.layout.progress_dialog)
        show()
    }
}

fun dismissLoader() {
    if (dialog != null && dialog!!.isShowing) {
        dialog!!.dismiss()
        dialog = null
    }
}