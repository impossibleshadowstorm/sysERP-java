package com.dsvv.syserp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class showStudentsAttendanceAdapter extends ArrayAdapter<showStudentsAttendance> {

    private Context ct;
    private ArrayList<showStudentsAttendance> arr;

    public showStudentsAttendanceAdapter(@NonNull Context context, int resource, @NonNull List<showStudentsAttendance> objects) {
        super(context, resource, objects);

        this.ct = context;
        this.arr = new ArrayList<>(objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater i = (LayoutInflater)ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = i.inflate(R.layout.view_students, null);
        }
        if(arr.size() > 0){
            showStudentsAttendance d = arr.get(position);

            ImageView student_image = convertView.findViewById(R.id.student_image);
            TextView view_username_attendance = convertView.findViewById(R.id.view_username_attendance);
            TextView view_username_attendance_status = convertView.findViewById(R.id.view_username_attendance_status);

            student_image.setImageResource(d.image);
            view_username_attendance.setText(d.name);
            view_username_attendance_status.setText(d.st);
        }

        return convertView;
    }
}
