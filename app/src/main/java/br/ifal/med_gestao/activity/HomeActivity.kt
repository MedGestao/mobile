package br.ifal.med_gestao.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import br.ifal.med_gestao.R
import br.ifal.med_gestao.databinding.DrawerLayoutBinding
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout : DrawerLayout

    private val binding by lazy {
        DrawerLayoutBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        supportActionBar?.setCustomView(R.layout.custom_toolbar_title);
        setContentView(binding.root)

        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawerLayout)
        var navigationView = findViewById<NavigationView>(R.id.nav_view)

        navigationView.setNavigationItemSelectedListener(this)

        var toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ListDoctorsFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.nav_home) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ListDoctorsFragment()).commit()
        } else if (item.itemId == R.id.nav_schedules) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SchedulesFragment()).commit()
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}