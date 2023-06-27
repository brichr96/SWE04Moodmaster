package com.example.moodmaster;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AlertDialog;

public class EmergencyCall {

//    private static String phoneNumber = "06502729621";

    private static String phoneNumber = "0676840004555";

    public static void showEmergencyCallConfirmationDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to call the emergency contact?");
        builder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Call the emergency contact
                makeCall(activity);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private static void makeCall(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        activity.startActivity(intent);
    }
}

