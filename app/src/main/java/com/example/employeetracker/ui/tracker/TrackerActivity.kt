package com.example.employeetracker.ui.tracker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.employeetracker.R
import com.example.employeetracker.databinding.ActivityTrackerBinding
import com.example.employeetracker.services.LocationTrackingService
import com.example.employeetracker.ui.login.LoginActivity

class TrackerActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityTrackerBinding
    private val viewModel: TrackerViewModel by viewModels {
        TrackerViewModelFactory(applicationContext)
    }
    
    private var employeeId: String? = null
    private var employeeName: String? = null
    
    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
                Toast.makeText(this, getString(R.string.permission_granted), Toast.LENGTH_SHORT).show()
                checkBackgroundPermission()
            }
            else -> {
                Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private val backgroundLocationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(this, "Background location permission granted", Toast.LENGTH_SHORT).show()
        } else {
            showBackgroundLocationDialog()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setSupportActionBar(binding.toolbar)
        
        employeeId = intent.getStringExtra("EMPLOYEE_ID")
        employeeName = intent.getStringExtra("EMPLOYEE_NAME")
        
        if (employeeId == null) {
            finish()
            return
        }
        
        binding.tvWelcome.text = "Welcome, $employeeName!"
        
        setupObservers()
        setupClickListeners()
        showPrivacyNotice()
        
        viewModel.setEmployeeId(employeeId!!)
        viewModel.checkTrackingStatus(employeeId!!)
    }
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.tracker_menu, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    private fun setupObservers() {
        viewModel.trackingStatus.observe(this) { isActive ->
            updateUI(isActive)
        }
        
        viewModel.currentLocation.observe(this) { location ->
            binding.tvCurrentLocation.text = location ?: "No location available"
        }
        
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
    
    private fun setupClickListeners() {
        binding.btnCheckIn.setOnClickListener {
            if (checkLocationPermissions()) {
                viewModel.startTracking(employeeId!!)
                startLocationService()
            } else {
                requestLocationPermissions()
            }
        }
        
        binding.btnCheckOut.setOnClickListener {
            viewModel.stopTracking(employeeId!!)
            stopLocationService()
        }
    }
    
    private fun updateUI(isTracking: Boolean) {
        if (isTracking) {
            binding.tvTrackingStatus.text = getString(R.string.status_active)
            binding.tvTrackingStatus.setTextColor(ContextCompat.getColor(this, R.color.success))
            binding.btnCheckIn.isEnabled = false
            binding.btnCheckOut.isEnabled = true
        } else {
            binding.tvTrackingStatus.text = getString(R.string.status_inactive)
            binding.tvTrackingStatus.setTextColor(ContextCompat.getColor(this, R.color.error))
            binding.btnCheckIn.isEnabled = true
            binding.btnCheckOut.isEnabled = false
        }
    }
    
    private fun checkLocationPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    private fun requestLocationPermissions() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.permission_location_title))
            .setMessage(getString(R.string.permission_location_message))
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                locationPermissionLauncher.launch(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ))
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }
    
    private fun checkBackgroundPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                backgroundLocationPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }
        }
    }
    
    private fun showBackgroundLocationDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.permission_background_title))
            .setMessage(getString(R.string.permission_background_message))
            .setPositiveButton(getString(R.string.ok), null)
            .show()
    }
    
    private fun showPrivacyNotice() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.privacy_notice_title))
            .setMessage(getString(R.string.privacy_notice_content))
            .setPositiveButton(getString(R.string.privacy_acknowledge), null)
            .show()
    }
    
    private fun startLocationService() {
        val serviceIntent = Intent(this, LocationTrackingService::class.java).apply {
            action = LocationTrackingService.ACTION_START_TRACKING
            putExtra(LocationTrackingService.EXTRA_EMPLOYEE_ID, employeeId)
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }
    
    private fun stopLocationService() {
        val serviceIntent = Intent(this, LocationTrackingService::class.java).apply {
            action = LocationTrackingService.ACTION_STOP_TRACKING
        }
        startService(serviceIntent)
    }
    
    private fun logout() {
        // Stop tracking if active
        viewModel.stopTracking(employeeId!!)
        stopLocationService()
        
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}