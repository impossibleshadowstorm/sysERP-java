package com.dsvv.syserp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ChooseAttendance extends AppCompatActivity {

    String[] courses = {"BCA", "BSc_It", "MCA"};
    String[] semester = {"I", "II", "III", "IV", "V", "VI"};
    AutoCompleteTextView autoCompleteTxt, autoCompleteTxtSem;
    ArrayAdapter<String> adapterItems, adapterItemSem;
    RelativeLayout attendance_list_btn;
    TextView date;
    public String item, semitem, my_date_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_attendance);

        date = (TextView) findViewById(R.id.date_stamp);

        my_date_str = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());

        date.setText(my_date_str);
        attendance_list_btn = (RelativeLayout) findViewById(R.id.proceed_btn);
        autoCompleteTxt = findViewById(R.id.auto_complete_txt);
        autoCompleteTxtSem = findViewById(R.id.auto_complete_txt_sem);
        adapterItems = new ArrayAdapter<String>(this, R.layout.course_list, courses);
        adapterItemSem = new ArrayAdapter<String>(this, R.layout.course_list, semester);

        autoCompleteTxt.setAdapter(adapterItems);
        autoCompleteTxtSem.setAdapter(adapterItemSem);

        Button back= (Button) findViewById(R.id.button_back);
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

        attendance_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item == null && semitem == null){
                    Toast.makeText(ChooseAttendance.this, "Please Select Course & Semester!!", Toast.LENGTH_SHORT).show();
                }else if (item == null){
                    Toast.makeText(ChooseAttendance.this, "Please Select Course!!", Toast.LENGTH_SHORT).show();
                }
                else if (semitem == null){
                    Toast.makeText(ChooseAttendance.this, "Please Select Semester!!", Toast.LENGTH_SHORT).show();
                }else {
                    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                    DocumentReference docIdRef = rootRef.collection("Attendance").document(item).collection(semitem).document(my_date_str);
                    docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    if(document.getString("1") == null){
                                        Toast.makeText(ChooseAttendance.this, "Click Tick Btn After All Swipe To Go Back!!", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getBaseContext(), Attendance.class);
                                        intent.putExtra("course", item);
                                        intent.putExtra("semester", semitem);
                                        intent.putExtra("date", my_date_str);
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(ChooseAttendance.this, " You have Already Taken Today's Attendance!! ", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(ChooseAttendance.this, "Click Tick Btn After All Swipe To Go Back!!", Toast.LENGTH_LONG).show();
                                    Map<String, Object> docData = new HashMap<>();

                                    rootRef.collection("Attendance").document(item).collection(semitem).document(my_date_str).set(docData);

                                    Intent intent = new Intent(getBaseContext(), Attendance.class);
                                    intent.putExtra("course", item);
                                    intent.putExtra("semester", semitem);
                                    intent.putExtra("date", my_date_str);
                                    startActivity(intent);
                                }
                            } else {
                                Toast.makeText(ChooseAttendance.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}