package com.edokristian.itineris.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.edokristian.itineris.R
import com.edokristian.itineris.databinding.ItemPersetujuanBinding
import com.edokristian.itineris.model.response.DataX
import com.google.android.material.bottomsheet.BottomSheetDialog

class ApprovalAdapter(val context: Context, val approvalRequest: List<DataX>) :
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

            holder.binding.btnApproved.setOnClickListener {
                val  bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(context)
                bottomSheetDialog.setContentView(R.layout.dialog_persetujuan_diterima)

                var tvLeaveType = bottomSheetDialog.findViewById<TextView>(R.id.tv_leave_type)
                var tvStartEndDate = bottomSheetDialog.findViewById<TextView>(R.id.tv_start_end_date)
                var tvReason = bottomSheetDialog.findViewById<TextView>(R.id.tv_reason)
                var tvClose = bottomSheetDialog.findViewById<ImageView>(R.id.iv_close)

                tvLeaveType!!.text = approvReq.leave_type
                tvStartEndDate!!.text = "${approvReq.start_date} s/d ${approvReq.end_date}"
                tvReason!!.text = approvReq.reason

                tvClose!!.setOnClickListener {
                    bottomSheetDialog.dismiss()
                }

                bottomSheetDialog.show()
            }
        }
    }

}