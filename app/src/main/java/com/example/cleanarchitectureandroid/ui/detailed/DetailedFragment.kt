package com.example.cleanarchitectureandroid.ui.detailed

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.cleanarchitectureandroid.App
import com.example.cleanarchitectureandroid.R
import com.example.cleanarchitectureandroid.databinding.FragmentAddBinding
import com.example.cleanarchitectureandroid.databinding.FragmentDeteilBinding
import com.example.cleanarchitectureandroid.model.User
import com.example.cleanarchitectureandroid.ui.add.AddViewModel
import com.example.cleanarchitectureandroid.ui.add.InventoryViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DetailedFragment : Fragment() {

    private val navigationArgs: DetailedFragmentArgs by navArgs()
    lateinit var user: User
    private var _binding: FragmentDeteilBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = DetailedFragment()
    }

    private val viewModel: AddViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as App).database.roomDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDeteilBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun bind(item: User) {
        binding.apply {
            itemRole.text = item.role
            itemName.text = item.firstName
            itemLastName.text = item.lastName
            itemAge.text = item.age.toString()
            sellItem.setOnClickListener { viewModel.sellItem(item) }
            deleteItem.setOnClickListener { showConfirmationDialog() }
            editItem.setOnClickListener { editItem() }
        }
    }

    private fun editItem() {
        val action = DetailedFragmentDirections.actionDetailedFragmentToAddFragment(
            getString(R.string.edit_fragment_title),
            user.id
        )
        this.findNavController().navigate(action)
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteItem()
            }
            .show()
    }

    private fun deleteItem() {
        viewModel.deleteItem(user)
        findNavController().navigateUp()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val id = navigationArgs.itemId
        viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
            user = selectedItem
            bind(user)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}