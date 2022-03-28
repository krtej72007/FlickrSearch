package com.ravitej.flickrsearch.ui.searchList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ravitej.flickrsearch.R
import com.ravitej.flickrsearch.data.entities.dtos.ItemsDTO
import com.ravitej.flickrsearch.databinding.LayoutSearchOverviewBinding
import com.ravitej.flickrsearch.ui.searchList.listeners.OnSearchItemClickListener

class SearchListAdapter(private val itemClickListener: OnSearchItemClickListener) :
    ListAdapter<ItemsDTO, SearchListAdapter.ImageSearchViewHolder>(ImageSearchDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSearchViewHolder {
        val binding: LayoutSearchOverviewBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.layout_search_overview,
                parent,
                false
            )

        return ImageSearchViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: ImageSearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ImageSearchViewHolder(
        private val binding: LayoutSearchOverviewBinding,
        private val listener: OnSearchItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemsDTO) {
            Glide.with(binding.searchImage)
                .load(item.media.m)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.searchImage)

            binding.searchImageTitle.text = item.title
            binding.root.setOnClickListener { listener.onClick(item) }
        }
    }

    object ImageSearchDiff : DiffUtil.ItemCallback<ItemsDTO>() {
        override fun areItemsTheSame(oldItem: ItemsDTO, newItem: ItemsDTO): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: ItemsDTO, newItem: ItemsDTO): Boolean {
            return false
        }
    }
}