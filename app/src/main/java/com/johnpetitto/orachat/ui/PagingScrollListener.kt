package com.johnpetitto.orachat.ui

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class PagingScrollListener(private val listener: PagingScrollListener.OnPageListener): RecyclerView.OnScrollListener() {
    private var loading: Boolean = false

    interface OnPageListener {
        fun loadPage()
    }

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val layoutManager = recyclerView!!.layoutManager as LinearLayoutManager

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (!loading) {
            if (firstVisibleItemPosition + visibleItemCount >= totalItemCount && firstVisibleItemPosition > 0) {
                loading = true
                listener.loadPage()
            }
        }
    }

    fun loadingComplete() {
        loading = false
    }
}
