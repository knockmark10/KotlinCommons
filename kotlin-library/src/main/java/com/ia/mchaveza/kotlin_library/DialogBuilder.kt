package com.ia.mchaveza.kotlin_library

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Toast

/**
 * Created by mchaveza on 19/12/2017.
 */
class DialogBuilder(context: Context) {

    val mContext = context

    fun buildOneButtonDialog(
            title: String,
            message: String,
            positiveButton: String,
            action: SimpleAlertAction
    ): AlertDialog.Builder {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(mContext)
        alertDialogBuilder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButton) { dialog, which -> action.onAccept(dialog, which) }
                .create()
                .show()
        return alertDialogBuilder
    }

    fun buildTwoButtonDialog(title: String,
                             message: String,
                             positiveButton: String,
                             negativeButton: String,
                             action: AlertAction): AlertDialog.Builder {
        val alertDialogBuilder = AlertDialog.Builder(mContext)
        alertDialogBuilder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButton) { dialog: DialogInterface, which: Int ->
                    action.onAccept(dialog, which)
                }
                .setNegativeButton(negativeButton) { dialog, which ->
                    action.onDeny(dialog, which)
                }
                .create()
                .show()
        return alertDialogBuilder
    }

    fun buildToast(message: String, length: Int) = Toast.makeText(mContext, message, length).show()

    fun buildSnackBar(message: String, view: View) = Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()

    interface SimpleAlertAction {
        fun onAccept(dialog: DialogInterface, id: Int)
    }

    interface AlertAction {
        fun onAccept(dialog: DialogInterface, id: Int)

        fun onDeny(dialog: DialogInterface, id: Int)
    }


}