package com.example.numberslight.view.ui

import android.content.Intent
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
import com.example.numberslight.service.model.NumberData
import com.example.numberslight.view.adapter.MyItemRecyclerViewAdapter
import com.example.numberslight.viewmodel.NumbersDataViewModel
import com.example.numberslight.viewmodel.NumbersListViewModelFactory
import kotlinx.android.synthetic.main.article_view.*
import javax.inject.Inject


class NumbersFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: NumbersListViewModelFactory

    private val viewModel: NumbersDataViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(NumbersDataViewModel::class.java)
    }
    var mDualPane = false
    var selectedName: String? = null
    var viewAdapter: MyItemRecyclerViewAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.article_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity?.application as App).component().inject(this)

        val viewManager = LinearLayoutManager(this.context)
        viewAdapter = MyItemRecyclerViewAdapter(object :
            ItemFragment.OnListFragmentInteractionListener {
            override fun onListFragmentInteraction(item: NumberData?) {
                showDetails(item?.name)
            }
        }
        )

        (my_recycler_view as RecyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

        }

        viewModel.numbers().observe(viewLifecycleOwner, Observer {
            viewAdapter?.updateValue(it)
        })



        // Check to see if we have a frame in which to embed the details
// fragment directly in the containing UI.
        val detailsFrame: View? = activity?.findViewById(R.id.details)
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() === View.VISIBLE
        if (savedInstanceState != null) { // Restore last state for checked position.
            selectedName = savedInstanceState.getString("curName", "")
        }
        if (mDualPane) {
            //viewAdapter.selectItem()// In dual-pane mode, the list view highlights the selected item.

            // Make sure our UI is in the correct state.
            showDetails(selectedName)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("curName", selectedName)
    }


    /**
     * Helper function to show the details of a selected item, either by
     * displaying a fragment in-place in the current UI, or starting a
     * whole new activity in which it is displayed.
     */
    fun showDetails(name: String?) {
        selectedName = name
        if (mDualPane) { // We can display everything in-place with fragments, so update
// the list to highlight the selected item and show the data.
         //   viewAdapter?.selectItem(index, true)
            //listView.setItemChecked(index, true)
            // Check what fragment is currently shown, replace if needed.
            var details: DetailsFragment? =
                fragmentManager!!.findFragmentById(R.id.details) as DetailsFragment?
            if (details == null || !details.getShownName().equals(name)) { // Make new fragment to show this selection.
                details = DetailsFragment.newInstance(name)
                // Execute a transaction, replacing any existing fragment
// with this one inside the frame.
                val ft: FragmentTransaction = fragmentManager!!.beginTransaction()
               // if (index == 0) {
                    details?.let { ft.replace(R.id.details, it) }
             //   } else {
                  //  details?.let { ft.replace(R.id.a_item, it) }
             //   }
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                ft.commit()
            }
        } else { // Otherwise we need to launch a new activity to display
// the dialog fragment with selected text.
            val intent = Intent()
            intent.setClass(activity!!, NumberDetailsActivity::class.java)
            intent.putExtra("name", name)
            startActivity(intent)
        }
    }
}