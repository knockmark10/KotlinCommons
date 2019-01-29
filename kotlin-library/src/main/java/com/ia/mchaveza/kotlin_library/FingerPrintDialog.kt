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
import java.util.concurrent.TimeUnit

class FingerPrintDialog
@Deprecated("Deprecated in favor of builder design pattern. Call Builder to set this property.")
constructor() : DialogFragment(), FingerPrintUtils.FingerPrintAuthCallback {

    private var tvTitle: TextView? = null
    private var tvMessage: TextView? = null
    private var tvStatus: TextView? = null
    private var ivFingerPrint: ImageView? = null
    private var btnCancel: TextView? = null
    private var btnChange: TextView? = null

    private var titleEnabled: Boolean = true
    private var title: String = "Autenticación"
    private var errorIcon: Int = R.drawable.ic_error
    private var cancelBtnMessage: String = "Cancelar"
    private var statusMessage: String = "Toca sensor"
    private var enableChangeMethodBtn: Boolean = true
    private var buttonsColor: Int = R.color.md_red_700
    private var successIcon: Int = R.drawable.ic_success
    private var message: String = "Coloca tu huella para autenticarte"
    private var mAuthenticationListener: FingerPrintDialogCallback? = null

    private lateinit var fingerPrintManager: FingerPrintUtils

    companion object {
        const val ERROR_TIMEOUT_MILLIS = 1600L
        const val SUCCESS_DELAY_MILLIS = 1300L
    }

    init {
        isCancelable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dialogView = container?.inflate(R.layout.dialog_fingerprint)
        tvTitle = dialogView?.findViewById(R.id.fingerprint_title)
        tvMessage = dialogView?.findViewById(R.id.fingerprint_instructions)
        tvStatus = dialogView?.findViewById(R.id.fingerprint_status)
        ivFingerPrint = dialogView?.findViewById(R.id.fingerprint_icon)
        btnCancel = dialogView?.findViewById(R.id.fingerprint_cancel)
        btnChange = dialogView?.findViewById(R.id.fingerprint_change_method)
        return dialogView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDialog()
        fingerPrintManager = FingerPrintUtils(activity!!, mAuthListener = this)
        fingerPrintManager.startAuthProcess()
    }

    private fun setupDialog() {
        this.tvTitle?.apply {
            text = title
            if (titleEnabled) {
                visible()
            } else {
                gone()
            }
        }
        this.tvMessage?.text = message
        this.tvStatus?.text = statusMessage
        this.btnCancel?.apply {
            setTextColor(ContextCompat.getColor(activity!!, buttonsColor))
            setOnClickListener(cancelAction)
        }
        this.btnChange?.apply {
            setTextColor(ContextCompat.getColor(activity!!, buttonsColor))
            setOnClickListener(changeAction)
        }
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
        ivFingerPrint?.setImageResource(errorIcon)
        if (isRecoverable) {
            restoreState()
        } else {
            Completable.timer(ERROR_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        fingerPrintManager.stopAuth()
                        dismiss()
                        mAuthenticationListener?.onChangeMethod()
                    }
        }
    }

    private val cancelAction = View.OnClickListener {
        fingerPrintManager.stopAuth()
        mAuthenticationListener?.onDialogClosed()
        dismiss()
    }

    private val changeAction = View.OnClickListener {
        fingerPrintManager.stopAuth()
        mAuthenticationListener?.onChangeMethod()
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
        ivFingerPrint?.setImageResource(successIcon)
        Completable.timer(SUCCESS_DELAY_MILLIS, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    dismiss()
                    mAuthenticationListener?.onAuthenticationSucceeded()
                }
    }

    override fun onTooManyAttempts(errorCode: Int, errString: CharSequence?) {
        setError(errString.toString(), false)
    }

    /**
     * Specifies whether or not the title will be displayed
     */
    @Deprecated("Deprecated in favor of builder design pattern. Call Builder to set this property.")
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
    @Deprecated("Deprecated in favor of builder design pattern. Call Builder to set this property.")
    fun title(title: String): FingerPrintDialog {
        if (title.isNotEmpty()) {
            tvTitle?.text = title
        }
        return this
    }

    /**
     * Set the message to point out what's about to happen
     */
    @Deprecated("Deprecated in favor of builder design pattern. Call Builder to set this property.")
    fun message(message: String): FingerPrintDialog {
        if (message.isNotEmpty()) {
            tvMessage?.text = message
        }
        return this
    }

    /**
     * Set the message for the cancel button
     */
    @Deprecated("Deprecated in favor of builder design pattern. Call Builder to set this property.")
    fun cancelButton(buttonMessage: String): FingerPrintDialog {
        if (buttonMessage.isNotEmpty()) {
            btnCancel?.text = buttonMessage
        }
        return this
    }

    /**
     * If empty, change method button is enabled with default message
     */
    @Deprecated("Deprecated in favor of builder design pattern. Call Builder to set this property.")
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
    @Deprecated("Deprecated in favor of builder design pattern. Call Builder to set this property.")
    fun successIcon(@DrawableRes resId: Int): FingerPrintDialog {
        successIcon = resId
        return this
    }

    /**
     * Change error icon
     */
    @Deprecated("Deprecated in favor of builder design pattern. Call Builder to set this property.")
    fun errorIcon(@DrawableRes resId: Int): FingerPrintDialog {
        errorIcon = resId
        return this
    }

    /**
     * Set the listener to handle incoming messages
     */
    @Deprecated("Deprecated in favor of builder design pattern. Call Builder to set this property.")
    fun setListener(listener: FingerPrintDialogCallback) {
        mAuthenticationListener = listener
    }

    /**
     * Set the default message
     */
    @Deprecated("Deprecated in favor of builder design pattern. Call Builder to set this property.")
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
    @Deprecated("Deprecated in favor of builder design pattern. Call Builder to set this property.")
    fun buttonsColor(@ColorRes resId: Int) {
        btnChange?.setTextColor(ContextCompat.getColor(activity!!, resId))
        btnCancel?.setTextColor(ContextCompat.getColor(activity!!, resId))
    }

    open inner class Builder {

        private var title: String? = null
        private var errorIcon: Int? = null
        private var message: String? = null
        private var successIcon: Int? = null
        private var buttonsColor: Int? = null
        private var statusMessage: String? = null
        private var titleEnabled: Boolean? = null
        private var cancelBtnMessage: String? = null
        private var enableChangeMethodBtn: Boolean? = null
        private var authenticationListener: FingerPrintDialogCallback? = null

        fun setTitleEnabled(enabled: Boolean): Builder {
            this.titleEnabled = enabled
            return this
        }

        fun setDialogTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setMessage(message: String): Builder {
            this.message = message
            return this
        }

        fun setStatusMessage(message: String): Builder {
            this.statusMessage = message
            return this
        }

        fun setCancelButtonMessage(message: String): Builder {
            this.cancelBtnMessage = message
            return this
        }

        fun enableChangeMethodButton(enable: Boolean): Builder {
            this.enableChangeMethodBtn = enable
            return this
        }

        fun setSuccessIcon(@DrawableRes resId: Int): Builder {
            this.successIcon = resId
            return this
        }

        fun setErrorIcon(@DrawableRes resId: Int): Builder {
            this.errorIcon = resId
            return this
        }

        fun setAuthenticationListener(listener: FingerPrintDialogCallback): Builder {
            this.authenticationListener = listener
            return this
        }

        fun setButtonsColors(@ColorRes resId: Int): Builder {
            this.buttonsColor = resId
            return this
        }

        fun create(): FingerPrintDialog {
            val fingerPrintDialog = FingerPrintDialog()
            this.title?.let {
                fingerPrintDialog.title = it
            }
            this.message?.let {
                fingerPrintDialog.message = it
            }
            this.errorIcon?.let {
                fingerPrintDialog.errorIcon = it
            }
            this.successIcon?.let {
                fingerPrintDialog.successIcon = it
            }
            this.buttonsColor?.let {
                fingerPrintDialog.buttonsColor = it
            }
            this.statusMessage?.let {
                fingerPrintDialog.statusMessage = it
            }
            this.titleEnabled?.let {
                fingerPrintDialog.titleEnabled = it
            }
            this.cancelBtnMessage?.let {
                fingerPrintDialog.cancelBtnMessage = it
            }
            this.enableChangeMethodBtn?.let {
                fingerPrintDialog.enableChangeMethodBtn = it
            }
            if (this.authenticationListener == null) {
                throw NullPointerException("FingerPrintDialogCallback interface required. You need to call setListener method.")
            } else {
                fingerPrintDialog.mAuthenticationListener = this.authenticationListener
            }
            return fingerPrintDialog
        }

    }

    interface FingerPrintDialogCallback {
        fun onAuthenticationSucceeded()
        fun onChangeMethod()
        fun onDialogClosed() {}
        @Deprecated("Avoid using this method. Call Builder for customization.")
        fun onSetupDialog() {}
    }

}