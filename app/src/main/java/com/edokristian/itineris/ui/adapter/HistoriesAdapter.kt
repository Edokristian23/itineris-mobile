package com.edokristian.itineris.ui.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Color.RED
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edokristian.itineris.R
import com.edokristian.itineris.databinding.ItemRiwayatPengajuanCutiBinding
import com.edokristian.itineris.model.response.DataX
import com.edokristian.itineris.model.response.GetLeaveHistoriesResponse

class HistoriesAdapter(val leaveHistories: List<DataX>) :
    RecyclerView.Adapter<HistoriesAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemRiwayatPengajuanCutiBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRiwayatPengajuanCutiBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return leaveHistories.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val histori = leaveHistories[position]
        holder.binding.tvLeaveType.text = histori.leave_type
        holder.binding.tvStartDate.text = "${histori.start_date} s/d ${histori.end_date}"
        holder.binding.tvReason.text = histori.reason
        holder.binding.tvStatus.text = histori.status

        if (histori.status == "approved") {
                holder.binding.tvStatus.text = histori.status
                holder.binding.tvStatus.setBackgroundResource(R.drawable.btn_action_terima)
        } else if(histori.status == "rejected"){
            holder.binding.tvStatus.text = histori.status
            holder.binding.tvStatus.setBackgroundResource(R.drawable.btn_action_tolak)
            holder.binding.tvReason.setTextColor(RED)
            holder.binding.tvReason.text = histori.rejection_note
        } else {
            holder.binding.tvStatus.text = histori.status
        }
    }
}