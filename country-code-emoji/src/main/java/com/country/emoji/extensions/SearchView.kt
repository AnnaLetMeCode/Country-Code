package com.country.emoji.extensions

import androidx.appcompat.widget.SearchView

fun SearchView.doOnQueryChanged(callback: ((String, Boolean) -> Unit)) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            callback(query.orEmpty(), false)
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            callback(newText.orEmpty(), true)
            return true
        }
    })
}
