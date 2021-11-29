package com.dsvv.syserp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ViewAttendance extends AppCompatActivity {

    ListView lsv_students;
    ArrayList<String> student_status;
    String course, sem, selectedDate;
    RelativeLayout back_btn;

    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);

        back_btn = findViewById(R.id.button_back_in_toolbar);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChooseAttendanceInView.class));
            }
        });

        lsv_students = findViewById(R.id.lsv_Students);

        student_status = getIntent().getStringArrayListExtra("list");
        course = getIntent().getStringExtra("course");
        sem = getIntent().getStringExtra("semester");
        selectedDate = getIntent().getStringExtra("date");

        ArrayList<showStudentsAttendance> arr = new ArrayList<>();

        CollectionReference colIdRef = rootRef.collection("Students").document(course).collection(sem);
        colIdRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int i = 0;
                for (QueryDocumentSnapshot document: Objects.requireNonNull(task.getResult())){
                    Map<String, Object> user = document.getData();
                    String name = user.get("name").toString();
                    arr.add(new showStudentsAttendance(R.drawable.profile3, student_status.get(i), name));

                    i = i + 1;
                }

                showStudentsAttendanceAdapter adapter = new showStudentsAttendanceAdapter(ViewAttendance.this, 0, arr);
                lsv_students.setAdapter(adapter);
            }
        });

//        Log.d("TAG", "onCreate: " + username.size());


//        for (int i = 0; i < student_status.size(); i++){
//            Log.d("TAG", "onCreate: ");
//            arr.add(new showStudentsAttendance(R.drawable.profile3, student_status.get(i), "l"));
//        }
    }
}