package com.tdp.cycle.bases

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.tdp.cycle.common.IProgressView


abstract class CycleBaseActivity<VB: ViewBinding>(
    private val bindingInflater: (layoutInflater: LayoutInflater) -> VB
) : AppCompatActivity(), IProgressView {

    lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingInflater.invoke(layoutInflater)
        setContentView(binding.root)
    }

    protected open fun clearClassVariables() { }
}