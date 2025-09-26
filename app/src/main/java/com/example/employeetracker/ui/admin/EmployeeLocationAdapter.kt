package com.example.employeetracker.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.employeetracker.databinding.ItemEmployeeLocationBinding
import java.text.SimpleDateFormat
import java.util.*

class EmployeeLocationAdapter : ListAdapter<EmployeeLocationData, EmployeeLocationAdapter.ViewHolder>(DiffCallback()) {
    
    private val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEmployeeLocationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class ViewHolder(private val binding: ItemEmployeeLocationBinding) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(data: EmployeeLocationData) {
            binding.apply {
                tvEmployeeName.text = data.employee.name
                tvEmail.text = data.employee.email
                
                // Set status
                val isActive = data.activeSession != null
                tvStatus.text = if (isActive) "ACTIVE" else "INACTIVE"
                tvStatus.setTextColor(
                    if (isActive) 
                        root.context.getColor(android.R.color.holo_green_dark)
                    else 
                        root.context.getColor(android.R.color.holo_red_dark)
                )
                
                // Set location info
                if (data.lastLocation != null) {
                    val locationText = "Last Location: ${String.format("%.6f", data.lastLocation.latitude)}, ${String.format("%.6f", data.lastLocation.longitude)}"
                    tvLastLocation.text = locationText
                    tvLastUpdate.text = "Last Update: ${dateFormat.format(data.lastLocation.timestamp)}"
                } else {
                    tvLastLocation.text = "Last Location: No data"
                    tvLastUpdate.text = "Last Update: Never"
                }
            }
        }
    }
    
    private class DiffCallback : DiffUtil.ItemCallback<EmployeeLocationData>() {
        override fun areItemsTheSame(oldItem: EmployeeLocationData, newItem: EmployeeLocationData): Boolean {
            return oldItem.employee.id == newItem.employee.id
        }
        
        override fun areContentsTheSame(oldItem: EmployeeLocationData, newItem: EmployeeLocationData): Boolean {
            return oldItem.employee == newItem.employee &&
                   oldItem.lastLocation?.timestamp == newItem.lastLocation?.timestamp &&
                   oldItem.activeSession?.isActive == newItem.activeSession?.isActive
        }
    }
}