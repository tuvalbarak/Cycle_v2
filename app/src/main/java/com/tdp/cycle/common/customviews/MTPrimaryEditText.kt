package com.tdp.cycle.common.customviews

import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.InputType
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.res.getResourceIdOrThrow
import androidx.core.view.updateLayoutParams
import com.tdp.cycle.R
import com.tdp.cycle.common.SimpleTextWatcher
import com.tdp.cycle.common.hide
import com.tdp.cycle.common.show
import com.tdp.cycle.databinding.MtEditTextPrimaryBinding

class MTPrimaryEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var binding: MtEditTextPrimaryBinding =
        MtEditTextPrimaryBinding.inflate(LayoutInflater.from(context), this, true)
    var listener: PrimaryEditTextListener? = null

    private var errorEmpty: String = ""
    private var errorInput: String = ""
    private var currentType: Int? = null

    interface PrimaryEditTextListener {
        fun afterTextChanged(id: Int, s: String) {}
        fun onPrimaryEditTextClick(type: Int?) {}
        fun onPrimaryEditTextFocused(type: Int?) {}
    }

    fun setPrimaryEditTextListener(listener: PrimaryEditTextListener, type: Int? = null) {
        this.listener = listener
        this.currentType = type
    }

    init {
        initUi(attrs)
        setListeners()
    }

    private fun initUi(attrs: AttributeSet?) {
        binding.apply {
            val attr = context.obtainStyledAttributes(attrs, R.styleable.MTPrimaryEditText)

            /** Title - Define the title of the editText */
            val title = attr.getString(R.styleable.MTPrimaryEditText_MTtitle)
            MTprimaryEditTextTitle.text = title

            /** Hint - Define the hint of the editText */
            val hint = attr.getString(R.styleable.MTPrimaryEditText_MThint)
            MTprimaryEditTextTextInput.hint = hint

            /** Errors - Define the errors of the editText */
            var err = attr.getString(R.styleable.MTPrimaryEditText_MTerrorEmpty)
            errorEmpty = if (!err.isNullOrBlank()) {
                "* $err"
            } else {
                DEFAULT_EMPTY_ERROR_MESSAGE
            }

            err = attr.getString(R.styleable.MTPrimaryEditText_MTerrorInput)
            errorInput = if (!err.isNullOrBlank()) {
                "* $err"
            } else {
                DEFAULT_INPUT_ERROR_MESSAGE
            }

            val textSize = attr.getInt(R.styleable.MTPrimaryEditText_MTtextSize, 16)
            setTextSize(textSize)

            MTprimaryEditTextTextInput.inputType = attr.getInt(
                R.styleable.MTPrimaryEditText_android_inputType,
                InputType.TYPE_CLASS_TEXT
            )

            if (attr.hasValue(R.styleable.MTPrimaryEditText_MTisEnabled)) {
                val isEnabled = attr.getBoolean(R.styleable.MTPrimaryEditText_MTisEnabled, true)
                setPrimaryEditTextEnabled(isEnabled)
            }

            /** HasArrow - set the arrow of the editText */
            val hasArrow = attr.getBoolean(R.styleable.MTPrimaryEditText_MThasArrow, false)
            if (hasArrow) {
                setHasArrowUi()
            }

            /** BkgSelector - set the bkg of the editText in case of error */
            if (attr.hasValue(R.styleable.MTPrimaryEditText_MTbackgroundSelector)) {
                try {
                    val bkgSelector =
                        attr.getResourceIdOrThrow(R.styleable.MTPrimaryEditText_MTbackgroundSelector)
                    MTprimaryEditTextLayout.setBackgroundResource(bkgSelector)
                } catch (ex: Exception) {
                    Log.d(MT_PRIMARY_EDIT_TEXT_TAG, "load background resource failed")
                }
            }

            /** CursorColor - set the cursor color of the editText */
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (attr.hasValue(R.styleable.MTPrimaryEditText_MTtextCursorDrawable)) {
                    val textCursorDrawable =
                        attr.getResourceId(
                            R.styleable.MTPrimaryEditText_MTtextCursorDrawable,
                            R.drawable.custom_text_cursor_primary
                        )
                    MTprimaryEditTextTextInput.setTextCursorDrawable(textCursorDrawable)
                }
            }

            setPrimaryEditTextSelected(true)

            attr.recycle()
        }
    }

    private fun setListeners() {
        binding.apply {
            MTprimaryEditTextTextInput.addTextChangedListener(object : SimpleTextWatcher() {
                override fun afterTextChanged(p0: Editable?) {
                    listener?.afterTextChanged(id, p0.toString())
                }
            })

            MTprimaryEditTextTextInput.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    listener?.onPrimaryEditTextFocused(currentType)
                }
            }
        }
    }

    private fun setHasArrowUi() {
        binding.apply {
            MTprimaryEditTextTextInput.isFocusable = false
            MTprimaryEditTextTextInput.isClickable = true

            MTprimaryEditTextTextInput.setOnClickListener {
                listener?.onPrimaryEditTextClick(currentType)
            }
            MTprimaryEditTextLayout.setOnClickListener {
                listener?.onPrimaryEditTextClick(currentType)
            }
        }
    }

    fun setWidthSize(widthSize: Int) {
        binding.MTprimaryEditTextLayout.updateLayoutParams {
            width = widthSize
        }
    }

    fun setHeightSize(heightSize: Int) {
        binding.MTprimaryEditTextLayout.updateLayoutParams {
            height = heightSize
        }
    }

    fun handleValidationErr(isValid: Boolean, isEmpty: Boolean = false) {
        binding.apply {
            setPrimaryEditTextSelected(isValid)

            if (isValid) {
                MTprimaryEditTextError.hide()
            } else {
                setErrMsg(isEmpty)
                MTprimaryEditTextError.show()
                MTprimaryEditTextTextInput.requestFocus()
            }
        }
    }

    private fun setErrMsg(isEmpty: Boolean) {
        binding.apply {
            if (isEmpty) {
                MTprimaryEditTextError.text = errorEmpty
            } else {
                MTprimaryEditTextError.text = errorInput
            }
        }
    }

    private fun setPrimaryEditTextSelected(isSelected: Boolean) {
        binding.apply {
            MTprimaryEditTextTextInput.isSelected = isSelected
            MTprimaryEditTextLayout.isSelected = isSelected
        }
    }

    fun setPrimaryEditTextEnabled(enabled: Boolean) {
        binding.apply {
            MTprimaryEditTextTextInput.isEnabled = enabled
            MTprimaryEditTextLayout.isEnabled = enabled
            MTprimaryEditTextLayout.alpha = if (enabled) 1F else 0.5F
        }
    }

    fun setText(text: String?) {
        text?.let {
            binding.MTprimaryEditTextTextInput.setText(text)
        }
    }

    private fun setTextSize(textSize: Int) {
        binding.MTprimaryEditTextTextInput.textSize = textSize.toFloat()
    }

    fun getText(): String = binding.MTprimaryEditTextTextInput.text.trim().toString()

    companion object {
        private const val DEFAULT_EMPTY_ERROR_MESSAGE = "* You must fill this field"
        private const val DEFAULT_INPUT_ERROR_MESSAGE = "* Input is invalid"
        private const val MT_PRIMARY_EDIT_TEXT_TAG = "MTPrimaryEditText"
    }
}