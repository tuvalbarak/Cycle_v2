package com.tdp.cycle.features.profile.gamification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tdp.cycle.R
import com.tdp.cycle.databinding.ItemGamificationBinding
import com.tdp.cycle.models.cycle_server.Gamification

class GamificationsAdapter : ListAdapter<Gamification, GamificationsAdapter.GamificationViewHolder>(GamificationDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamificationViewHolder {
        val binding = ItemGamificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GamificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GamificationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class GamificationViewHolder(val binding: ItemGamificationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(gamification: Gamification) {
            binding.apply {
                val textColor =
                    if((gamification.amount ?: 0) >= 0) root.resources.getColor(R.color.positive, null)
                    else root.resources.getColor(R.color.negative, null)

                gamificationAmount.setTextColor(textColor)
                gamificationAmount.text = gamification.amount.toString()
                gamificationDescription.text = gamification.description.toString()
            }
        }
    }
}

object GamificationDiffCallback : DiffUtil.ItemCallback<Gamification>() {
    override fun areItemsTheSame(oldItem: Gamification, newItem: Gamification): Boolean = oldItem == newItem
    override fun areContentsTheSame(oldItem: Gamification, newItem: Gamification): Boolean = oldItem == newItem
}
