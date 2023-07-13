package com.example.crimealert

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.crimealert.databinding.ActivityMainBinding
import com.example.crimealert.models.User
import com.example.crimealert.models.UserResponse
import com.example.crimealert.utils.UtilManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    lateinit var toggle: ActionBarDrawerToggle
    @Inject
    lateinit var utilManager: UtilManager
    private var user: UserResponse? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.navhostfragment) as NavHostFragment?
        if (navHost != null) {
            navController = navHost.navController
        }
        if (utilManager.getToken() != null) {
            val jsonUser = utilManager.getUser()
            if (jsonUser != null) {
                user = Gson().fromJson(jsonUser, UserResponse::class.java)
            }
            if(user!!.user.RoleId == 1){
                binding.navView.inflateMenu(R.menu.admin_app_menu)
            }
            else {
                binding.navView.inflateMenu(R.menu.civillian_app_menu)
            }
        }
        else{
            binding.navView.inflateMenu(R.menu.my_app_menu)
        }
        setupWithNavController(binding.navView, navController)
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.open,
            R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }
    fun logout(item: MenuItem) {
        utilManager.saveToken(null)
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}