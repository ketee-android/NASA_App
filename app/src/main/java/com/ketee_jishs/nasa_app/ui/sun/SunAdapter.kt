package com.ketee_jishs.nasa_app.ui.sun

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ketee_jishs.nasa_app.R
import kotlinx.android.synthetic.main.sun_item.view.*

class SunAdapter(
    private var data: ArrayList<SunServerResponseData> = arrayListOf()
) : RecyclerView.Adapter<SunAdapter.ViewHolder>() {

    fun replaceData(data: ArrayList<SunServerResponseData>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.sun_item, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(data[position])

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(data: SunServerResponseData) {
            itemView.cmeTime.text = "Date: ${data.startTime}"
            itemView.cmeNote.text = data.note

            try {
                itemView.cmeAnalysesNote.visibility = View.VISIBLE
                itemView.cmeAnalysesNote.text = data.cmeAnalyses[0].note
            } catch (e: Exception) {
                itemView.cmeAnalysesNote.visibility = View.GONE
            }
        }
    }
}
