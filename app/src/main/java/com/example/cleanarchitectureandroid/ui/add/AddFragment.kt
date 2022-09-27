package com.example.cleanarchitectureandroid.ui.add

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.cleanarchitectureandroid.App
import com.example.cleanarchitectureandroid.base.InventoryViewModelFactory
import com.example.cleanarchitectureandroid.databinding.FragmentAddBinding
import com.example.cleanarchitectureandroid.model.User

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as App).database.roomDao()
        )
    }

    private val navigationArgs: AddFragmentArgs by navArgs()
    lateinit var user: User

    companion object {
        fun newInstance() = AddFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.roleGetTv.text.toString(),
            binding.addName.text.toString(),
            binding.addLastName.text.toString(),
            binding.addAge.text.toString().toInt()
        )
    }

    private fun bind(item: User) {
        binding.apply {
            roleGetTv.setText(item.role, TextView.BufferType.SPANNABLE)
            addName.setText(item.firstName, TextView.BufferType.SPANNABLE)
            addLastName.setText(item.lastName, TextView.BufferType.SPANNABLE)
            addAge.setText(item.age.toString(), TextView.BufferType.SPANNABLE)
            save.setOnClickListener { updateItem() }
        }
    }

    private fun addNewItem() {
        if (isEntryValid()) {
            viewModel.addNewItem(
                binding.roleGetTv.text.toString(),
                binding.addName.text.toString(),
                binding.addLastName.text.toString(),
                binding.addAge.text.toString().toInt(),
            )
            val action = AddFragmentDirections.actionAddFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }

    private fun updateItem() {
        if (isEntryValid()) {
            viewModel.updateItem(
                this.navigationArgs.itemId,
                this.binding.roleGetTv.text.toString(),
                this.binding.addName.text.toString(),
                this.binding.addLastName.text.toString(),
                this.binding.addAge.text.toString().toInt()
            )
            val action = AddFragmentDirections.actionAddFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.itemId
        if (id > 0) {
            viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
                user = selectedItem
                bind(user)
            }
        } else {
            binding.save.setOnClickListener {
                addNewItem()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
}