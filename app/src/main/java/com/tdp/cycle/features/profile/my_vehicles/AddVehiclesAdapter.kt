package com.tdp.cycle.features.profile.my_vehicles

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tdp.cycle.R
import com.tdp.cycle.databinding.ItemAddVehicleBinding
import com.tdp.cycle.models.cycle_server.ElectricVehicle
import com.tdp.cycle.models.cycle_server.VehicleMeta

class AddVehiclesAdapter(
    private val onMyVehicleSelected: (VehicleMeta) -> Unit,
    private val context: Context?
) : ListAdapter<VehicleMeta, AddVehiclesAdapter.AddVehicleViewHolder> (AddVehicleDiffCallback)  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddVehicleViewHolder {
        val binding = ItemAddVehicleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddVehicleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddVehicleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AddVehicleViewHolder(private val binding: ItemAddVehicleBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(electricVehicle: VehicleMeta) {
            binding.apply {
                itemAddVehiclesImage.setImageDrawable(context?.resources?.getDrawable(R.drawable.ic_car, null))
                itemAddVehiclesName.text = electricVehicle.brand
                itemAddVehiclesModel.text = electricVehicle.model
                itemAddVehiclesContainer.isSelected = electricVehicle.isSelected ?: false
                itemAddVehiclesContainer.setOnClickListener {
                    onMyVehicleSelected.invoke(electricVehicle)
                }
            }
        }
    }
}

object AddVehicleDiffCallback : DiffUtil.ItemCallback<VehicleMeta>() {
    override fun areItemsTheSame(oldItem: VehicleMeta, newItem: VehicleMeta): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: VehicleMeta, newItem: VehicleMeta): Boolean = oldItem == newItem
}
