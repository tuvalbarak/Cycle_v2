package com.tdp.cycle.common.customviews

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.res.getResourceIdOrThrow
import androidx.core.view.updateLayoutParams
import androidx.core.widget.TextViewCompat
import com.tdp.cycle.R
import com.tdp.cycle.common.MTDensityUtil
import com.tdp.cycle.databinding.MtPrimaryButtonBinding

/** implementation of MonkeyTech Primary Button view.
 *  This view should be in use in case we have the same button in couple of places the project with different attributes.
 *  You can change the attributes below via xml file or programmatically in your code
 *  You can inherit MTPrimaryButton class and override the methods below as you wish */

open class MTPrimaryButton @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {

    private var binding: MtPrimaryButtonBinding =
        MtPrimaryButtonBinding.inflate(LayoutInflater.from(context), this, true)
    private var currentType: Int? = null

    fun setPrimaryButtonListener(listener: PrimaryButtonListener, type: Int? = null) {
        this.listener = listener
        this.currentType = type
    }

    var listener: PrimaryButtonListener? = null

    interface PrimaryButtonListener {
        fun onPrimaryButtonClick(type: Int?) {}
    }

    init {
        attrs.let {
            val attrArray = context.obtainStyledAttributes(attrs, R.styleable.MTPrimaryButton)

            /** Title - Define text inside the button */
            if (attrArray.hasValue(R.styleable.MTPrimaryButton_MTtitle)) {
                val title = attrArray.getString(R.styleable.MTPrimaryButton_MTtitle)
                this.setText(title)
            }

            setTextSize(
                attrArray.getInt(R.styleable.MTPrimaryButton_MTtextSize, 11)
            )

            /** IsEnabled - boolean value - Button state if enabled or disabled */
            if (attrArray.hasValue(R.styleable.MTPrimaryButton_MTisEnabled)) {
                val isEnabled = attrArray.getBoolean(R.styleable.MTPrimaryButton_MTisEnabled, false)
                setPrimaryBtnEnabled(isEnabled)
            }

            /** BackgroundSelector - Define the button background */
            if (attrArray.hasValue(R.styleable.MTPrimaryButton_MTbackgroundSelector)) {
                try {
                    val bkgSelector =
                        attrArray.getResourceIdOrThrow(R.styleable.MTPrimaryButton_MTbackgroundSelector)
                    binding.MTprimaryButton.setBackgroundResource(bkgSelector)
                } catch (ex: Exception) {
                    Log.d(MT_PRIMARY_BUTTON_TAG, "load background resource failed")
                }
            }

            /** TextColorSelector - Define the text color */
            if (attrArray.hasValue(R.styleable.MTPrimaryButton_MTtextColorSelector)) {
                val textColorSelector =
                    attrArray.getColorStateList(R.styleable.MTPrimaryButton_MTtextColorSelector)
                binding.MTprimaryButton.setTextColor(textColorSelector)
            }

            if (attrArray.hasValue(R.styleable.MTPrimaryButton_MTisSlim)) {
                val isSlim = attrArray.getBoolean(R.styleable.MTPrimaryButton_MTisSlim, false)
                if (isSlim) {
                    setSlimUi()
                }
            }

            /** Stroke - Define stroke for the button */
            if (attrArray.hasValue(R.styleable.MTPrimaryButton_MTstrokeTextColor)) {
                val strokeTextColor =
                    attrArray.getColor(
                        R.styleable.MTPrimaryButton_MTstrokeTextColor,
                        ContextCompat.getColor(
                            context,
                            R.color.blackTextColor
                        )
                    )
                binding.MTprimaryButton.setTextColor(strokeTextColor)

                // Check if need a frame, put white background as default
                var background = ContextCompat.getDrawable(context, R.color.white)
                if (attrArray.hasValue(R.styleable.MTPrimaryButton_MTstrokeBackground)) {
                    background = AppCompatResources.getDrawable(
                        context,
                        attrArray.getResourceId(R.styleable.MTPrimaryButton_MTstrokeBackground, 0)
                    )
                }
                binding.MTprimaryButton.background = background
            }

            /** Style - Define text style of the button */
            if (attrArray.hasValue(R.styleable.MTPrimaryButton_MTstyle)) {
                try {
                    val styleId =
                        attrArray.getResourceIdOrThrow(R.styleable.MTPrimaryButton_MTstyle)
                    setButtonTextAppearance(styleId)
                } catch (ex: Exception) {
                    Log.d(MT_PRIMARY_BUTTON_TAG, "load style resource failed")
                }
            }

            if(attrArray.hasValue(R.styleable.MTPrimaryButton_MTisTextBold)) {
                if (attrArray.getBoolean(R.styleable.MTPrimaryButton_MTisTextBold, false)) {
                    binding.MTprimaryButton.setTypeface(binding.MTprimaryButton.typeface, Typeface.BOLD)
                }
            }

            attrArray.recycle()
        }
    }

    fun setText(text: String?) {
        text?.let {
            binding.MTprimaryButton.text = text
        }
    }

    private fun setTextSize(textSize: Int) {
        binding.MTprimaryButton.textSize = textSize.toFloat()
    }

    private fun setSlimUi() {
        val topBottomPadding = MTDensityUtil.toPx(4)
        val leftRightPadding = MTDensityUtil.toPx(6)
        binding.MTprimaryButton.setPadding(
            leftRightPadding,
            topBottomPadding,
            leftRightPadding,
            topBottomPadding
        )
    }

    fun setWidthSize(widthSize: Int) {
        binding.MTprimaryButton.updateLayoutParams {
            width = widthSize
        }
    }

    fun setHeightSize(heightSize: Int) {
        binding.MTprimaryButton.updateLayoutParams {
            height = heightSize
        }
    }

    private fun setButtonTextAppearance(styleId: Int) {
        binding.MTprimaryButton.let {
            TextViewCompat.setTextAppearance(it, styleId)
        }
    }

    fun setButtonTextColor(color: Int) {
        binding.MTprimaryButton.setTextColor(color)
    }

    private fun setButtonBackgroundColor(color: Int) {
        binding.MTprimaryButton.setBackgroundColor(color)
    }

    fun changeBackgroundColor(drawable: Drawable?) {
        binding.MTprimaryButton.background = drawable
    }

    fun setBtnEnabled(enabled: Boolean) {
        setPrimaryBtnEnabled(enabled)
    }

    private fun setPrimaryBtnEnabled(isEnabled: Boolean) {
        this.isEnabled = isEnabled
        binding.MTprimaryButton.isEnabled = isEnabled
    }

    companion object {
        private const val MT_PRIMARY_BUTTON_TAG = "MTPrimaryButton"
    }
}