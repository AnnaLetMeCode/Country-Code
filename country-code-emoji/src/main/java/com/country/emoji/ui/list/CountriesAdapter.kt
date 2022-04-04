package com.country.emoji.ui.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.country.emoji.databinding.LayoutItemCountryBinding

class CountriesAdapter(
    private val onItemClick: ((CountryItem) -> Unit)?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val differ = AsyncListDiffer(this, DiffCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemCountry = LayoutItemCountryBinding.inflate(layoutInflater, parent, false)
        return PostHolder(itemCountry)
    }

    override fun getItemCount() = currentList().size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        (differ.currentList[position]).let {
            (holder as PostHolder).bind(it)
        }

    fun submitList(newList: List<CountryItem>) = differ.submitList(newList)

    private fun currentList(): List<CountryItem> = differ.currentList

    inner class PostHolder(private val binding: LayoutItemCountryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(differ.currentList[bindingAdapterPosition])
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: CountryItem) {
            with(binding) {
                tvRclCountryName.text = item.countryName
                tvRclCountryCode.isVisible = item.isCodeVisible
                tvRclCountryCode.text = item.codeText
                tvRclCountryCode.setTypeface(null, item.typeface)
            }
        }
    }
}

private class DiffCallback : DiffUtil.ItemCallback<CountryItem>() {
    override fun areItemsTheSame(oldItem: CountryItem, newItem: CountryItem) =
        oldItem.countryCode == newItem.countryCode

    override fun areContentsTheSame(oldItem: CountryItem, newItem: CountryItem) =
        oldItem == newItem
}
