package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.security.AccessController;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.security.AccessController.getContext;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public EarthquakeAdapter(@NonNull Context context, int resource, @NonNull List<Earthquake> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // get data from this position
        Earthquake earthquake = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // find views for data population
        TextView tv_magnitude = convertView.findViewById(R.id.tv_magnitude);
        TextView tv_position = convertView.findViewById(R.id.tv_position);
        TextView tv_place = convertView.findViewById(R.id.tv_place);
        TextView tv_time = convertView.findViewById(R.id.tv_time);
        TextView tv_date = convertView.findViewById(R.id.tv_date);

        tv_magnitude.setText(String.valueOf(earthquake.getMagnitude()));

        // Check if location contains word "of"
        String location = earthquake.getLocation();
        boolean contains = location.contains("of");

        if (contains) {
            // If does set first part to textview position
            String[] parts = location.split("(?<=of)");
            tv_position.setText(parts[0]);
            // .. set second to textview place
            String trimmed = parts[1].trim();
            tv_place.setText(trimmed);

        } else {
            // If doesn't set following
            tv_position.setText("Near the ");
            tv_place.setText(location);
        }

        tv_date.setText(earthquake.unixTimeToDate());
        tv_time.setText(earthquake.unixTimeToTime());

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) tv_magnitude.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(earthquake.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        return convertView;

    }

    /**
     * Method for getting color for magnitude
     *
     * @param mMagnitude
     * @return int value of color resource id
     */

    public int getMagnitudeColor(double mMagnitude) {

        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(mMagnitude);

        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);

    }


}
