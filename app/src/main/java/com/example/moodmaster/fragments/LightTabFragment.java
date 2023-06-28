//package com.example.moodmaster.fragments;
//
//import android.content.Context;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.fragment.app.Fragment;
//
//import com.example.moodmaster.R;
//
//import org.w3c.dom.Text;
//
//public class LightTabFragment extends Fragment {
//
//    private SensorManager sensorManager;
//    private Sensor lightSensor;
//    private TextView displayLux;
//
//    private float lux;
//
//    public LightTabFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
//        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
//        lux = 42;
//
//        SensorEventListener lightEventListener = new SensorEventListener() {
//            @Override
//            public void onSensorChanged(SensorEvent sensorEvent) {
//                lux = sensorEvent.values[0];
//
////                System.out.println("-------------LUX TEST " + lux);
//
//                if (lux < 10) {
//                    System.out.println("-------------LUX TEST DUNKEL " + lux);
//                    displayLux.setText("DUNKEL");
//                } else {
//                    System.out.println("-------------LUX TEST HELL " + lux);
//                    displayLux.setText("HELL");
//                }
//                //System.out.println("LUX: " + lux);
//            }
//
//            @Override
//            public void onAccuracyChanged(Sensor sensor, int i) {
//
//            }
//        };
//
//        sensorManager.registerListener(lightEventListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
//
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//
//        View rootView = inflater.inflate(R.layout.activity_light, container, false);
//        displayLux = rootView.findViewById(R.id.textView);
//
//
//
//
//        return inflater.inflate(R.layout.activity_light, container, false);
//    }
//
//    public float getLux() {
//        System.out.println("-----------------------BUBU " + lux);
//        return lux;
//    }
//}



package com.example.moodmaster.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.moodmaster.EmergencyCall;
import com.example.moodmaster.R;
import com.example.moodmaster.feelingScale_MoodShow.BreathingActivity;
import com.example.moodmaster.feelingScale_MoodShow.FeelingScale;
import com.example.moodmaster.feelingScale_MoodShow.MapsActivity;
import com.example.moodmaster.feelingScale_MoodShow.moods_tabbed;

public class LightTabFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor;

    private TextView displayLight;

    private float lux; // Light

    private SharedPreferences algoValues;

    private final String KEY = "luxValue";




    public LightTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        algoValues = getContext().getSharedPreferences("algo", Context.MODE_PRIVATE);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_light, container, false);
        displayLight = rootView.findViewById(R.id.lightValueTextView);

        ImageButton emergencyCall = rootView.findViewById(R.id.emergencyButton);

        emergencyCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmergencyCall.showEmergencyCallConfirmationDialog(getActivity());
            }
        });

        Button breathingButton = rootView.findViewById(R.id.startBreathingButton);

        breathingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BreathingActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        lux = event.values[0];
        saveLux(lux);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(lux < 50){
                    displayLight.setText(R.string.dark);
                }
                else if(lux >= 50 && lux < 100){
                    displayLight.setText(R.string.dim);
                }
                else if(lux >= 100 && lux < 10000){
                    displayLight.setText(R.string.bright);
                }
                else{
                    displayLight.setText(R.string.sunlight);
                }

            }
        });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // We don't need this in the current context.
    }

    private void saveLux(float luxValue){
        SharedPreferences.Editor editor = algoValues.edit();
        editor.putFloat(KEY, luxValue);
        editor.apply();
    }
}
