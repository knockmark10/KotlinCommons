package com.ia.mchaveza.kotlin_library

import android.hardware.fingerprint.FingerprintManager
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import rx.Completable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.lang.NullPointerException
import java.util.concurrent.TimeUnit

class FingerPrintDialog : DialogFragment(), FingerPrintUtils.FingerPrintAuthCallback {

    private var tvTitle: TextView? = null
    private var tvMessage: TextView? = null
    private var tvStatus: TextView? = null
    private var ivFingerPrint: ImageView? = null
    private var btnCancel: TextView? = null
    private var btnChange: TextView? = null
    private var icSuccess: Int = R.drawable.ic_success
    private var icError: Int = R.drawable.ic_error
    private var mListener: FingerPrintDialogCallback? = null
    private var statusMessage: String = ""

    private lateinit var fingerPrintManager: FingerPrintUtils

    companion object {
        const val ERROR_TIMEOUT_MILLIS = 1600L
        const val SUCCESS_DELAY_MILLIS = 1300L
    }

    init {
        isCancelable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dialogView = LayoutInflater.from(activity!!).inflate(R.layout.dialog_fingerprint, null)
        tvTitle = dialogView.findViewById(R.id.fingerprint_title)
        tvMessage = dialogView.findViewById(R.id.fingerprint_instructions)
        tvStatus = dialogView.findViewById(R.id.fingerprint_status)
        ivFingerPrint = dialogView.findViewById(R.id.fingerprint_icon)
        btnCancel = dialogView.findViewById(R.id.fingerprint_cancel)
        btnChange = dialogView.findViewById(R.id.fingerprint_change_method)
        return dialogView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkListener()
        statusMessage = getString(R.string.fingerprint_default_status)
        btnCancel?.setOnClickListener(cancelAction)
        btnChange?.setOnClickListener(changeAction)
        mListener?.onSetupDialog()
        fingerPrintManager = FingerPrintUtils(activity!!, mAuthListener = this)
        fingerPrintManager.startAuthProcess()
    }

    private fun restoreState() {
        Completable.timer(ERROR_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    tvStatus?.text = statusMessage
                    tvStatus?.setTextColor(ContextCompat.getColor(activity!!, R.color.md_grey_400))
                    ivFingerPrint?.setImageResource(R.drawable.ic_sensor_fingerprint)
                    fingerPrintManager.startAuthProcess()
                }
    }

    private fun setError(error: String, isRecoverable: Boolean) {
        tvStatus?.text = error
        tvStatus?.setTextColor(ContextCompat.getColor(activity!!, R.color.md_red_700))
        ivFingerPrint?.setImageResource(icError)
        if (isRecoverable) {
            restoreState()
        } else {
            Completable.timer(ERROR_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        fingerPrintManager.stopAuth()
                        dismiss()
                        mListener?.onChangeMethod()
                    }
        }
    }

    private val cancelAction = View.OnClickListener {
        fingerPrintManager.stopAuth()
        mListener?.onDialogClosed()
        dismiss()
    }

    private val changeAction = View.OnClickListener {
        fingerPrintManager.stopAuth()
        mListener?.onChangeMethod()
        dismiss()
    }

    override fun onAuthProcessFailed(error: Throwable) {
        setError(error.message.toString(), false)
    }

    override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
        setError(errString.toString(), true)
    }

    override fun onAuthenticationFailed() {
        setError(getString(R.string.fingerprint_auth_failed), true)
    }

    override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
        setError(helpString.toString(), true)
    }

    override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult?) {
        tvStatus?.text = getString(R.string.fingerprint_auth_succeeded)
        tvStatus?.setTextColor(ContextCompat.getColor(activity!!, R.color.md_green_700))
        ivFingerPrint?.setImageResource(icSuccess)
        Completable.timer(SUCCESS_DELAY_MILLIS, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    dismiss()
                    mListener?.onAuthenticationSucceeded()
                }
    }

    override fun onTooManyAttempts(errorCode: Int, errString: CharSequence?) {
        setError(errString.toString(), false)
    }

    private fun checkListener() {
        if (mListener == null) {
            throw NullPointerException("FingerPrintDialogCallback interface required. You need to call setListener method.")
        }
    }

    /**
     * Specifies whether or not the title will be displayed
     */
    fun titleEnabled(enabled: Boolean): FingerPrintDialog {
        if (enabled) {
            tvTitle?.visible()
        } else {
            tvTitle?.gone()
        }
        return this
    }

    /**
     * Set the title for your dialog
     */
    fun title(title: String): FingerPrintDialog {
        if (title.isNotEmpty()) {
            tvTitle?.text = title
        }
        return this
    }

    /**
     * Set the message to point out what's about to happen
     */
    fun message(message: String): FingerPrintDialog {
        if (message.isNotEmpty()) {
            tvMessage?.text = message
        }
        return this
    }

    /**
     * Set the message for the cancel button
     */
    fun cancelButton(buttonMessage: String): FingerPrintDialog {
        if (buttonMessage.isNotEmpty()) {
            btnCancel?.text = buttonMessage
        }
        return this
    }

    /**
     * If empty, change method button is enabled with default message
     */
    fun changeMethodButton(buttonMessage: String = ""): FingerPrintDialog {
        btnChange?.visible()
        if (buttonMessage.isNotEmpty()) {
            btnChange?.text = buttonMessage
        }
        return this
    }

    /**
     * Pick the success icon
     */
    fun successIcon(@DrawableRes resId: Int): FingerPrintDialog {
        icSuccess = resId
        return this
    }

    /**
     * Change error icon
     */
    fun errorIcon(@DrawableRes resId: Int): FingerPrintDialog {
        icError = resId
        return this
    }

    /**
     * Set the listener to handle incoming messages
     */
    fun setListener(listener: FingerPrintDialogCallback) {
        mListener = listener
    }

    /**
     * Set the default message
     */
    fun defaultStatusMessage(statusMessage: String): FingerPrintDialog {
        if (statusMessage.isNotEmpty()) {
            this.statusMessage = statusMessage
            tvStatus?.text = this.statusMessage
        }
        return this
    }

    /**
     * Set the buttons color
     */
    fun buttonsColor(@ColorRes resId: Int) {
        btnChange?.setTextColor(ContextCompat.getColor(activity!!, resId))
        btnCancel?.setTextColor(ContextCompat.getColor(activity!!, resId))
    }


    interface FingerPrintDialogCallback {
        fun onAuthenticationSucceeded()
        fun onChangeMethod()
        fun onDialogClosed()
        fun onSetupDialog()
    }

}