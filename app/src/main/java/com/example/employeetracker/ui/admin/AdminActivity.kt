package com.example.employeetracker.ui.admin

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.employeetracker.R
import com.example.employeetracker.databinding.ActivityAdminBinding
import com.example.employeetracker.ui.login.LoginActivity

class AdminActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAdminBinding
    private val viewModel: AdminViewModel by viewModels {
        AdminViewModelFactory(applicationContext)
    }
    private lateinit var adapter: EmployeeLocationAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setSupportActionBar(binding.toolbar)
        
        setupRecyclerView()
        setupObservers()
        setupSwipeRefresh()
        
        viewModel.loadEmployeeLocations()
    }
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.admin_menu, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                viewModel.loadEmployeeLocations()
                true
            }
            R.id.action_logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    private fun setupRecyclerView() {
        adapter = EmployeeLocationAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@AdminActivity)
            adapter = this@AdminActivity.adapter
        }
    }
    
    private fun setupObservers() {
        viewModel.employeeLocations.observe(this) { employees ->
            if (employees.isNotEmpty()) {
                binding.recyclerView.visibility = View.VISIBLE
                binding.tvNoData.visibility = View.GONE
                adapter.submitList(employees)
            } else {
                binding.recyclerView.visibility = View.GONE
                binding.tvNoData.visibility = View.VISIBLE
            }
        }
        
        viewModel.isLoading.observe(this) { isLoading ->
            binding.swipeRefresh.isRefreshing = isLoading
        }
    }
    
    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadEmployeeLocations()
        }
    }
    
    private fun logout() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}