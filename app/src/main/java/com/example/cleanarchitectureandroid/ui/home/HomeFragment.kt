package com.example.cleanarchitectureandroid.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cleanarchitectureandroid.App
import com.example.cleanarchitectureandroid.R
import com.example.cleanarchitectureandroid.base.InventoryViewModelFactory
import com.example.cleanarchitectureandroid.databinding.FragmentHomeBinding
import com.example.cleanarchitectureandroid.ui.add.AddViewModel
import com.example.cleanarchitectureandroid.ui.home.adapter.ItemAdapter

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: AddViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as App).database.roomDao()
        )
    }
    private  var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ItemAdapter {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailedFragment(it.id)
            this.findNavController().navigate(action)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter
        // Attach an observer on the allItems list to update the UI automatically when the data
        // changes.
        viewModel.allItems.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }

        binding.add.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToAddFragment(
                getString(R.string.add_fragment_title)
            )
            this.findNavController().navigate(action)
        }
    }
}