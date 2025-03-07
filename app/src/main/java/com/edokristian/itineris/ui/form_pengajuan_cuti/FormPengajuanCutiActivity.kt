package com.edokristian.itineris.ui.form_pengajuan_cuti

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.edokristian.itineris.MainActivity
import com.edokristian.itineris.R
import com.edokristian.itineris.databinding.ActivityFormPengajuanCutiBinding
import com.edokristian.itineris.model.request.LeaveRequest
import com.edokristian.itineris.model.request.LoginRequest
import com.edokristian.itineris.model.response.GetCurrentEmployeeResponse
import com.edokristian.itineris.model.response.GetEmployeeResponse
import com.edokristian.itineris.model.response.LeaveResponse
import com.edokristian.itineris.model.response.LoginResponse
import com.edokristian.itineris.services.ApiClient
import com.edokristian.itineris.services.ApiService
import com.edokristian.itineris.ui.login.LoginActivity
import com.edokristian.itineris.ui.riwayat_pengajuan_cuti.RiwayatPengajuanCutiActivity
import com.edokristian.itineris.utils.Constant
import com.edokristian.itineris.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FormPengajuanCutiActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityFormPengajuanCutiBinding
    private lateinit var sessionManager: SessionManager
    private val api: ApiService = ApiClient.getRetroClientInstance().create(ApiService::class.java)
    private lateinit var spinner: Spinner
    private lateinit var dataSpinner: Any

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityFormPengajuanCutiBinding.inflate(layoutInflater)

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

        val myCalendar = Calendar.getInstance()

        val datePickerStart = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateStartDate(myCalendar)
        }

        val datePickerEnd = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateEndDate(myCalendar)
        }

        binding.etStartDate.setOnClickListener {
            DatePickerDialog(this, datePickerStart, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.etEndDate.setOnClickListener {
            DatePickerDialog(this, datePickerEnd, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        spinner = findViewById(R.id.spinner2)
        ArrayAdapter.createFromResource(
            this,
            R.array.leave_type,
            R.layout.color_spinner_layout
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            spinner.onItemSelectedListener = this

        }

        binding.btnKirim.setOnClickListener {
//            Toast.makeText(this, "${sessionManager.getString(Constant.PREFS_SPINNER)}", Toast.LENGTH_SHORT).show()
//            Toast.makeText(this, "endDate : ${sessionManager.getString(Constant.PREFS_END_DATE)}", Toast.LENGTH_SHORT).show()
                val end_date = sessionManager.getString(Constant.PREFS_END_DATE)
                val start_date = sessionManager.getString(Constant.PREFS_START_DATE)
                val reason = binding.etReason.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                try {
                   leaveRequest(
                       end_date=end_date!!,
                       leave_type=1,
                       reason=reason,
                       start_date=start_date!!)
                    moveToHome()
                } catch (e: Exception){
                    throw Exception(e.message)
                }
            }
        }

        binding.btnRiwayatCuti.setOnClickListener {
            val intent = Intent(this, RiwayatPengajuanCutiActivity::class.java)
            startActivity(intent)
        }

        binding.btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            sessionManager.clear()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun moveToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun updateEndDate(myCalendar: Calendar) {
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        binding.etEndDate.setText(sdf.format(myCalendar.time))
        sessionManager.put(Constant.PREFS_END_DATE, sdf.format(myCalendar.time))
    }

    private fun updateStartDate(myCalendar: Calendar) {
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        binding.etStartDate.setText(sdf.format(myCalendar.time))
        sessionManager.put(Constant.PREFS_START_DATE, sdf.format(myCalendar.time))
    }

    private suspend fun getUser(): GetCurrentEmployeeResponse? {
        val response = api.getEmployee("Bearer ${sessionManager.getString(Constant.PREFS_USER_TOKEN)}")

        if (response.isSuccessful) {
            val user = response.body()
            withContext(Dispatchers.Main) {
                binding.etName.setText(user!!.data.name)
                binding.etNip.setText(user.data.nip)
            }
            return user
        } else {
            withContext(Dispatchers.Main) {

            }
            return null
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        dataSpinner = parent!!.getItemAtPosition(position)
        sessionManager.put(Constant.PREFS_SPINNER, dataSpinner.toString())
//        Log.e("Spinner", "onCreate: ${sessionManager.getString(Constant.PREFS_SPINNER)}", )

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private suspend fun leaveRequest(end_date: String,
                                     leave_type: Int,
                                     reason: String,
                                     start_date: String): LeaveResponse? {
        val response = api.leaveRequest("Bearer ${sessionManager.getString(Constant.PREFS_USER_TOKEN)}",
            LeaveRequest(
                end_date=end_date,
                leave_type=leave_type,
                reason=reason,
                start_date=start_date,
        ))

        if (response.isSuccessful) {
            val user = response.body()
            withContext(Dispatchers.Main) {
                Toast.makeText(this@FormPengajuanCutiActivity, "Permohonan Cuti Berhasil di Proses : ${user!!.message}", Toast.LENGTH_SHORT).show()
            }
            return user
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@FormPengajuanCutiActivity, "Permohonan Cuti Gagal di Proses", Toast.LENGTH_SHORT).show()
            }
            return null
        }
    }
}