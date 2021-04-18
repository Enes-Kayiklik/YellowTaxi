package com.eneskayiklik.yellowtaxi.ui.base

import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

abstract class BaseDialogFragment<B : ViewDataBinding> : DialogFragment() {

    @get:LayoutRes
    abstract val layoutId: Int
    lateinit var binder: B

    abstract fun initialize()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        this.binder = DataBindingUtil.inflate(inflater, layoutId, container, false)
        initialize()
        return binder.root
    }
}