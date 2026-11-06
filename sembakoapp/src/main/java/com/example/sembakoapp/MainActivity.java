package com.example.sembakoapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.example.sembakoapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    NavHostFragment navHost =
        (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host);
    if (navHost != null) {
      NavController navController = navHost.getNavController();
      NavigationUI.setupWithNavController(binding.bottomNav, navController);
    }

    // targetSdk 37 forces edge-to-edge. BottomNavigationView applies the
    // bottom system-bar inset itself, so padding the root bottom would double it.
    ViewCompat.setOnApplyWindowInsetsListener(
        binding.getRoot(),
        (view, insets) -> {
          Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
          view.setPadding(bars.left, bars.top, bars.right, 0);
          return insets;
        });
  }
}
