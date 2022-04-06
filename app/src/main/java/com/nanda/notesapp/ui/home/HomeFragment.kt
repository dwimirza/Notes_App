package com.nanda.notesapp.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.nanda.notesapp.R
import com.nanda.notesapp.data.entity.Notes
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.ui.NotesViewModel
import com.google.android.material.snackbar.Snackbar
import com.nanda.notesapp.databinding.FragmentHomeBinding
import com.nanda.notesapp.utils.ExtensionFunctions.setActionBar
import com.nanda.notesapp.utils.HelperFunctions
import com.nanda.notesapp.utils.HelperFunctions.checkIsDataEmpty


class HomeFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding as FragmentHomeBinding

    private val homeViewModel by viewModels<NotesViewModel>()

    private val homeAdapter by lazy { HomeAdapter() }

    private var _currentData: List<Notes>? = null
    private val currentData get() = _currentData as List<Notes>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mHelperFunctions = HelperFunctions
        setHasOptionsMenu(true)


        binding.apply {
            toolbarHome.setActionBar(requireActivity())
            fab.setOnClickListener{
                findNavController().navigate(R.id.action_homeFragment_to_addFragment)
            }
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rvNotes.apply {
            homeViewModel.getAllData().observe(viewLifecycleOwner){
                checkIsDataEmpty(it)
                showEmptyDataLayout(it)
                homeAdapter.setData(it)
                _currentData = it
            }
            adapter = homeAdapter
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

            swipeToDelete(this)
        }

    }

    private fun showEmptyDataLayout(data: List<Notes>) {
        when (data.isEmpty()) {
            true -> {binding.rvNotes.visibility = View.INVISIBLE
                    binding.imgNoData.visibility = View.VISIBLE}
            false -> {binding.rvNotes.visibility = View.VISIBLE
                    binding.imgNoData.visibility = View.INVISIBLE}
        }
    }

//    private fun checkIsDataEmpty(data: List<Notes>?) {
//        binding.apply {
//            if(data?.isEmpty() == true) {
//                imgNoData.visibility = View.VISIBLE
//                rvNotes.visibility = View.INVISIBLE
//            } else {
//                imgNoData.visibility = View.INVISIBLE
//                rvNotes.visibility = View.VISIBLE
//            }
//        }
//    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val search = menu.findItem(R.id.menu_search)
        val searchAction = search.actionView as? SearchView
        searchAction?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_high_priority -> homeViewModel.sortByHighPriority().observe(this) {dataHigh ->
                homeAdapter.setData(dataHigh)
            }
            R.id.menu_low_priority -> homeViewModel.sortByLowPriority().observe(this) {dataLow ->
                homeAdapter.setData(dataLow)
            }
            R.id.menu_delete_all -> confirmDeleteAll()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmDeleteAll(){
       if(currentData.isEmpty()){
           Toast.makeText(requireContext(), "No Notes", Toast.LENGTH_SHORT).show()
       }else{
           AlertDialog.Builder(requireContext())
               .setTitle("Delete All Your Notes ?")
               .setMessage("Are You Sure You Want To Clear All of This ?")
               .setPositiveButton("yes"){_, _ ->
                   homeViewModel.deleteAllData()
                   Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT)
                       .show()
               }
               .setNegativeButton("NO"){dialog, _ ->
                   dialog.dismiss()
               }
               .show()
       }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        val querySearch = "%$query%"
        query?.let {
            homeViewModel.searchByQuery(querySearch).observe(this) { data ->
                homeAdapter.setData(data)
            }
        }

        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        val querySearch = "%$newText%"
        newText?.let {
            homeViewModel.searchByQuery(querySearch).observe(this) { data ->
                homeAdapter.setData(data)
            }
        }

        return true
    }

    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeToDelete = object : ItemTouchHelper.SimpleCallback(
         0,
         ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
               val deletedItem = homeAdapter.listNotes[viewHolder.adapterPosition]
                homeViewModel.deleteNote(deletedItem)
                restoredData(viewHolder.itemView, deletedItem)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDelete)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoredData(view: View, deletedItem: Notes) {
        val snackBar = Snackbar.make(
            view,
            "Note Deleted",
            Snackbar.LENGTH_LONG
        )
        snackBar.setTextColor(ContextCompat.getColor(view.context, R.color.black))
        snackBar.setAction("UNDO") {
            homeViewModel.insertData(deletedItem)
        }
        snackBar.setActionTextColor(ContextCompat.getColor(view.context, R.color.black))
        snackBar.show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


