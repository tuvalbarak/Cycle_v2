package com.tdp.cycle.bases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tdp.cycle.common.ProgressData
import com.tdp.cycle.common.SingleLiveEvent
import com.tdp.cycle.common.loge
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


abstract class CycleBaseViewModel : ViewModel() {
    val errorEvent = SingleLiveEvent<String>()
    val progressData = ProgressData()

    /** This handler catches only uncaught exceptions by the caller (without try-catch block) */
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        loge(exception.localizedMessage)
        exception.printStackTrace()
    }

    /** Use this function to launch coroutines inside VMs (IO scope). It will catch any uncaught exceptions */
    fun safeViewModelScopeIO(
        coroutineContext: CoroutineContext = Dispatchers.IO + coroutineExceptionHandler,
        block: suspend () -> Unit
    ) {
        viewModelScope.launch(coroutineContext) {
            block.invoke()
        }
    }

    /** Use this function to launch coroutines inside VMs (Main scope). It will catch any uncaught exceptions */
    fun safeViewModelScopeMain(
        coroutineContext: CoroutineContext = Dispatchers.Main + coroutineExceptionHandler,
        block: suspend () -> Unit
    ) {
        viewModelScope.launch(coroutineContext) {
            block.invoke()
        }
    }

}