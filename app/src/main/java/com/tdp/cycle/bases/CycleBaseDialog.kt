package com.tdp.cycle.bases

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.tdp.cycle.common.IProgressView


/**
 * This dialog supports full screen dialog and dialog alert with width adjustment
 * full screen dialog -> call setDialogFullScreen() on onViewCreated
 * dialog alert with width adjustment -> call setDialogWidthPercentage(requestedWidth) on onViewCreated
 * */

private const val DEFAULT_DIALOG_WIDTH = 85

abstract class CycleBaseDialog<VB: ViewBinding>(
    private val bindingInflater: (layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean) -> VB
) : DialogFragment(), IProgressView {

    private var _binding: VB? = null
    val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setDialogWidthPercentage(DEFAULT_DIALOG_WIDTH)
    }

    override fun onDestroyView() {
        _binding = null
        clearClassVariables()
        super.onDestroyView()
    }

    protected open fun setDialogWidthPercentage(percentage: Int) {
        context?.resources?.displayMetrics?.widthPixels?.let { containerWidth ->
            (containerWidth * percentage / 100)
        }?.also { dialogWidth ->
            dialog?.window?.setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    protected open fun setDialogFullScreen() {
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    protected open fun clearClassVariables() { }
}