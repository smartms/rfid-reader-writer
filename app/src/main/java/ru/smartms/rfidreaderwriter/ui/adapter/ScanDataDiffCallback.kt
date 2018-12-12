package ru.smartms.rfidreaderwriter.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.smartms.rfidreaderwriter.db.entity.ScanData

class ScanDataDiffCallback : DiffUtil.ItemCallback<ScanData>() {

    override fun areItemsTheSame(oldItem: ScanData, newItem: ScanData): Boolean {
        return oldItem.barcode == newItem.barcode && oldItem.count == newItem.count
    }

    override fun areContentsTheSame(oldItem: ScanData, newItem: ScanData): Boolean {
        return oldItem == newItem
    }
}