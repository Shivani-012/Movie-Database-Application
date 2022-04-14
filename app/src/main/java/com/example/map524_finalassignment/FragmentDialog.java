package com.example.map524_finalassignment;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDialog extends DialogFragment {

    // interface for detecting fragment button clicks
    public interface DialogClickListener {
        void dialogListenerOnFavourite();
        void dialogListenerOnWatchLater();
        void dialogListenerOnCancel();
    }

    // initialize listener
    public DialogClickListener listener;

    // declare variables for parameters
    public static String Tag = "tag";
    private static final String ARG_PARAM1 = "param1";
    private final String dialogText1;
    private final String dialogText2;
    private String movieName;

    FragmentDialog(){
        dialogText1 = "Which list would you like to add ";
        dialogText2 = " to?";
    }

    // method that creates a new instance of dialog fragment
    public static FragmentDialog newInstance(String movie) {
        FragmentDialog fragment = new FragmentDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    // method that creates dialog fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieName = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    // method called on creation of fragment, sets view and elements
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dialog, container, false);

        // combine text to get text to display
        String text = dialogText1 + movieName + dialogText2;

        // get and set text view
        TextView textView = v.findViewById(R.id.dialog_fragment_text);
        textView.setText(text);

        // declare favourites button and set on click listener
        Button favouriteBtn = v.findViewById(R.id.dialog_fragment_favourites_button);
        favouriteBtn.setOnClickListener(view -> {
            // call listener method
            listener.dialogListenerOnFavourite();
            dismiss();
        });

        // declare watch later button and set on click listener
        Button watchLaterBtn = v.findViewById(R.id.dialog_fragment_watch_later_button);
        watchLaterBtn.setOnClickListener(view -> {
            // call listener method
            listener.dialogListenerOnWatchLater();
            dismiss();
        });

        // declare cancel button and set on click listener
        Button cancelBtn = v.findViewById(R.id.dialog_fragment_cancel_button);
        cancelBtn.setOnClickListener(view -> {
            // call listener method
            listener.dialogListenerOnCancel();
            dismiss();
        });

        return v;
    }
}