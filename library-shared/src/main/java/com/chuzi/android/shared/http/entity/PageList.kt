package com.chuzi.android.shared.http.entity
import com.google.gson.annotations.SerializedName


data class PageList<T>(
    @SerializedName("total_count")
    var totalCount: Int? = null,
    @SerializedName("incomplete_results")
    var incompleteResults: Boolean? = null,
    @SerializedName("items")
    var items: List<T>? = null
)