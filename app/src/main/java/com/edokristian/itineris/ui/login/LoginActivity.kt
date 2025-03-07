package com.edokristian.itineris.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.edokristian.itineris.MainActivity
import com.edokristian.itineris.R
import com.edokristian.itineris.databinding.ActivityLoginBinding
import com.edokristian.itineris.model.request.LoginRequest
import com.edokristian.itineris.model.response.GetCurrentEmployeeResponse
import com.edokristian.itineris.model.response.GetLeaveHistoriesResponse
import com.edokristian.itineris.model.response.LoginResponse
import com.edokristian.itineris.services.ApiClient
import com.edokristian.itineris.services.ApiService
import com.edokristian.itineris.ui.dashboard.DashboardApproverActivity
import com.edokristian.itineris.utils.Constant
import com.edokristian.itineris.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager
    private val api: ApiService = ApiClient.getRetroClientInstance().create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        sessionManager = SessionManager(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnLogin.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val nip = binding.etNip.text.toString()
                    val pass = binding.etPassword.text.toString()
                    loginUser(nip, pass)
                    if (getUser()!!.data.role == "Staff"){
                        moveToIntentHome()
                    } else {
                        moveToIntentDashboardApprover()
                    }
//                    getUser()
//                    getLeaveHistories()
//                    moveToIntent()
                } catch (e: Exception){
                    throw Exception(e.message)
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        if (sessionManager.getBoolean(Constant.PREFS_IS_LOGIN)){
            if (sessionManager.getBoolean(Constant.PREFS_ROLE_STAFF)){
            moveToIntentHome()
            } else {
                moveToIntentDashboardApprover()
            }
        }
    }

//    private suspend fun getLeaveHistories(): GetLeaveHistoriesResponse? {
//        val response = api.getLeaveHistories("Bearer ${sessionManager.getString(Constant.PREFS_USER_TOKEN)}")
//
//        if (response.isSuccessful) {
//            val histori = response.body()
//            withContext(Dispatchers.Main) {
//                histori!!.data.forEach { dataX ->
//                    Log.e("getLeaveHistories", "getLeaveHistories: ${dataX.id}", )
//                }
//            }
//            return histori
//        } else {
//            withContext(Dispatchers.Main) {
//
//            }
//            return null
//        }
//    }

    private fun moveToIntentHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun moveToIntentDashboardApprover() {
        val intent = Intent(this, DashboardApproverActivity::class.java)
        startActivity(intent)
    }

    private suspend fun getUser(): GetCurrentEmployeeResponse? {
        val response = api.getEmployee("Bearer ${sessionManager.getString(Constant.PREFS_USER_TOKEN)}")

        if (response.isSuccessful) {
            val user = response.body()
            withContext(Dispatchers.Main) {
                sessionManager.put(Constant.PREFS_USERNAME, user!!.data.name)
                Log.e("ZZZ", "onCreate: ${user!!.data.name}")
            }
            return user
        } else {
            withContext(Dispatchers.Main) {

            }
            return null
        }
    }
    private suspend fun loginUser(nip: String, pass: String): LoginResponse? {
                val response = api.login(LoginRequest(nip, pass))

                if (response.isSuccessful) {
                    val user = response.body()
                    withContext(Dispatchers.Main) {
                        if (nip.isNotEmpty() && pass.isNotEmpty()){
                            val token = user!!.data.token
                            sessionManager.put(Constant.PREFS_USER_TOKEN, token)
                            sessionManager.put(Constant.PREFS_IS_LOGIN, true)
                            if (getUser()!!.data.role == "Staff"){
                                sessionManager.put(Constant.PREFS_ROLE_STAFF, true)
                                sessionManager.put(Constant.PREFS_USER_ID, getUser()!!.data.id.toString())
                            } else {
                                sessionManager.put(Constant.PREFS_USER_ID, getUser()!!.data.id.toString())
                            }
                            Log.e("token", "token: ${sessionManager.getString(Constant.PREFS_USER_TOKEN)}")
                        } else {
                            Log.e("111", "onCreate: ${nip.isEmpty()}")
                        }
                        Toast.makeText(this@LoginActivity, "Berhasil Login", Toast.LENGTH_SHORT)
                            .show()
                    }
                    return user
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@LoginActivity, "Gagal Login", Toast.LENGTH_SHORT).show()
                    }
                    return null
                }
            }

//    private suspend fun getLeaveHistories(): GetLeaveHistoriesResponse? {
//        val response = api.getLeaveHistories("Bearer ${sessionManager.getString(Constant.PREFS_USER_TOKEN)}")
//
//        if (response.isSuccessful) {
//            val histori = response.body()
//            withContext(Dispatchers.Main) {
//                histori!!.data.forEach { dataX ->
//                    Toast.makeText(this@LoginActivity, "Histori sukses", Toast.LENGTH_SHORT).show()
//                }
//            }
//            return histori
//        } else {
//            withContext(Dispatchers.Main) {
//                Toast.makeText(this@LoginActivity, "Histori Gagal", Toast.LENGTH_SHORT).show()
//            }
//            return null
//        }
//    }

}