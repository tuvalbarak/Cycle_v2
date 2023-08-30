package com.tdp.cycle.common.customviews

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tdp.cycle.R
import com.tdp.cycle.databinding.CustomInputViewBinding

class CustomInputView  @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr) {

    var binding : CustomInputViewBinding

    interface CustomInputViewListener{
        fun onSendMessageClicked(message: String)
    }

    private var listener: CustomInputViewListener? = null

    init {
        val inflater = LayoutInflater.from(context).inflate(R.layout.custom_input_view, this, true)
        binding = CustomInputViewBinding.bind(inflater.rootView)
        initUi()
    }

    private fun initUi() {
        binding.chatBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s?.toString()?.trim().isNullOrEmpty()){
                    setSendButtonEnable(false)
                }else{
                    setSendButtonEnable(true)
                }
            }
        })
        binding.sendButton.setOnClickListener{
            listener?.onSendMessageClicked(message = binding.chatBox.text.toString())
        }
    }

    fun setOnSendMessageListener(function: (String) -> Unit) {
        this.listener = object: CustomInputViewListener {
            override fun onSendMessageClicked(message: String) {
                function.invoke(message)
                binding.chatBox.text.clear()
            }
        }
    }

    private var isEnabledState:Boolean = false
    private fun setSendButtonEnable(isEnabled: Boolean){
        this.isEnabledState = isEnabled
        binding.sendButton.isEnabled = isEnabled && !isBlockedState
        binding.sendButton.isSelected = isEnabled && !isBlockedState
    }

    private var isBlockedState:Boolean = false
    fun setSendButtonBlocked(isBlocked: Boolean){
        this.isBlockedState = isBlocked
        setSendButtonEnable(isEnabledState)
    }

}