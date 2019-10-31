package com.uninove.controledehoras;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
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
import android.widget.ImageView;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements ListHour.OnFragmentInteractionListener, About.OnFragmentInteractionListener, Contact.OnFragmentInteractionListener {

    private AppBarConfiguration mAppBarConfiguration;

    private Button btnAction;
    private TextView startHour;
    private TextView startInterval;
    private TextView endInterval;
    private TextView endHour;
    private TextView totalHour;
    private ImageView imgImagem;

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
                R.id.nav_tools, R.id.nav_share, R.id.nav_send, R.id.listHour, R.id.about, R.id.contact)
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

    public String formatHour(Long ms) {
        int segundos = (int) Math.round((ms / 1000) % 60);
        int minutos = (int) Math.round((ms / 60000) % 60);
        int horas = (int) Math.round((ms / 3600000));
        return String.format("%02d:%02d:%02d", horas, minutos, segundos);
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


        switch (list.size()) {
            case 1:
                startHour.setText(((LocalDateTime) list.get(0)).format(formatter));
                this.imgImagem = findViewById(R.id.imgImagem);
                this.imgImagem.setImageResource(R.drawable.inicio);
                break;
            case 2:
                startInterval.setText(((LocalDateTime) list.get(1)).format((formatter)));

                LocalDateTime t1 = (LocalDateTime) list.get(0);
                LocalDateTime t2 = (LocalDateTime) list.get(1);

                totalHour.setText(this.formatHour(this.differenceHours(t1, t2)));
                this.imgImagem = findViewById(R.id.imgImagem);
                this.imgImagem.setImageResource(R.drawable.almoco);
                break;
            case 3:
                endInterval.setText(((LocalDateTime) list.get(2)).format(formatter));
                this.imgImagem = findViewById(R.id.imgImagem);
                this.imgImagem.setImageResource(R.drawable.volta);
                break;
            case 4:
                endHour.setText(((LocalDateTime) list.get(3)).format((formatter)));

                Long int1 = this.differenceHours((LocalDateTime) list.get(0), (LocalDateTime) list.get(1));
                Long int2 = this.differenceHours((LocalDateTime) list.get(2), (LocalDateTime) list.get(3));

                totalHour.setText(this.formatHour(int1 + int2));
                this.imgImagem = findViewById(R.id.imgImagem);
                this.imgImagem.setImageResource(R.drawable.fim);
                break;
            default:
                startHour.setText("00:00:00");
                startInterval.setText("00:00:00");
                endInterval.setText("00:00:00");
                endHour.setText("00:00:00");
                totalHour.setText("00:00:00");
                list.removeAll(list);
                this.imgImagem = findViewById(R.id.imgImagem);
                this.imgImagem.setImageResource(R.drawable.clock);
                break;

        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    //contatos
    public void enviarEmail(View view) {
        Intent mail = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "mail@gmail.com", null));
        mail.putExtra(Intent.EXTRA_SUBJECT, "O aplicativo do ponto eletrônico apresenta erros");
        mail.putExtra(Intent.EXTRA_TEXT, "Insira abaixo seu problema com o app:");
        startActivity(Intent.createChooser(mail, "send email..."));
    }

    public void efetuarLigacao(View view) {
        Intent liga = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "11955555555"));
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        startActivity(liga);
    }
}
