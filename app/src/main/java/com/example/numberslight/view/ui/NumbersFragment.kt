package com.example.numberslight.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.numberslight.App
import com.example.numberslight.R
import com.example.numberslight.R.id.number_details
import com.example.numberslight.utils.BUNDLE_NAME
import com.example.numberslight.utils.launch
import com.example.numberslight.view.adapter.MyItemRecyclerViewAdapter
import com.example.numberslight.viewmodel.NumbersDataViewModel
import com.example.numberslight.viewmodel.NumbersListViewModelFactory
import kotlinx.android.synthetic.main.article_view.*
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.article_view, container, false).also {
            (activity?.application as App).component().inject(this)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.numbers().observe(viewLifecycleOwner, Observer {
            viewAdapter?.updateValue(it)
        })
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

        selectedName = savedInstanceState?.getString("curName", "") ?: ""

        if (isDualPane) {
            showDetails(selectedName)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("curName", selectedName)
    }

    private fun showDetails(name: String?) {
        selectedName = name
        if (isDualPane) {
            viewAdapter?.selectItem(name)
            var details: NumberDetailsFragment? =
                activity?.supportFragmentManager?.findFragmentById(number_details) as NumberDetailsFragment?
            if (details == null || details.getShownName() != name) {
                details = NumberDetailsFragment.newInstance(name = name)
                activity?.supportFragmentManager?.beginTransaction()?.apply {
                    details?.let { replace(number_details, it) }
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