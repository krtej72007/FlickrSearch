package com.ravitej.flickrsearch.ui.searchList.listeners

import com.ravitej.flickrsearch.data.entities.dtos.ItemsDTO

interface OnSearchItemClickListener {

    fun onClick(item: ItemsDTO)
}