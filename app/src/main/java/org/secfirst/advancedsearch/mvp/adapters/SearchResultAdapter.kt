package org.secfirst.advancedsearch.mvp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.search_result_item.view.*
import org.secfirst.advancedsearch.R
import org.secfirst.advancedsearch.models.SearchResult

class SearchResultAdapter(private val results : MutableList<SearchResult>, private val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SearchResultViewHolder(LayoutInflater.from(context).inflate(R.layout.search_result_item, parent, false))
    }

    override fun getItemCount(): Int = results.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SearchResultViewHolder).resultTitle.text = results[position].title
    }

    fun append(vararg result: SearchResult) {
        results.addAll(result)
        notifyDataSetChanged()
    }

    fun reset() {
        results.clear()
        notifyDataSetChanged()
    }

    class SearchResultViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val resultTitle: TextView = view.searchResultTitle
    }

}