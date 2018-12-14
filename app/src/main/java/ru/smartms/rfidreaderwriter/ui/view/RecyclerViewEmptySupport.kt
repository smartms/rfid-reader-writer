package ru.smartms.rfidreaderwriter.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewEmptySupport : RecyclerView {

    private lateinit var emptyView: View

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    private val emptyObserver = object : RecyclerView.AdapterDataObserver() {

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            setEmptyView()
            super.onItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            setEmptyView()
            super.onItemRangeRemoved(positionStart, itemCount)
        }

        override fun onChanged() {
            setEmptyView()
            super.onChanged()
        }
    }

    fun setEmptyView() {
        if (adapter?.itemCount == 0) {
            emptyView.visibility = View.VISIBLE
            this@RecyclerViewEmptySupport.visibility = View.GONE
        } else {
            emptyView.visibility = View.GONE
            this@RecyclerViewEmptySupport.visibility = View.VISIBLE
        }
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(emptyObserver)
        emptyObserver.onChanged()
    }

    fun setEmptyView(emptyView: View) {
        this.emptyView = emptyView
    }
}