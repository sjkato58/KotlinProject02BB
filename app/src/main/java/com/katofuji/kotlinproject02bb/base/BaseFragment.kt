package com.katofuji.kotlinproject02bb.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.katofuji.kotlinproject02bb.ISDEBUG
import com.katofuji.kotlinproject02bb.TAG_CORE
import com.katofuji.kotlinproject02bb.databinding.FragmentCharacterSelectionBinding
import com.katofuji.kotlinproject02bb.utils.GlideApp
import com.katofuji.kotlinproject02bb.utils.GlideRequests
import com.katofuji.kotlinproject02bb.utils.Inflate

abstract class BaseFragment<VB: ViewBinding, VM : BaseViewModel>constructor(
    private val inflate: Inflate<VB>
) : Fragment() {

    protected abstract val viewModel: VM

    protected var _binding: VB? = null
    protected val binding get() = _binding!!

    lateinit var glideRequests: GlideRequests

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        glideRequests = GlideApp.with(this)
        if (ISDEBUG) Log.i(TAG_CORE, "onCreate $this")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (ISDEBUG) Log.i(TAG_CORE, "onCreateView $this")
        _binding = inflate.invoke(inflater, container, false)
        initViews()
        initBaseObservers()
        initObservers()
        return binding.root
    }

    protected open fun initViews() = Unit

    private fun initBaseObservers() {
        viewModel.navigationEvent.observe(viewLifecycleOwner) {
            it(findNavController())
        }
    }

    protected open fun initObservers() = Unit

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}