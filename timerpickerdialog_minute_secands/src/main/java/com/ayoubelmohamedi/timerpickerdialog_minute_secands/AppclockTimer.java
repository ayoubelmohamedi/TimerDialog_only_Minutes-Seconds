package com.ayoubelmohamedi.timerpickerdialog_minute_secands;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

import java.text.DecimalFormat;

public class AppclockTimer extends AppCompatDialogFragment {

    public static final String DIALOG_ID = "id";
    public static final String Dialog_Message ="message";
    public static final String DIALOG_POSITIVE_RID = "positive_rid";
    public static final String Dialog_Negative_RID = "Negative_rid";



    interface DialogEvents {
        void onPositiveDialogResult(int dialogId, Bundle args);
        void onNegativeDialogResult(int dialogId, Bundle args);
        void onDialogCancelled(int dialogId);
    }


    private DialogEvents mDialogEvents;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //activities containing this fragment must implement its callbacks.
        if (!(context instanceof DialogEvents)){
            throw new ClassCastException(context.toString() +" must implement AppDialogEvents interface");
        }

        mDialogEvents = (DialogEvents) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();

        //Reset the active callbacks interface, because we don't have an activity any longer.
        mDialogEvents = null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final Bundle arguments = getArguments();
        final int dialogId;
        String messageString;
        int positiveStringId ;
        int negativeStringId;

        if (arguments != null){
            dialogId = arguments.getInt(DIALOG_ID);
            messageString = arguments.getString(Dialog_Message);

            if (dialogId == 0 || messageString == null){
                throw new IllegalArgumentException("DIALOG_ID and/or DIALOG_MESSAGE not present in the bundle");
            }

            positiveStringId = arguments.getInt(DIALOG_POSITIVE_RID);
            if (positiveStringId == 0){
                positiveStringId = R.string.ok;
            }

            negativeStringId = arguments.getInt(Dialog_Negative_RID);
            if (negativeStringId == 0){
                negativeStringId = R.string.cancel;
            }
        } else {
            throw new IllegalArgumentException("Must pass DIALOG_ID and DIALOG_MESSAGE in the bundle");
        }

        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = layoutInflater.inflate(R.layout.fragment_my_time_picker_dialog,null,false);

        Button upButtonMinute = dialogView.findViewById(R.id.upForMinute);
        Button downButtonMinute = dialogView.findViewById(R.id.downForMinute);
        Button upButtonSecands = dialogView.findViewById(R.id.upForSecands);
        Button downButtonSecands = dialogView.findViewById(R.id.downForSecands);

        upButtonMinute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSubMethod(R.id.upForMinute,dialogView);
            }
        });

        downButtonMinute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSubMethod(R.id.downForMinute,dialogView);
            }
        });

        upButtonSecands.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSubMethod(R.id.upForSecands,dialogView);
            }
        });

        downButtonSecands.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSubMethod(R.id.downForSecands,dialogView);
            }
        });


        builder.setTitle(messageString)
               .setView(dialogView)
               .setPositiveButton(positiveStringId, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mDialogEvents != null){
                            TextView minute = (TextView)dialogView.findViewById(R.id.minute_text);
                            TextView secands = (TextView)dialogView.findViewById(R.id.secands_text);
                            Bundle arg = getArguments();
                            arg.putString("minute",minute.getText().toString());
                            arg.putString("secands",secands.getText().toString());
                            mDialogEvents.onPositiveDialogResult(dialogId,arguments);
                        }
                    }
                })
                .setNegativeButton(negativeStringId, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mDialogEvents!=null){
                            mDialogEvents.onNegativeDialogResult(dialogId,arguments);
                        }
                    }
                });

        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (mDialogEvents != null){
            int dialogId = getArguments().getInt(DIALOG_ID);
            mDialogEvents.onDialogCancelled(dialogId);
        }
    }

    private void addSubMethod(int buttonId, View view){

        TextView secands_field = view.findViewById(R.id.secands_text);
        TextView minute_field = view.findViewById(R.id.minute_text);

        if (buttonId == R.id.upForMinute) {
            int firstMinute = Integer.parseInt(minute_field.getText().toString());
            if (firstMinute < 60) {
                int plus = firstMinute + 1;
                DecimalFormat format = new DecimalFormat("00");
                String formatedValue = format.format(plus);
                minute_field.setText(formatedValue);
            }
        } else if (buttonId == R.id.upForSecands) {
            int firstSecands = Integer.parseInt(secands_field.getText().toString());
            if (firstSecands < 59) {
                int plus2 = firstSecands + 1;
                DecimalFormat format = new DecimalFormat("00");
                String formatedValue = format.format(plus2);
                secands_field.setText(formatedValue);
            }
        } else if (buttonId == R.id.downForMinute) {
            int firstMinute2 = Integer.parseInt(minute_field.getText().toString());
            if (firstMinute2 > 0) {
                int sub = firstMinute2 - 1;
                DecimalFormat format = new DecimalFormat("00");
                String formatedValue = format.format(sub);
                minute_field.setText(formatedValue);
            }
        } else if (buttonId == R.id.downForSecands) {
            int firstSecands2 = Integer.parseInt(secands_field.getText().toString());
            if (firstSecands2 > 0) {
                int sub2 = firstSecands2 - 1;
                DecimalFormat format = new DecimalFormat("00");
                String formatedValue = format.format(sub2);
                secands_field.setText(formatedValue);
            }
        }
    }




}
