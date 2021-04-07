package com.ketee_jishs.nasa_app.ui.earth

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ketee_jishs.nasa_app.BuildConfig
import com.ketee_jishs.nasa_app.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.earth_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class EarthAdapter(
    private var data: ArrayList<EarthServerResponseData> = arrayListOf(),
    private var itemDate: Date
) : RecyclerView.Adapter<EarthAdapter.ViewHolder>() {

    @SuppressLint("SimpleDateFormat")
    private val formatterImage = SimpleDateFormat("yyyy/MM/dd")

    fun replaceData(data: ArrayList<EarthServerResponseData>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.earth_item, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(data[position])

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(data: EarthServerResponseData) {
            val date = formatterImage.format(itemDate)
            val url = data.image
            // https://api.nasa.gov/EPIC/archive/natural/2021/03/27/png/epic_1b_20210327004554.png?api_key=DEMO_API
            val image =
                "https://api.nasa.gov/EPIC/archive/natural/$date/png/$url.png?api_key=${BuildConfig.NASA_API_KEY}"
            Picasso.get().load(Uri.parse(image)).into(itemView.earthImageView)

            itemView.captionText.text = data.caption
            itemView.dateText.text = "Date: ${data.date}"
        }
    }
}