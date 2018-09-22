package net.devaction.mylocation;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import net.devaction.mylocation.config.ConfigFetcher;
import net.devaction.mylocation.db.LocationServiceController;
import net.devaction.mylocation.listeners.FloatingActionButtonOnClickListener;
import net.devaction.mylocation.processors.LocationDataProcessor;

import java.io.InputStream;

/**
 * @author Víctor Gil
 *
 * since June 2018
 */
public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        ConfigFetcher.fetch(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (LocationServiceController.isServiceEnabled(getApplicationContext())){
            changeFloatingButtonIconToStop();
            LocationDataProcessor.process(getApplicationContext());
        } else
            changeFloatingButtonIconToPlay();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void changeFloatingButtonIconToPlay(){
        //we also display a text view saying that the Location service is disabled
        TextView mainTextView = findViewById(R.id.mainTextView);
        mainTextView.setText(R.string.service_is_disabled);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageResource(R.mipmap.ic_launcher_icon_play);
        String enablingService = getResources().getString(R.string.enabling_service);
        fab.setOnClickListener(new FloatingActionButtonOnClickListener(enablingService, this));
    }

    public void changeFloatingButtonIconToStop(){
        TextView mainTextView = findViewById(R.id.mainTextView);
        mainTextView.setText(R.string.service_is_enabled);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageResource(R.mipmap.ic_icon_media_stop);
        String disablingService = getResources().getString(R.string.disabling_service);
        fab.setOnClickListener(new FloatingActionButtonOnClickListener(disablingService, this));
    }
}

