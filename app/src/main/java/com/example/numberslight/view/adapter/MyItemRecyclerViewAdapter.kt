package com.example.numberslight.view.adapter


import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.numberslight.R
import com.example.numberslight.service.model.NumberData
import com.example.numberslight.view.ui.ItemFragment.OnListFragmentInteractionListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_item.view.*


class MyItemRecyclerViewAdapter(
    private val listener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {
    private val values = ArrayList<NumberData>()
    private val onClickListener: View.OnClickListener
    private val clickedItem: ArrayList<NumberData> = ArrayList()

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as NumberData
            listener?.onListFragmentInteraction(item)
            v.setBackgroundColor(v.resources.getColor(R.color.colorAccent))
            clickedItem.add(item)
        }
    }

    fun updateValue(list: ArrayList<NumberData>) {
        values.clear()
        values.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        if(clickedItem.contains(item)){
            holder.view.setBackgroundColor(holder.view.resources.getColor(
                R.color.colorAccent
            ))
        }else {
            holder.view.setBackgroundResource(android.R.color.transparent)
        }
        holder.name.text = item.name
        item.image?.let { Picasso.get().load(it).into(holder.image); }

        with(holder.view) {
            tag = item
            setOnClickListener(onClickListener)
            setOnFocusChangeListener { view, focused ->
                if (focused) {
                    setBackgroundResource(R.color.colorPrimaryDark)
                } else {
                    if(clickedItem.contains(item)){
                        setBackgroundResource(R.color.colorAccent)
                    }else {
                        setBackgroundResource(android.R.color.transparent)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = values.size
    fun selectItem(i: Int, b: Boolean) {

    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.name
        val image: ImageView = view.image

        override fun toString(): String {
            return super.toString() + " '" + name.text + "'"
        }
    }
}
