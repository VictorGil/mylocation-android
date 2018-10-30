package net.devaction.mylocation.listeners;

import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import net.devaction.mylocation.R;
import net.devaction.mylocation.db.LocationServiceController;
import net.devaction.mylocation.db.OnOrOff;
import static net.devaction.mylocation.db.OnOrOff.ON;
import net.devaction.mylocation.MainActivity;
import net.devaction.mylocation.processors.LocationDataProcessor;

/**
 * @author Víctor Gil
 *
 * since September 2018
 */
public class FloatingActionButtonOnClickListener implements OnClickListener{
    private final String messageToDisplay;
    private final MainActivity mainActivity;

    public FloatingActionButtonOnClickListener(String messageToDisplay, MainActivity mainActivity){
        this.messageToDisplay = messageToDisplay;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onClick(View view) {
        Snackbar.make(view, messageToDisplay, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        int toastMessageResourceId = -1;
        OnOrOff onOrOff = LocationServiceController.switchOnOrOff(view.getContext());
        if (onOrOff == ON){
            toastMessageResourceId = R.string.service_has_been_enabled;
            Log.i(this.getClass().getSimpleName(),"Location service has been enabled");
            mainActivity.changeFloatingButtonIconToStop();
            Log.i(this.getClass().getSimpleName(),"Going to send the current location data");
            LocationDataProcessor.process(mainActivity.getApplicationContext());
        }
        else {
            toastMessageResourceId = R.string.service_has_been_disabled;
            mainActivity.changeFloatingButtonIconToPlay();
        }
        Toast toast = Toast.makeText(view.getContext(), toastMessageResourceId, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, -150);
        toast.show();
    }
}

