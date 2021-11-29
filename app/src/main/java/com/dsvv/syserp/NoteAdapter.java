package com.dsvv.syserp;

//
//import android.content.Context;
//import android.view.ContentInfo;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//
//public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {
//
//    Context context;
//    List<student> list;
//
//    public NoteAdapter(Context context, List<student> list) {
//        this.context = context;
//        this.list = list;
//    }
//
//
//    @NonNull
//    @Override
//    public NoteAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list,parent,false);
//        return new MyViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        holder.text_view_title.setText(list.get(position).getTitle());
//        holder.text_view_priority.setText(list.get(position).getPriority());
//    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder{
//        TextView text_view_title, text_view_priority;
//
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//            text_view_title = itemView.findViewById(R.id.text_view_title);
//            text_view_priority = itemView.findViewById(R.id.text_view_priority);
//        }
//    }
//}
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class NoteAdapter extends FirestoreRecyclerAdapter<student, NoteAdapter.NoteHolder> {
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<student> options) {
        super(options);
    }

//    Map<String, Object> docData = new HashMap<>();

    public String roll;

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull student model) {
        holder.textViewName.setText(model.getName());
        holder.textViewRno.setText(String.valueOf(model.getRno()));
    }


    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list,parent, false);

        return new NoteHolder(v);
    }


    public void presentItem(int position, String c, String sm, String date, Map<String, Object> docData){

        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference docref = database.collection("Attendance").document(c).collection(sm).document(date);

        int pos = position + 1;
        String s=Integer.toString(pos);

        docData.replace(s, "present");

        docref.set(docData);
    }

    public void absentItem(int position, String c, String sm, String date, Map<String, Object> docData){
//        getSnapshots().getSnapshot(position).getReference().delete();
//        getSnapshots().getSnapshot(position).getReference().update(s, "absent");
//        getSnapshots().getSnapshot(position).getReference().set(docData);
//        getSnapshots().getSnapshot(position).getReference().set(docData);

        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference docref = database.collection("Attendance").document(c).collection(sm).document(date);

        int pos = position + 1;
        String s=Integer.toString(pos);

        docData.replace(s, "absent");

        docref.set(docData);
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewRno;

        public NoteHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewRno = itemView.findViewById(R.id.text_view_rno);

        }
    }
}
