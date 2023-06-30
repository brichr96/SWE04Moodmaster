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

    private static void makeCall(Activity activity, Context context) {
        emergencyNumber = context.getSharedPreferences("tel", Context.MODE_PRIVATE);
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getNumber()));
        activity.startActivity(intent);
    }

    private static String getNumber() {
        return emergencyNumber.getString(KEY, phoneNumber);
    }
}

