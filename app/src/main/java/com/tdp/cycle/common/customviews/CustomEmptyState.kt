package com.tdp.cycle.common.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.tdp.cycle.R
import com.tdp.cycle.databinding.CustomEmptyStateBinding

class CustomEmptyState  @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr) {

    private var binding: CustomEmptyStateBinding =
        CustomEmptyStateBinding.inflate(LayoutInflater.from(context), this, true)

    private var listener: ButtonsClickListener? = null

    interface ButtonsClickListener {
        fun firstButtonClick()
        fun secondButtonClick() {}
    }

    fun setButtonsListener(listener: ButtonsClickListener?) {
        this.listener = listener
    }

    init {
        with(binding) {
            emptyStateFirstButton.setOnClickListener {
                listener?.firstButtonClick()
            }
            emptyStateSecondButton.setOnClickListener {
                listener?.secondButtonClick()
            }
        }
        attrs.let {
            val attrArray = context.obtainStyledAttributes(attrs, R.styleable.CustomEmptyState)

            if (attrArray.hasValue(R.styleable.CustomEmptyState_TitleCustom)) {
                val value = attrArray.getString(R.styleable.CustomEmptyState_TitleCustom)
                value?.let { it1 -> this.setTitle(it1) }
            }
            if (attrArray.hasValue(R.styleable.CustomEmptyState_TextCustom)) {
                val value = attrArray.getString(R.styleable.CustomEmptyState_TextCustom)
                value?.let { it1 -> this.setText(it1) }
            }
            if (attrArray.hasValue(R.styleable.CustomEmptyState_FirstButtonTextCustom)) {
                val value = attrArray.getString(R.styleable.CustomEmptyState_FirstButtonTextCustom)
                value?.let { it1 -> this.setFirstButtonText(it1) }
            }
            if (attrArray.hasValue(R.styleable.CustomEmptyState_SecondButtonTextCustom)) {
                val value = attrArray.getString(R.styleable.CustomEmptyState_SecondButtonTextCustom)
                value?.let { it1 -> this.setSecondButtonText(it1) }
            }
            if (attrArray.hasValue(R.styleable.CustomEmptyState_BackgroundCustom)) {
                val value = attrArray.getResourceId(R.styleable.CustomEmptyState_BackgroundCustom, 0)
                value.let { it1 -> this.setImage(it1) }
            }
            if (attrArray.hasValue(R.styleable.CustomEmptyState_SecondButtonState)) {
                val value = attrArray.getBoolean(R.styleable.CustomEmptyState_SecondButtonState, false)
                value.let { it1 -> this.changeSecondButtonState(it1) }
            }
            if (attrArray.hasValue(R.styleable.CustomEmptyState_FirstButtonState)) {
                val value = attrArray.getBoolean(R.styleable.CustomEmptyState_FirstButtonState, false)
                value.let { it1 -> this.changeFirstButtonState(it1) }
            }

            if (attrArray.hasValue(R.styleable.CustomEmptyState_FirstButtonState)) {
                val value = attrArray.getBoolean(R.styleable.CustomEmptyState_FirstButtonState, false)
                value.let { it1 -> this.changeFirstButtonState(it1) }
            }

            if (attrArray.hasValue(R.styleable.CustomEmptyState_TextCustomState)) {
                val value = attrArray.getBoolean(R.styleable.CustomEmptyState_TextCustomState, false)
                value.let { it1 -> this.changeTextCustomState(it1) }
            }

            if (attrArray.hasValue(R.styleable.CustomEmptyState_TitleCustomState)) {
                val value = attrArray.getBoolean(R.styleable.CustomEmptyState_TitleCustomState, false)
                value.let { it1 -> this.changeTitleCustomState(it1) }
            }

            attrArray.recycle()
        }
    }

    fun setImage(resId: Int) {
        binding.emptyStateIllustration.setBackgroundResource(resId)
    }

    fun setTitle(text: String) {
        binding.emptyStateTitle.text = text
    }

    fun setText(text: String) {
        binding.emptyStateDescription.text = text
    }

    fun setFirstButtonText(text: String) {
        binding.emptyStateFirstButton.setText(text)
    }

    fun setSecondButtonText(text: String) {
        binding.emptyStateSecondButton.setText(text)
    }

    fun changeSecondButtonState(state: Boolean) {
        binding.emptyStateSecondButton.isVisible = state
    }

    fun changeFirstButtonState(state: Boolean) {
        binding.emptyStateFirstButton.isVisible = state
    }

    fun changeTextCustomState(state: Boolean) {
        binding.emptyStateDescription.isVisible = state
    }

    fun changeTitleCustomState(state: Boolean) {
        binding.emptyStateTitle.isVisible = state
    }
}