package com.edokristian.itineris.ui.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.edokristian.itineris.R
import com.edokristian.itineris.databinding.ItemPersetujuanBinding
import com.edokristian.itineris.model.request.ApprovalRequest
import com.edokristian.itineris.model.response.ApprovalResponse
import com.edokristian.itineris.model.response.DataX
import com.edokristian.itineris.services.ApiClient
import com.edokristian.itineris.services.ApiService
import com.edokristian.itineris.ui.form_pengajuan_cuti.FormPengajuanCutiActivity
import com.edokristian.itineris.ui.persetujuan.PersetujuanActivity
import com.edokristian.itineris.utils.Constant
import com.edokristian.itineris.utils.SessionManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ApprovalAdapter(val context: Context, val approvalRequest: List<DataX>) :
        RecyclerView.Adapter<ApprovalAdapter.ViewHolder>() {

    private lateinit var sessionManager: SessionManager
    private val api: ApiService = ApiClient.getRetroClientInstance().create(ApiService::class.java)


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
                val  bottomSheetDialog= BottomSheetDialog(context)
                bottomSheetDialog.setContentView(R.layout.dialog_persetujuan_diterima)

                var tvLeaveType = bottomSheetDialog.findViewById<TextView>(R.id.tv_leave_type)
                var tvStartEndDate = bottomSheetDialog.findViewById<TextView>(R.id.tv_start_end_date)
                var tvReason = bottomSheetDialog.findViewById<TextView>(R.id.tv_reason)
                var tvClose = bottomSheetDialog.findViewById<ImageView>(R.id.iv_close)
                val btnTerima = bottomSheetDialog.findViewById<Button>(R.id.btn_terima_persetujuan)

                tvLeaveType!!.text = approvReq.leave_type
                tvStartEndDate!!.text = "${approvReq.start_date} s/d ${approvReq.end_date}"
                tvReason!!.text = approvReq.reason

                tvClose!!.setOnClickListener {
                    bottomSheetDialog.dismiss()
                }

                bottomSheetDialog.show()

                sessionManager = SessionManager(context)

                btnTerima!!.setOnClickListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            approvalInAction(
                                ApprovalRequest(
                                    id = approvReq.id,
                                    rejection_note = "",
                                    status = 1
                                )
                            )
                            bottomSheetDialog.dismiss()
                            val intent = Intent(context, PersetujuanActivity::class.java)
                            context.startActivity(intent)

                        } catch (e: Exception){
                            throw Exception(e.message)
                        }
                    }
                }

            }

            holder.binding.btnRejected.setOnClickListener {
                val  bottomSheetDialog= BottomSheetDialog(context)
                bottomSheetDialog.setContentView(R.layout.dialog_persetujuan_ditolak)

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

    private suspend fun approvalInAction(approvalRequest: ApprovalRequest): ApprovalResponse? {
        val response = api.approvalAction("Bearer ${sessionManager.getString(Constant.PREFS_USER_TOKEN)}",
            approvalRequest
        )

        if (response.isSuccessful) {
            val approval = response.body()
            withContext(Dispatchers.Main) {
                Log.e("Approval", "approvalInAction: ${approval!!.message}", )
            }
            return approval
        } else {
            withContext(Dispatchers.Main) {
                Log.e("Approval", "approvalInAction: upss", )
            }
            return null
        }
    }

}