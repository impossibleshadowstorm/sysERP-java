package com.dsvv.syserp;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.motion.widget.OnSwipe;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Attendance extends AppCompatActivity {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = null;

    String course, semester, date;
    int student_count = 0;
    Map<String, Object> docData = new HashMap<>(); //storage for present / absent


    private NoteAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        FloatingActionButton donebtn = findViewById(R.id.button_done);

        donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDashboard();
            }
        });

        Intent intent = getIntent();
        course = getIntent().getStringExtra("course");
        semester = getIntent().getStringExtra("semester");
        date = getIntent().getStringExtra("date");

        notebookRef = db.collection("Students").document(course).collection(semester);

        notebookRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        student_count++;
                        docData.put(Integer.toString(student_count), "absent");;
                    }
                } else {
                    student_count = 0;
                }
            }
        });

        setUpRecyclerView();
    }

    private void openDashboard() {
        startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
    }

    private void setUpRecyclerView(){
        Query query = notebookRef.orderBy("rno", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<student> options = new FirestoreRecyclerOptions.Builder<student>()
                .setQuery(query, student.class)
                .build();

        adapter = new NoteAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction == ItemTouchHelper.LEFT){
                    adapter.presentItem(viewHolder.getAdapterPosition(), course, semester, date, docData);

                    Toast.makeText(Attendance.this, "Present!!" + viewHolder.getAdapterPosition(), Toast.LENGTH_SHORT).show();

                }
                else{
                    adapter.absentItem(viewHolder.getAdapterPosition(), course, semester, date, docData);

                    Toast.makeText(Attendance.this, "Absent!! " + viewHolder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                }
            }
        }).attachToRecyclerView(recyclerView);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}