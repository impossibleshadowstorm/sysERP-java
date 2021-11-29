package com.dsvv.syserp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseAttendanceInView extends AppCompatActivity {

    String[] courses = {"BCA", "BSc_It", "MCA"};
    String[] semester = {"I", "II", "III", "IV", "V", "VI"};
    AutoCompleteTextView autoCompleteTxt, autoCompleteTxtSem;
    ArrayAdapter<String> adapterItems, adapterItemSem;
    RelativeLayout proceed_btn;
    String item, semitem, selecteddate;

    DatePickerDialog datePickerDialog;
    Button dateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_attendance_in_view);


        initDatePicker();
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());
        selecteddate = getTodaysDate();

        autoCompleteTxt = findViewById(R.id.auto_complete_txt);
        autoCompleteTxtSem = findViewById(R.id.auto_complete_txt_sem);
        adapterItems = new ArrayAdapter<String>(this, R.layout.course_list, courses);
        adapterItemSem = new ArrayAdapter<String>(this, R.layout.course_list, semester);

        autoCompleteTxt.setAdapter(adapterItems);
        autoCompleteTxtSem.setAdapter(adapterItemSem);

        Button back = (Button) findViewById(R.id.button_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
            }
        });

        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString();
            }
        });
        autoCompleteTxtSem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                semitem = parent.getItemAtPosition(position).toString();
            }
        });


        proceed_btn = findViewById(R.id.proceed_btn_view);
        proceed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item == null && semitem == null) {
                    Toast.makeText(ChooseAttendanceInView.this, "Please Select Course & Semester!!", Toast.LENGTH_SHORT).show();

                    FirebaseFirestore rootRefs = FirebaseFirestore.getInstance();

                    Map<String, Object> docData = new HashMap<>();
                    docData.put("name", "World");
                    docData.put("rno", 190);

//                    String id = rootRefs.collection("Students").document("BCA").collection("I").getId();
//                    rootRefs.collection("Students").document("BCA").collection("I").document(id).set(docData);
                    rootRefs.collection("Students").document("BCA").collection("I").add(docData);
//                    rootRefs.collection("Students").document(item).collection(semitem).document("sumjjj").set("name");


//                    collection.document("hjfjh").update(map);

                } else if (item == null) {
                    Toast.makeText(ChooseAttendanceInView.this, "Please Select Course!!", Toast.LENGTH_SHORT).show();
                } else if (semitem == null) {
                    Toast.makeText(ChooseAttendanceInView.this, "Please Select Semester!!", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

                    DocumentReference docIdRef = rootRef.collection("Attendance").document(item).collection(semitem).document(selecteddate);
                    docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Toast.makeText(ChooseAttendanceInView.this, "You Have Already Taken The Attendance!! " + selecteddate, Toast.LENGTH_SHORT).show();
                                    Map<String, Object> users = document.getData();
//                                    Toast.makeText(ChooseAttendanceInView.this, " " + users, Toast.LENGTH_SHORT).show();

//                                    List<String> list = new ArrayList<String>(users.keySet());
                                    List<Object> status = new ArrayList<Object>(users.values());
                                    ArrayList<String> user_status = new ArrayList<String>();

                                    for (int i = 0; i < status.size(); i++) {
                                        user_status.add(i, status.get(i).toString());
                                    }


                                    Intent intent = new Intent(getBaseContext(), ViewAttendance.class);
                                    intent.putExtra("list", user_status);
                                    intent.putExtra("course", item);
                                    intent.putExtra("semester", semitem);
                                    intent.putExtra("date", selecteddate);
                                    startActivity(intent);


                                } else {
                                    Toast.makeText(ChooseAttendanceInView.this, "You Didn't Had Any Class On " + selecteddate + " .", Toast.LENGTH_LONG).show();
//                                    Map<String, Object> docData = new HashMap<>();
//
//                                    rootRef.collection("Attendance").document(item).collection(semitem).document(date).set(docData);
//
//                                    Intent intent = new Intent(getBaseContext(), Attendance.class);
//                                    intent.putExtra("course", item);
//                                    intent.putExtra("semester", semitem);
//                                    intent.putExtra("date", date);
//                                    startActivity(intent);
                                }
                            } else {
                                Toast.makeText(ChooseAttendanceInView.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
                selecteddate = date;
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String makeDateString(int day, int month, int year) {
        return day + "-" + getMonthFormat(month) + "-" + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1)
            return "Jan";
        if (month == 2)
            return "Feb";
        if (month == 3)
            return "Mar";
        if (month == 4)
            return "Apr";
        if (month == 5)
            return "May";
        if (month == 6)
            return "Jun";
        if (month == 7)
            return "Jul";
        if (month == 8)
            return "Aug";
        if (month == 9)
            return "Sep";
        if (month == 10)
            return "Oct";
        if (month == 11)
            return "Nov";
        if (month == 12)
            return "Dec";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }
}