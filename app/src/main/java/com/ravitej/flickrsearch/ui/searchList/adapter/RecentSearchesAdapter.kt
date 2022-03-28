package com.ravitej.flickrsearch.ui.searchList.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ravitej.flickrsearch.R
import com.ravitej.flickrsearch.databinding.LayoutRecentSearchesBinding
import com.ravitej.flickrsearch.ui.searchList.listeners.OnRecentSearchItemClickListener
import com.ravitej.flickrsearch.ui.searchList.adapter.RecentSearchesAdapter.RecentSearchesViewHolder

class RecentSearchesAdapter (private val recentSearchListener: OnRecentSearchItemClickListener):
    ListAdapter<String, RecentSearchesViewHolder>(RecentSearchDiffUtil) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchesViewHolder {
        val binding: LayoutRecentSearchesBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.layout_recent_searches,
            parent,
            false
        )

        return RecentSearchesViewHolder(binding, recentSearchListener)
    }

    override fun onBindViewHolder(holder: RecentSearchesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class RecentSearchesViewHolder(
        private val binding: LayoutRecentSearchesBinding,
        private val recentSearchListener: OnRecentSearchItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.recentItem.text = item
            binding.recentItem.setOnClickListener { recentSearchListener.onRecentSearchItemClick(item) }
        }
    }

    object RecentSearchDiffUtil : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}