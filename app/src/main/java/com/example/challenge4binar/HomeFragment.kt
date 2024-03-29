package com.example.challenge4binar

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.challenge4binar.databinding.FragmentHomeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences

    val database = AppDatabase.getDatabase(requireContext())
    val noteDao = database!!.noteDao()

    private val noteViewModel by viewModels<NoteViewModel> { NoteViewModelFactory(NoteRepository(noteDao = noteDao )) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteViewModel.checkWorking()
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        sharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvLogout.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }
        val adapter = NoteAdapter(requireContext(), noteViewModel, mutableListOf())

        val recyclerView = binding.rvNote
        recyclerView.adapter = adapter

        noteViewModel.allNotes.observe(viewLifecycleOwner) { notes ->
            if (notes.isEmpty()) {
                binding.tvDatakosong.visibility = View.VISIBLE
                binding.rvNote.visibility = View.GONE
            } else {
                binding.tvDatakosong.visibility = View.GONE
                binding.rvNote.visibility = View.VISIBLE
                adapter.setNotes(notes)
            }
        }

        binding.btnTambahHome.setOnClickListener{
            dialogtambah()
        }

    }

    fun dialogtambah() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.tambah, null)

        builder.setView(dialogView)
        builder.setTitle("Tambah Data")

        builder.setPositiveButton("Add") { dialog, _ ->
            val contentEditText = dialogView.findViewById<EditText>(R.id.et_judul_tambah)
            val titleEditText = dialogView.findViewById<EditText>(R.id.et_catatan_tambah)
            val notecontent = contentEditText.text.toString()
            val titlenote = titleEditText.text.toString()

            if (notecontent.isNotEmpty() && titlenote.isNotEmpty()) {
                val newNote = Note(note = notecontent, title = titlenote)
                noteViewModel.insert(newNote)
            }
        }
    }
}