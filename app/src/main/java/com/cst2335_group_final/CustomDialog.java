/* **************************************************************
 * Algonquin College - School of Advanced Technology
 * CST 2335 - Graphical Interface Programming
 * Final Group Project
 *
 * Author: EVERETT HOLDEN
 * Student #: 040812130
 * Network login name: hold0052
 * Lab instructor: ABDUL
 * Section: 014
 * Due date: 2016.12.09
 *
 *  -- Class definition
 * Purpose --
 * **************************************************************/
package com.cst2335_group_final;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
/**
 * Custom dialog for the Automobile Activity
 * @author    EVERETT HOLDEN
 * @version   1.0.0 2016.11.21
 */
public class CustomDialog extends DialogFragment {

    /**
     * Interface for specifing the method for handling positive
     * clicks on the dialog that are handled in the activity presenting
     * the dialog
     *************************************************************/
    public interface NoticeDialogListener{
        public void onDialogPositiveClick(DialogFragment dialog);
    }

    NoticeDialogListener mListener;//instance of the interface

    /**
     * The dialog is attached to the Activity it is displayed in
     * @param activity the instance of Activity the dialog to which the dialog is attached
     *************************************************************/
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            mListener = (NoticeDialogListener) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + "must implement NoticeDialogListener");
        }
    }

    /**
     * OnCreateDialog builds an AlertDialog and sets the custom layout
     * to specify its view
     * @param savedInstanceState bundle data stored from previous instance
     *************************************************************/
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog, null));//inflates the custom layout
        builder.setPositiveButton(R.string.toolbar_dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
               mListener.onDialogPositiveClick(CustomDialog.this);
            }
        });

        builder.setNegativeButton(R.string.toolbar_dialog_cancel,new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
            }
        });
        return builder.create();
    }
}