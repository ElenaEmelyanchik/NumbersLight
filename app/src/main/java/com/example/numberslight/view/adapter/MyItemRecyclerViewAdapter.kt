package com.example.numberslight.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.numberslight.R
import com.example.numberslight.service.model.NumberData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_item.view.*

class MyItemRecyclerViewAdapter(
    private val isDualPane: Boolean,
    private val listener: (NumberData)->Unit
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {
    private val values = ArrayList<NumberData>()
    private var clickedItem: String? = null
    private var selectedPos = RecyclerView.NO_POSITION

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

        holder.name.text = item.name
        item.image?.let { Picasso.get().load(it).into(holder.image); }

        with(holder.view) {
            setBackColor(position, item)

            setOnFocusChangeListener { _, focused ->
                if (focused) {
                    setBackgroundResource(R.color.colorPrimaryDark)
                } else {
                    setBackColor(position, item)
                }
            }
        }
    }

    private fun View.setBackColor(
        position: Int,
        item: NumberData
    ) {
        setBackgroundColor(
            if (selectedPos == position && isDualPane) {
                Color.GREEN
            } else {
                if (clickedItem == item.name && isDualPane) {
                    selectedPos = position
                    Color.GREEN
                } else {
                    Color.TRANSPARENT
                }
            }
        )
    }

    override fun getItemCount(): Int = values.size

    fun selectItem(name: String?) {
        clickedItem = name
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.name
        val image: ImageView = view.image

        init {
            view.setOnClickListener {
                notifyItemChanged(selectedPos)
                selectedPos = layoutPosition
                notifyItemChanged(selectedPos)
                listener.invoke(it.tag as NumberData)
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + name.text + "'"
        }
    }
}
