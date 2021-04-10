package com.ketee_jishs.nasa_app.ui.notes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ketee_jishs.nasa_app.R
import kotlinx.android.synthetic.main.notes_item.view.*

class NotesAdapter(
    private var data: MutableList<NotesData> = mutableListOf()
) : RecyclerView.Adapter<NotesAdapter.ViewHolder>(), ItemTouchHelperAdapter {

    fun replaceData(data: MutableList<NotesData>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.notes_item, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(data[position])

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), ItemTouchHelperViewHolder {
        @SuppressLint("SetTextI18n")
        fun bind(data: NotesData) {
            itemView.noteItemHeader.text = data.noteHeader
            itemView.noteItemBody.text = data.noteBody

            itemView.hideNoteButton.setOnClickListener { hideNote() }
            itemView.noteDropUpButton.setOnClickListener { dropNoteUp() }
            itemView.noteDropDownButton.setOnClickListener { dropNoteDown() }
        }

        private fun hideNote() {
            data.removeAt(layoutPosition)
            notifyItemRemoved(layoutPosition)
        }

        private fun dropNoteUp() {
            layoutPosition.takeIf { it > 0 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition - 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition - 1)
            }
        }

        private fun dropNoteDown() {
            layoutPosition.takeIf { it < data.size - 1 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition + 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition + 1)
            }
        }

        override fun onItemSelected() {}

        override fun onItemClear() {}
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        data.removeAt(fromPosition).apply {
            data.add(
                if (toPosition > fromPosition) toPosition - 1
                else toPosition, this
            )
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }
}