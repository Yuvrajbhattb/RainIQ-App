package com.rainiq.ui.tips

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rainiq.databinding.ItemAiTipBinding

data class AiTip(
    val category: String,
    val title: String,
    val body: String,
    val savings: String
)

class AiTipsAdapter(private val tips: List<AiTip>) : RecyclerView.Adapter<AiTipsAdapter.TipViewHolder>() {

    inner class TipViewHolder(val binding: ItemAiTipBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tip: AiTip) {
            binding.tvCategory.text = tip.category
            binding.tvTitle.text = tip.title
            binding.tvBody.text = tip.body
            binding.tvSavings.text = tip.savings
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipViewHolder {
        val binding = ItemAiTipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TipViewHolder, position: Int) {
        holder.bind(tips[position])
    }

    override fun getItemCount(): Int = tips.size
}
