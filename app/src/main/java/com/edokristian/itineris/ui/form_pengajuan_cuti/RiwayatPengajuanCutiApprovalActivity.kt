package com.edokristian.itineris.ui.form_pengajuan_cuti

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.edokristian.itineris.MainActivity
import com.edokristian.itineris.R
import com.edokristian.itineris.databinding.ActivityRiwayatPengajuanCutiApprovalBinding
import com.edokristian.itineris.databinding.ActivityRiwayatPengajuanCutiBinding
import com.edokristian.itineris.model.response.GetLeaveHistoriesResponse
import com.edokristian.itineris.services.ApiClient
import com.edokristian.itineris.services.ApiService
import com.edokristian.itineris.ui.adapter.HistoriesAdapter
import com.edokristian.itineris.ui.dashboard.DashboardApproverActivity
import com.edokristian.itineris.ui.login.LoginActivity
import com.edokristian.itineris.ui.persetujuan.PersetujuanActivity
import com.edokristian.itineris.utils.Constant
import com.edokristian.itineris.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RiwayatPengajuanCutiApprovalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRiwayatPengajuanCutiApprovalBinding
    private lateinit var sessionManager: SessionManager
    private val api: ApiService = ApiClient.getRetroClientInstance().create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRiwayatPengajuanCutiApprovalBinding.inflate(layoutInflater)

        setContentView(binding.root)

        sessionManager = SessionManager(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        CoroutineScope(Dispatchers.IO).launch {
            try {
                getAllLeaveHistories()
            } catch (e: Exception){
                throw Exception(e.message)
            }
        }

        binding.btnHome.setOnClickListener {
            val intent = Intent(this, DashboardApproverActivity::class.java)
            startActivity(intent)
        }


        binding.btnLogout.setOnClickListener {
            sessionManager.clear()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private suspend fun getAllLeaveHistories(): GetLeaveHistoriesResponse? {
        val response = api.getAllLeaveHistories("Bearer ${sessionManager.getString(Constant.PREFS_USER_TOKEN)}")

        if (response.isSuccessful) {
            val histori = response.body()
            withContext(Dispatchers.Main) {
                binding.rvLeaveHistories.adapter = HistoriesAdapter(histori!!.data)
                binding.rvLeaveHistories.layoutManager = LinearLayoutManager(this@RiwayatPengajuanCutiApprovalActivity)
                histori.data.forEach { dataX ->
                    Log.e("EDO", "getLeaveHistories: ${dataX.id}", )
                }
            }
            return histori
        } else {
            withContext(Dispatchers.Main) {

            }
            return null
        }
    }
}