package com.ds.DukeStudy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ds.DukeStudy.R;
import com.ds.DukeStudy.objects.Student;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * This is a generic Members Fragment. This will be expanded to work for classes and groups.
 */

public class MembersFragment extends Fragment {
    private DatabaseReference databaseRef;
    private FirebaseListAdapter<String> adapter1;
    private ListView membersListView;
    public Boolean isCourse=Boolean.TRUE;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle curBundle=getArguments();
        View view=inflater.inflate(R.layout.members_layout,null);
        membersListView=(ListView) view.findViewById(R.id.membersListView);
        databaseRef = FirebaseDatabase.getInstance().getReference();
        String sourceID=curBundle.getString("myid");
        DatabaseReference currentSourceStudentsRef;
        if(isCourse){
            currentSourceStudentsRef=databaseRef.child("courses").child(sourceID).child("studentKeys");}
        else{
            currentSourceStudentsRef=databaseRef.child("groups").child(sourceID).child("studentKeys");
        };
        final DatabaseReference studentsRef=databaseRef.child("students");
        adapter1=new FirebaseListAdapter<String>(getActivity(),String.class,android.R.layout.two_line_list_item,currentSourceStudentsRef) {
            @Override
            protected void populateView(final View v, final String model,final int position) {
                //Get reference to particular student in database
                DatabaseReference curStudentRef=studentsRef.child(model);
                final TextView mytext1=(TextView) v.findViewById(android.R.id.text1);
                final TextView mytext2=(TextView) v.findViewById(android.R.id.text2);
                curStudentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Student curStudent= dataSnapshot.getValue(Student.class);
                        mytext1.setText(curStudent.getName());
                        mytext2.setText(curStudent.getEmail());
                        v.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                                Context context = getActivity().getApplicationContext();
                                ViewProfileFragment nextFrag= new ViewProfileFragment();
                                Bundle mybundle=new Bundle();
                                mybundle.putString("myid",model.toString());
                                nextFrag.setArguments(mybundle);
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.flContent, nextFrag, null)
                                        .addToBackStack(null)
                                        .commit();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };
        membersListView.setAdapter(adapter1);
        return view;

    }
}