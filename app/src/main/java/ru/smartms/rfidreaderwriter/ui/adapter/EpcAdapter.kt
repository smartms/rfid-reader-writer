package ru.smartms.rfidreaderwriter.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_scan_data.view.*
import ru.smartms.rfidreaderwriter.R
import ru.smartms.rfidreaderwriter.db.entity.ScanData

internal class EpcAdapter(private val clickListener: (ScanData) -> Unit) :
    ListAdapter<ScanData, EpcAdapter.ViewHolder>(ScanDataDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_scan_data, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(scanData: ScanData, clickListener: (ScanData) -> Unit) {
            itemView.tv_epc.text = scanData.barcode
            itemView.setOnClickListener { clickListener(scanData) }
        }
    }
}
