package com.example.moodmaster;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import androidx.appcompat.app.AlertDialog;

public class EmergencyCall {

    private static String phoneNumber = "06766280010";
    private static SharedPreferences emergencyNumber;
    private static final String KEY = "number";


    /**
     * Displays a confirmation dialog for making an emergency call.
     *
     * @param activity The activity where the dialog is displayed.
     * @param context  The context used to access resources and preferences.
     */
    public static void showEmergencyCallConfirmationDialog(Activity activity, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.confirmation);
        builder.setMessage(R.string.emergency_dialog);
        builder.setPositiveButton(R.string.call, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                makeCall(activity, context);
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.show();
    }

    /**
     * Initiates a phone call to the emergency number.
     *
     * @param activity The activity from which the phone call is initiated.
     * @param context  The context used to access shared preferences.
     */
    private static void makeCall(Activity activity, Context context) {
        emergencyNumber = context.getSharedPreferences("tel", Context.MODE_PRIVATE);
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getNumber()));
        activity.startActivity(intent);
    }

    /**
     * Retrieves the emergency phone number from the shared preferences.
     *
     * @return The emergency phone number.
     */
    private static String getNumber() {
        return emergencyNumber.getString(KEY, phoneNumber);
    }
}

