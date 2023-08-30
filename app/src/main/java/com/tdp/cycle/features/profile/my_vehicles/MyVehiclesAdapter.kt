package com.tdp.cycle.features.profile.my_vehicles

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tdp.cycle.R
import com.tdp.cycle.databinding.ItemMyVehiclesBinding
import com.tdp.cycle.models.cycle_server.ElectricVehicle

class MyVehiclesAdapter(
    private val onMyVehicleSelected: (ElectricVehicle) -> Unit,
    private val context: Context?
) : ListAdapter<ElectricVehicle, MyVehiclesAdapter.ElectricVehicleModelViewHolder> (ElectricVehicleModelDiffCallback)  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectricVehicleModelViewHolder {
        val binding = ItemMyVehiclesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ElectricVehicleModelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ElectricVehicleModelViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ElectricVehicleModelViewHolder(private val binding: ItemMyVehiclesBinding) : RecyclerView.ViewHolder(binding.root){

         fun bind(electricVehicle: ElectricVehicle) {
             binding.apply {
                 itemMyVehiclesImage.setImageDrawable(context?.resources?.getDrawable(R.drawable.ic_car, null))
                 itemMyVehiclesName.text = electricVehicle.vehicleMeta?.brand
                 itemMyVehiclesModel.text = electricVehicle.vehicleMeta?.model
                 itemMyVehiclesContainer.isSelected = electricVehicle.isSelected ?: false
                 itemMyVehiclesContainer.setOnClickListener {
                     onMyVehicleSelected.invoke(electricVehicle)
                 }
             }
        }
    }
}

object ElectricVehicleModelDiffCallback : DiffUtil.ItemCallback<ElectricVehicle>() {
    override fun areItemsTheSame(oldItem: ElectricVehicle, newItem: ElectricVehicle): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: ElectricVehicle, newItem: ElectricVehicle): Boolean = oldItem == newItem
}
