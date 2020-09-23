package com.doodleblue.innovations.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doodleblue.innovations.R
import com.doodleblue.innovations.databinding.ItemContactDataBinding
import com.doodleblue.innovations.model.ContactData
import java.util.*

class ContactRvAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var dataList: MutableList<ContactData> = ArrayList()
    private var mOnItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding = DataBindingUtil.inflate<ItemContactDataBinding>(
            LayoutInflater.from(context),
            R.layout.item_contact_data, parent, false
        )
        return PeopleViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as PeopleViewHolder
        viewHolder.setData(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    private fun add(data: ContactData) {
        dataList.add(data)
        notifyItemInserted(dataList.size - 1)
    }

    fun addAtPosition(position: Int, data: ContactData) {
        dataList.add(position, data)
        notifyItemInserted(position)
    }

    fun addAll(dataList: List<ContactData>) {
        dataList.forEach { factsRow ->
            add(factsRow)
        }
    }

    private fun remove(data: ContactData) {
        val position = dataList.indexOf(data)
        if (position > -1) {
            dataList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        dataList.clear()
        notifyDataSetChanged()
    }

    fun getItem(position: Int): ContactData {
        return dataList[position]
    }

    fun setItemAtPosition(position: Int, data: ContactData) {
        dataList[position] = data
        notifyItemChanged(position)
    }

    fun removeItem(position: Int) {
        remove(dataList[position])
    }

    fun getFactsRowsList(): List<ContactData>? {
        return dataList
    }

    fun setPeopleList(dataList: MutableList<ContactData>) {
        this.dataList = dataList
    }

    fun size(): Int {
        return dataList.size
    }

    internal inner class PeopleViewHolder(var itemBinding: ItemContactDataBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun setData(data: ContactData) {
            itemBinding.contactData = data

            itemBinding.rlRoot.setOnClickListener {
                mOnItemClickListener?.onItemClick(data, adapterPosition)
            }
        }
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(data: ContactData, position: Int)
    }
}
