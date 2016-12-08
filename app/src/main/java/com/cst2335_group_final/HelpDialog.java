package com.cst2335_group_final;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * Created by victo on 2016-12-07.
 */

public class HelpDialog extends Dialog {
    public HelpDialog(Context context) {
        super(context);
        this.context = context;
    }


    public static Dialog onCreateDialog(int version, int title, int message, Context context) {
        final String degree = Character.toString((char) 0x00B0);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Get the layout inflater
        // Pass null as the parent view because its going in the dialog layout
        String messageStr = String.format(context.getResources().getString(version),
                context.getResources().getString(message));
        builder.setMessage(messageStr).setTitle(context.getResources().getString(title));
                //.setView(view)
                // Add action buttons
                /*.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });*/
        builder.show();
        return builder.create();
    }

    private static Context context;
}
