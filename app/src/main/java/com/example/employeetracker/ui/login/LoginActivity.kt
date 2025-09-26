package com.example.employeetracker.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.employeetracker.databinding.ActivityLoginBinding
import com.example.employeetracker.ui.admin.AdminActivity
import com.example.employeetracker.ui.tracker.TrackerActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(applicationContext)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupObservers()
        setupClickListeners()
        
        // Initialize demo data
        lifecycleScope.launch {
            viewModel.initializeDemoData()
        }
    }
    
    private fun setupObservers() {
        viewModel.loginResult.observe(this) { result ->
            binding.progressBar.visibility = View.GONE
            
            result.fold(
                onSuccess = { employee ->
                    val intent = if (employee.isAdmin) {
                        Intent(this, AdminActivity::class.java)
                    } else {
                        Intent(this, TrackerActivity::class.java).apply {
                            putExtra("EMPLOYEE_ID", employee.id)
                            putExtra("EMPLOYEE_NAME", employee.name)
                        }
                    }
                    startActivity(intent)
                    finish()
                },
                onFailure = { error ->
                    Toast.makeText(this, error.message ?: "Login failed", Toast.LENGTH_SHORT).show()
                }
            )
        }
        
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnLogin.isEnabled = !isLoading
            binding.btnAdminLogin.isEnabled = !isLoading
        }
    }
    
    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()
            
            if (validateInput(email, password)) {
                viewModel.login(email, password)
            }
        }
        
        binding.btnAdminLogin.setOnClickListener {
            // Pre-fill admin credentials for demo
            binding.etEmail.setText("admin@company.com")
            binding.etPassword.setText("admin123")
        }
    }
    
    private fun validateInput(email: String, password: String): Boolean {
        var isValid = true
        
        if (email.isEmpty()) {
            binding.tilEmail.error = "Email is required"
            isValid = false
        } else {
            binding.tilEmail.error = null
        }
        
        if (password.isEmpty()) {
            binding.tilPassword.error = "Password is required"
            isValid = false
        } else {
            binding.tilPassword.error = null
        }
        
        return isValid
    }
}