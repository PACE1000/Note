package com.example.challenge4binar

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(
    private val context: Context,
    private val noteViewModel: NoteViewModel,
    private val notes: MutableList<Note>):RecyclerView.Adapter<NoteAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val deleteButton : ImageView = itemView.findViewById(R.id.btn_delete_rv)
        val updateButton :ImageView = itemView.findViewById(R.id.btn_update_rv)

        fun bind(note: Note){
            deleteButton.setOnClickListener{
                dialoghapus(note)

            }
            updateButton.setOnClickListener {
                dialogupdate(note)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.delete, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteAdapter.ViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    private fun dialoghapus(note: Note) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete Note")

        builder.setPositiveButton("Delete") { dialog, _ ->
            noteViewModel.delete(note)

            val position = notes.indexOf(note)
            if (position != -1) {
                notes.removeAt(position)
                notifyItemRemoved(position)
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun dialogupdate(note: Note) {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.update, null)

        // Use var instead of val for these variables
        var etJudul = dialogView.findViewById<EditText>(R.id.et_judul_edit)
        var etCatatan = dialogView.findViewById<EditText>(R.id.et_catatan_edit)
        val btnInput = dialogView.findViewById<Button>(R.id.btn_edit)

        etJudul.setText(note.title)
        etCatatan.setText(note.note)

        builder.setView(dialogView)
        builder.setTitle("Update Note")

        val dialog = builder.create()

        btnInput.setOnClickListener {
            // Handle the update logic here
            val updatedTitle = etJudul.text.toString()
            val updatedNote = etCatatan.text.toString()

            if (updatedTitle.isNotEmpty() && updatedNote.isNotEmpty()) {
                // Update the note with the new data
                note.title = updatedTitle
                note.note = updatedNote

                // Call your update method or ViewModel function here
                // For example: noteViewModel.update(note)

                // Dismiss the dialog when the update is done
                dialog.dismiss()
            } else {
                // Display an error message or handle empty fields
            }
        }

        dialog.show()
    }
    fun setNotes(newNotes: List<Note>){
        notes.clear()
        notes.addAll(newNotes)
    }


}