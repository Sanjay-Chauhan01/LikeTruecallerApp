package com.doodleblue.innovations.ui.fragment

import android.app.AlertDialog
import android.content.ContentResolver
import android.content.DialogInterface
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.doodleblue.innovations.R
import com.doodleblue.innovations.databinding.FragmentSecondBinding
import com.doodleblue.innovations.model.ContactData
import com.doodleblue.innovations.ui.adapter.ContactRvAdapter
import com.doodleblue.innovations.viewmodel.ContactViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber


class SetNicknameFragment : Fragment() {

    private lateinit var binding: FragmentSecondBinding
    private lateinit var contactViewModel: ContactViewModel
    private var contactRvAdapter: ContactRvAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_second, container, false)
        binding.secondFragment = this
        contactViewModel = ViewModelProvider(requireActivity()).get(ContactViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initObserver()

        GlobalScope.launch {
            if (contactViewModel.contactDataList.value == null || contactViewModel.contactDataList.value?.isEmpty()!!) {
                contactViewModel.isShowProgressDialog.postValue(true)
                contactViewModel.contactDataList.postValue(getAllContacts())
            }
        }

//        view.findViewById<Button>(R.id.button_second).setOnClickListener {
//            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
//        }
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
                showSetNicknameDialog(data, position)
            }
        })
    }

    /***
     * This method is use to initialize observer
     */
    private fun initObserver() {
        contactViewModel.contactDataList.observe(requireActivity(), Observer { contactData ->
            contactData.forEach {
                Timber.e("Size : %s = %s", it.contactName, it.contactNumber)
            }
//            val displayContactList: ArrayList<ContactData> = arrayListOf()
            /*contactViewModel.allContactData.value?.forEach { data ->
                if (!contactViewModel.allContactData.value?.contains(data)!!) {
                    displayContactList.add(data)
                    Timber.e("Contact data Not match")
                }
            }*/
            contactRvAdapter?.clear()
            contactRvAdapter?.addAll(contactData)

            if (contactData.isEmpty()) {
                binding.tvNoDataFound.visibility = View.VISIBLE
            } else {
                binding.tvNoDataFound.visibility = View.GONE
            }
            contactViewModel.isShowProgressDialog.postValue(false)
        })
    }

    private fun getAllContacts(): ArrayList<ContactData> {
        val contactList: ArrayList<ContactData> = ArrayList()
        val cr: ContentResolver = requireActivity().contentResolver
        val cur: Cursor? = cr.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null
        )
        while (cur?.moveToNext()!!) {
            val contactData = ContactData()
            val id = cur.getString(
                cur.getColumnIndex(ContactsContract.Contacts._ID)
            )
            val name = cur.getString(
                cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            )
            if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                val pCur = cr.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    arrayOf(id),
                    null
                )
                while (pCur!!.moveToNext()) {
                    val phoneNo = pCur.getString(
                        pCur.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                        )
                    )
                    contactData.contactName = name
                    contactData.contactNumber = phoneNo
                    contactList.add(contactData)
                }
                pCur.close()
            }
        }
        cur.close()
        return contactList
    }

    private fun showSetNicknameDialog(data: ContactData, position: Int) {
        val input = EditText(requireContext())
        input.hint = getString(R.string.lbl_set_nickname)
        input.isSingleLine = true
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp

        val alertDialog: AlertDialog = AlertDialog.Builder(requireContext())
            .setView(input)
            .setCancelable(false)
            .setTitle(getString(R.string.lbl_set_nickname))
            .setPositiveButton(R.string.lbl_save, null)
            .setNegativeButton(R.string.lbl_cancel, null)
            .create()
        alertDialog.setOnShowListener { dialog ->
            val buttonPositive: Button =
                (dialog as AlertDialog).getButton(DialogInterface.BUTTON_POSITIVE)
            buttonPositive.setOnClickListener {
                if (TextUtils.isEmpty(input.text)) {
                    input.error = "This field can't be empty!"
                } else {
                    dialog.dismiss()
                    data.contactName = input.text.toString()
                    contactViewModel.insertNewsSource(data)
                    contactRvAdapter?.removeItem(position)
                }
            }
            val buttonNegative =
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            buttonNegative.setOnClickListener {
                dialog.dismiss()
            }
        }
        alertDialog.show()
    }
}