package com.example.numberslight.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.numberslight.App
import com.example.numberslight.R
import com.example.numberslight.R.id.number_details
import com.example.numberslight.di.NoNetworkConnectionInterceptor
import com.example.numberslight.utils.BUNDLE_NAME
import com.example.numberslight.utils.EMPTY
import com.example.numberslight.utils.handleNoNetworkError
import com.example.numberslight.utils.launch
import com.example.numberslight.view.adapter.MyItemRecyclerViewAdapter
import com.example.numberslight.viewmodel.NumbersDataViewModel
import com.example.numberslight.viewmodel.NumbersListViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.number_list_view.*
import javax.inject.Inject


class NumbersFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: NumbersListViewModelFactory

    private val viewModel: NumbersDataViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(NumbersDataViewModel::class.java)
    }
    var isDualPane = false
    var selectedName: String? = null
    var viewAdapter: MyItemRecyclerViewAdapter? = null
    private var snackbar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.number_list_view, container, false).also {
            (activity?.application as App).component().inject(this)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.numbers().observe(viewLifecycleOwner, Observer {
            viewAdapter?.updateValue(it)
        })

        viewModel.state().observe(viewLifecycleOwner, Observer {
            handleError(it)
        })
    }

    private fun handleError(it: NumbersDataViewModel.State?) {
        when (it) {
            is NumbersDataViewModel.State.Success -> snackbar?.dismiss()
            is NumbersDataViewModel.State.Error -> {
                when (it.error) {
                    is NoNetworkConnectionInterceptor.NoNetworkConnectionException -> snackbar =
                        activity?.findViewById<View>(
                            android.R.id.content
                        )?.handleNoNetworkError(
                            {
                                viewModel.getNumbers()
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewManager = LinearLayoutManager(this.context)
        val detailsFrame: View? = activity?.findViewById(number_details)
        isDualPane = detailsFrame != null && detailsFrame.visibility == View.VISIBLE

        viewAdapter = MyItemRecyclerViewAdapter(isDualPane) { t -> showDetails(t.name) }

        (my_recycler_view as RecyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

        }

        selectedName = savedInstanceState?.getString(BUNDLE_NAME, EMPTY) ?: EMPTY

        if (isDualPane) {
            showDetails(selectedName)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(BUNDLE_NAME, selectedName)
    }

    private fun showDetails(name: String?) {
        selectedName = name
        if (isDualPane) {
            viewAdapter?.selectItem(name)
            var details: NumberDetailsFragment1? =
                activity?.supportFragmentManager?.findFragmentById(number_details) as NumberDetailsFragment1?
            if (details == null || details.getShownName() != name) {
                details = NumberDetailsFragment1.newInstance(name)
                activity?.supportFragmentManager?.beginTransaction()?.apply {
                    replace(number_details, details)
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    commit()
                }
            }
        } else {
            this.context?.let {
                launch<NumberDetailsActivity>(it) {
                    putExtra(BUNDLE_NAME, name)
                }
            }
        }
    }
}