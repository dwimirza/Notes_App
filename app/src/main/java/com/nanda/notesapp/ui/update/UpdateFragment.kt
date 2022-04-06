package com.nanda.notesapp.ui.update

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notesapp.ui.NotesViewModel
import com.nanda.notesapp.R
import com.nanda.notesapp.data.NotesRepository
import com.nanda.notesapp.data.entity.Notes
import com.nanda.notesapp.databinding.FragmentUpdateBinding
import com.nanda.notesapp.utils.ExtensionFunctions.setActionBar
import com.nanda.notesapp.utils.HelperFunctions.parseToPriority
import com.nanda.notesapp.utils.HelperFunctions.spinnerListener
import java.text.SimpleDateFormat
import java.util.*


class UpdateFragment : Fragment() {

    private var _binding : FragmentUpdateBinding? = null
    private val binding get() = _binding as FragmentUpdateBinding

    private val args by navArgs<UpdateFragmentArgs>()

    private val updateViewModel by viewModels<NotesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //inisialisasi variabel dataBinding yang ada di XML
        binding.safeArgs = args

        setHasOptionsMenu(true)

        binding.apply {
            toolbarUpdate.setActionBar(requireActivity())
            spinnerPrioritiesUpdate.onItemSelectedListener = spinnerListener(context, binding.priorityIndicator)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_save, menu)
        val action = menu.findItem(R.id.action_save)
        action.actionView.findViewById<AppCompatImageButton>(R.id.btn_save).setOnClickListener {
            updateNote()
        }
    }

    private fun updateNote(){
        binding.apply {
            val title = edtTitleUpdate.text.toString()
            val priority = spinnerPrioritiesUpdate.selectedItem.toString()
            val description = edtDescriptionUpdate.text.toString()

            val calender = Calendar.getInstance().time
            val date = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(calender)

            val notes = Notes(
                args.currentItem.id,
                title,
                parseToPriority(priority, context),
                description,
                date)

            //Make Some Decicion if title empty show error massage
            //and if the description is empty show alert
            if (edtTitleUpdate.text.isEmpty()) {
                edtTitleUpdate.error = "Please Fill Field"
            } else if (edtDescriptionUpdate.text.isEmpty()) {
                Toast.makeText(context, "Your Notes Is still empty", Toast.LENGTH_LONG).show()
            } else {
                updateViewModel.updateNote(notes)
                val action = UpdateFragmentDirections.actionUpdateFragmentToDetailFragment(notes)
                findNavController().navigate(action)
                Toast.makeText(context, "successful add note.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}