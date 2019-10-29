package com.uninove.controledehoras;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity  {

    private AppBarConfiguration mAppBarConfiguration;

    private Button btnAction;
    private TextView startHour;
    private TextView startInterval;
    private TextView endInterval;
    private TextView endHour;
    private TextView totalHour;

    private List list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        this.btnAction = findViewById(R.id.btn_action);

        list = new ArrayList<LocalDateTime>();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public String formatHour(Long ms){
        int segundos = (int) ( ms / 1000 ) % 60;
        int minutos  = (int) ( ms / 60000 ) % 60;
        int horas    = (int) (ms / 3600000);
        return String.format( "%02d:%02d:%02d", horas, minutos,segundos );
    }

    public Long differenceHours(LocalDateTime t1, LocalDateTime t2) {
        return t1.until(t2, ChronoUnit.MILLIS);
    }



    public void getHour(View v) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime localDate = LocalDateTime.now();

        this.list.add(localDate);

        startHour = findViewById(R.id.hour_start);
        startInterval = findViewById(R.id.start_interval);
        endInterval = findViewById(R.id.end_interval);
        endHour = findViewById(R.id.hour_end);
        totalHour = findViewById(R.id.total_hour);



        switch (list.size()){
            case 1:
                startHour.setText(( (LocalDateTime) list.get(0) ).format(formatter));
                break;
            case 2:
                startInterval.setText( ((LocalDateTime) list.get(1)).format((formatter)) );

                LocalDateTime t1 = (LocalDateTime) list.get(0);
                LocalDateTime t2 = (LocalDateTime) list.get(1);

                totalHour.setText(this.formatHour(this.differenceHours(t1, t2)));

                break;
            case 3:
                endInterval.setText(( (LocalDateTime) list.get(0) ).format(formatter));
                break;
            case 4:
                endHour.setText( ((LocalDateTime) list.get(1)).format((formatter)) );

                Long int1 = this.differenceHours((LocalDateTime) list.get(0), (LocalDateTime) list.get(1));
                Long int2 = this.differenceHours((LocalDateTime) list.get(2), (LocalDateTime) list.get(3));

                totalHour.setText( this.formatHour( int1 + int2 ) );

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + list.size());
        }

    }

}
