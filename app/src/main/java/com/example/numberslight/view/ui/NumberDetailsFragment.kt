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


class NumberDetailsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: NumberDetailsViewModelFactory

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
    }

    fun getShownName(): String {
        return arguments?.getString("name", "") ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (container == null) {
            return null
        }

        val inflate= inflater.inflate(R.layout.number_details_view, container, false)
        (activity?.application as App).component().inject(this)
        return inflate
    }

    companion object{
        fun newInstance(name: String?): NumberDetailsFragment? {
            val f = NumberDetailsFragment()
            val args = Bundle()
            args.putString("name", name)
            f.arguments = args
            return f
        }
    }
}