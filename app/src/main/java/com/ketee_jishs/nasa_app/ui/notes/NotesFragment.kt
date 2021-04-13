package com.ketee_jishs.nasa_app.ui.notes

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.ketee_jishs.nasa_app.R
import kotlinx.android.synthetic.main.dialog_layout.*
import kotlinx.android.synthetic.main.notes_fragment.*

@Suppress("SameParameterValue")
class NotesFragment : Fragment() {
    val viewModel: NotesViewModel by lazy {
        ViewModelProviders.of(this).get(NotesViewModel::class.java)
    }

    private val notesAdapter = NotesAdapter(arrayListOf())
    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.notes_fragment, container, false)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        notesRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        notesRecyclerView.adapter = notesAdapter
        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(notesAdapter))
        itemTouchHelper.attachToRecyclerView(notesRecyclerView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.notesLiveData.observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getAllNotes()
    }

    private fun renderData(notesAppState: NotesAppState) {
        when (notesAppState) {
            is NotesAppState.Success -> {
                notesAdapter.replaceData(notesAppState.notesData)
                addNote(notesAppState.notesData)
            }
            is NotesAppState.Error -> {
                Toast.makeText(context, "Data loading error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun addNote(data: MutableList<NotesData>) {
        addNoteView.setOnClickListener {
            val dialog = Dialog(context!!)
            dialog.setContentView(R.layout.dialog_layout)
            dialog.okButton.setOnClickListener {
                val newNoteHeader = dialog.inputHeaderNoteEditText.text.toString()
                val newNoteBody = dialog.inputBodyNoteEditText.text.toString()
                if (newNoteBody.isEmpty() || newNoteHeader.isEmpty()) {
                    dialog.dismiss()
                } else {
                    data.add(NotesData(newNoteHeader, newNoteBody))
                    viewModel.saveNoteToDB(NotesData(newNoteHeader, newNoteBody))
                    notesAdapter.replaceData(data)
                    activity?.recreate()
                }
            }
            dialog.cancelButton.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    companion object {
        fun newInstance() = NotesFragment()
    }
}