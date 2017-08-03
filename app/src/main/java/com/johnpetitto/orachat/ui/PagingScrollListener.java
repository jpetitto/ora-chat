package com.johnpetitto.orachat.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class PagingScrollListener extends RecyclerView.OnScrollListener {
    private OnPageListener listener;
    private boolean loading;

    public interface OnPageListener {
        void loadPage();
    }

    public PagingScrollListener(OnPageListener listener) {
        this.listener = listener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!loading) {
            if (firstVisibleItemPosition + visibleItemCount >= totalItemCount && firstVisibleItemPosition > 0) {
                loading = true;
                listener.loadPage();
            }
        }
    }

    public void loadingComplete() {
        loading = false;
    }
}
