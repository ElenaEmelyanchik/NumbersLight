package com.example.numberslight.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.numberslight.App
import com.example.numberslight.R
import com.example.numberslight.viewmodel.NumberDetailsViewModel
import com.example.numberslight.viewmodel.NumberDetailsViewModelFactory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.number_details_view.*
import javax.inject.Inject


open class DetailsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: NumberDetailsViewModelFactory

    private val viewModel: NumberDetailsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(NumberDetailsViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity?.application as App).component().inject(this)
        viewModel.getDetails(getShownName())
        viewModel.getNumberDetails().observe(viewLifecycleOwner, Observer {
            textView.text = it.text
            it.image?.let { Picasso.get().load(it).into(imageView); }
        })
    }

    fun getShownName(): String {
        return getArguments()?.getString("name", "") ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (container == null) {
            return null
        }
        val inflate= inflater.inflate(R.layout.number_details_view, container, false)

        return inflate
    }

    companion object{
        fun newInstance(name: String?): DetailsFragment? {
            val f = DetailsFragment()
            // Supply index input as an argument.
            val args = Bundle()
            args.putString("name", name)
            f.setArguments(args)
            return f
        }
    }
}