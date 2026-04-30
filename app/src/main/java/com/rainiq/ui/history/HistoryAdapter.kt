package com.rainiq.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rainiq.databinding.ItemHistoryBinding
import com.rainiq.utils.formatIndian

data class HistoryEntry(
    val id: String,
    val day: String,
    val month: String,
    val rainfallMm: Float,
    val roofType: String,
    val savedLiters: Int
)

class HistoryAdapter(
    private val entries: MutableList<HistoryEntry>,
    private val onDelete: (HistoryEntry, Int) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(entry: HistoryEntry) {
            binding.tvDay.text = entry.day
            binding.tvMonth.text = entry.month
            binding.tvRainfall.text = "${entry.rainfallMm} mm rainfall"
            binding.tvRoofType.text = "${entry.roofType} Roof"
            binding.tvSavedLiters.text = "+ ${entry.savedLiters.formatIndian()} L"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(entries[position])
    }

    override fun getItemCount(): Int = entries.size

    fun removeAt(position: Int) {
        val entry = entries[position]
        entries.removeAt(position)
        notifyItemRemoved(position)
        onDelete(entry, position)
    }
}
