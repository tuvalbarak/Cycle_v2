package com.tdp.cycle.bases

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.tdp.cycle.common.IProgressView
import dagger.hilt.android.internal.managers.ViewComponentManager
import java.lang.Exception


abstract class CycleBaseFragment<VB: ViewBinding>(
    private val bindingInflater: (layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean) -> VB
): Fragment(), IProgressView {

    private var _binding: VB? = null
    val binding get() = _binding

    protected var pbListener: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        pbListener = try {
            //If the context is DaggerHilt - use baseContext (activity context)
            (context as ViewComponentManager.FragmentContextWrapper).baseContext
        } catch (e: Exception) {
            //get the context
            context
        } catch (e: Exception) {
            throw ClassCastException("$context must implement Progressable")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        _binding = null
        pbListener = null
        clearClassVariables()
        super.onDestroyView()
    }

    protected open fun clearClassVariables() { }

    protected fun popBackStack() {
        activity?.onBackPressedDispatcher?.onBackPressed()
    }

    override fun handleProgress(isLoading: Boolean) {
        (pbListener as? IProgressView?)?.handleProgress(isLoading)
    }

}