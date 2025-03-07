package com.edokristian.itineris.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edokristian.itineris.databinding.ItemPersetujuanBinding
import com.edokristian.itineris.model.response.DataX

class ApprovalAdapter(val approvalRequest: List<DataX>) :
        RecyclerView.Adapter<ApprovalAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemPersetujuanBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemPersetujuanBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return approvalRequest.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val approvReq = approvalRequest[position]
        if (approvReq.status == "pending"){
            holder.binding.tvName.text = approvReq.employee_id.toString()
            holder.binding.tvLeaveType.text = approvReq.leave_type
            holder.binding.tvStartEndDate.text = "${approvReq.start_date} s/d ${approvReq.end_date}"
            holder.binding.tvReason.text = approvReq.reason
        }
    }

}