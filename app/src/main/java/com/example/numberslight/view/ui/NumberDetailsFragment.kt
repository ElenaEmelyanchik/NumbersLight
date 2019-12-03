package com.example.numberslight.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.numberslight.App
import com.example.numberslight.R
import com.example.numberslight.di.NoNetworkConnectionInterceptor
import com.example.numberslight.utils.BUNDLE_NAME
import com.example.numberslight.utils.EMPTY
import com.example.numberslight.utils.handleNoNetworkError
import com.example.numberslight.viewmodel.NumberDetailsViewModel
import com.example.numberslight.viewmodel.NumberDetailsViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.number_details_view.*
import javax.inject.Inject


class NumberDetailsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: NumberDetailsViewModelFactory

    private var snackbar: Snackbar? = null

    private val viewModel: NumberDetailsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(NumberDetailsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getDetails(getShownName())
        viewModel.getNumberDetails().observe(viewLifecycleOwner, Observer {
            textView.text = it.text
            it.image?.let { Picasso.get().load(it).into(imageView); }
        })

        viewModel.state().observe(viewLifecycleOwner, Observer {
            handleError(it)
        })
    }

    private fun handleError(it: NumberDetailsViewModel.State?) {
        when (it) {
            is NumberDetailsViewModel.State.Success -> snackbar?.dismiss()
            is NumberDetailsViewModel.State.Error -> {
                when (it.error) {
                    is NoNetworkConnectionInterceptor.NoNetworkConnectionException -> snackbar =
                        activity?.findViewById<View>(
                            android.R.id.content
                        )?.handleNoNetworkError(
                            {
                                viewModel.getDetails(
                                    getShownName()
                                )
                            }, resources
                        )
                    else -> Toast.makeText(
                        context,
                        getString(R.string.unexpected_error),
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
        }
    }

    fun getShownName(): String {
        return arguments?.getString(BUNDLE_NAME, EMPTY) ?: EMPTY
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (container == null) {
            return null
        }

        val inflate = inflater.inflate(R.layout.number_details_view, container, false)
        (activity?.application as App).component().inject(this)
        return inflate
    }

    companion object {
        fun newInstance(name: String?): NumberDetailsFragment =
            NumberDetailsFragment().apply {
                val args = Bundle().apply {
                    putString(BUNDLE_NAME, name)
                }
                arguments = args
            }
    }
}