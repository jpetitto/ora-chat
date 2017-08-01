package com.johnpetitto.orachat.data;

import com.google.gson.annotations.SerializedName;

public class Pagination {
    private @SerializedName("current_page") int currentPage;
    private @SerializedName("per_page") int perPage;
    private @SerializedName("page_count") int pageCount;
    private @SerializedName("total_count") int totalCount;

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPerPage() {
        return perPage;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getTotalCount() {
        return totalCount;
    }
}
