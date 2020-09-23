package com.doodleblue.innovations.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.doodleblue.innovations.R
import com.doodleblue.innovations.databinding.FragmentFirstBinding
import com.doodleblue.innovations.model.ContactData
import com.doodleblue.innovations.ui.adapter.ContactRvAdapter
import com.doodleblue.innovations.viewmodel.ContactViewModel
import com.newsapp.constants.TAG_ASK_PERMISSION
import timber.log.Timber

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding
    private lateinit var contactViewModel: ContactViewModel
    private var contactRvAdapter: ContactRvAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_first, container, false)
        binding.firstFragment = this
        contactViewModel = ViewModelProvider(requireActivity()).get(ContactViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initObserver()

        contactViewModel.allContactData
    }

    /***
     * This method is use to initialize observer
     */
    private fun initObserver() {
        contactViewModel.allContactData.observe(requireActivity(), Observer { contactData ->
            contactData.forEach {
                Timber.e("Data : %s = %s", it.contactName, it.contactNumber)
            }
            contactRvAdapter?.clear()
            contactRvAdapter?.addAll(contactData)

            if (contactData.isEmpty()) {
                binding.tvNoDataFound.visibility = View.VISIBLE
            } else {
                binding.tvNoDataFound.visibility = View.GONE
            }
        })
    }

    private fun initAdapter() {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.rvList.layoutManager = linearLayoutManager

        val itemDecorator = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(
            ContextCompat.getDrawable(requireContext(), R.drawable.divider)!!
        )
        binding.rvList.addItemDecoration(itemDecorator)
        contactRvAdapter = ContactRvAdapter(requireContext())
        binding.rvList.adapter = contactRvAdapter
        binding.rvList.setHasFixedSize(true)
        contactRvAdapter?.setItemClickListener(object : ContactRvAdapter.OnItemClickListener {
            override fun onItemClick(data: ContactData, position: Int) {
                //
            }
        })
    }

    fun onAddContactClick() {
        if (contactViewModel.isPermissionGranted.value!!) {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        } else {
            contactViewModel.notifyActivity.postValue(TAG_ASK_PERMISSION)
        }
    }
}