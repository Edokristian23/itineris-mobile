package com.edokristian.itineris

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.edokristian.itineris.databinding.ActivityMainBinding
import com.edokristian.itineris.model.response.GetCurrentEmployeeResponse
import com.edokristian.itineris.model.response.GetEmployeeResponse
import com.edokristian.itineris.model.response.GetLeaveHistoriesResponse
import com.edokristian.itineris.services.ApiClient
import com.edokristian.itineris.services.ApiService
import com.edokristian.itineris.ui.adapter.HistoriesAdapter
import com.edokristian.itineris.ui.dashboard.DashboardApproverActivity
import com.edokristian.itineris.ui.form_pengajuan_cuti.FormPengajuanCutiActivity
import com.edokristian.itineris.ui.login.LoginActivity
import com.edokristian.itineris.ui.riwayat_pengajuan_cuti.RiwayatPengajuanCutiActivity
import com.edokristian.itineris.utils.Constant
import com.edokristian.itineris.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager
    private val api: ApiService = ApiClient.getRetroClientInstance().create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        sessionManager = SessionManager(this)



        CoroutineScope(Dispatchers.IO).launch {
            try {
                    getUser()

            } catch (e: Exception){
                throw Exception(e.message)
            }
        }

        binding.formPengajuanCuti.setOnClickListener {
            val intent = Intent(this, FormPengajuanCutiActivity::class.java)
            startActivity(intent)
        }

        binding.riwayatPengajuanCuti.setOnClickListener {
//            Toast.makeText(this, "${sessionManager.getString(Constant.PREFS_USER_ID)}", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, RiwayatPengajuanCutiActivity::class.java)
            startActivity(intent)
        }

        binding.tvCurrentDate.text = getCurrentDate()
        binding.tvCurrentTime.text = getCurrentTime()
        binding.btnLeaveRequest.setOnClickListener {
            val intent = Intent(this, FormPengajuanCutiActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            sessionManager.clear()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun getCurrentDate():String{
        val sdf = SimpleDateFormat("dd MMMM yyyy")
        return sdf.format(Date())
    }

    private fun getCurrentTime():String{
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(Date())
    }

    private suspend fun getUser(): GetCurrentEmployeeResponse? {
        val response = api.getEmployee("Bearer ${sessionManager.getString(Constant.PREFS_USER_TOKEN)}")

        if (response.isSuccessful) {
            val user = response.body()
            withContext(Dispatchers.Main) {
                binding.tvName.text = user!!.data.name
                binding.tvNip.text = user.data.nip
                binding.tvPosition.text = user.data.position
                binding.tvLeaveBalance.text = user.data.leave_balance.toString()
                binding.tvDepartment.text = user.data.department
                binding.tvProcessedLeave.text = user.data.processed_leave.toString()
                binding.tvTotalLeave.text = user.data.total_leave.toString()

            }
            return user
        } else {
            withContext(Dispatchers.Main) {

            }
            return null
        }
    }

}