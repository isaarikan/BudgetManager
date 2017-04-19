package FireBase;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import Model.Income;

/**
 * Created by isaarikan on 18.04.2017.
 */

public class FireBaseHelper {

    DatabaseReference databaseReference;
    Boolean saved;
    ArrayList<Income> incomes=new ArrayList<>();


    public FireBaseHelper(DatabaseReference databaseReference){
        this.databaseReference=databaseReference;


    }

    public Boolean save(Income income){

        if(income==null){

            saved=false;

        }else{

            try{

                databaseReference.child("Income").push().setValue(income);
                saved=true;
            }catch (Exception ex){

                ex.printStackTrace();;
                saved=false;

            }
        }

        return saved;


    }

    private void fetchData(DataSnapshot dataSnapshot)
    {
        incomes.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            Income income=ds.getValue(Income.class);
            incomes.add(income);
        }
    }


    public ArrayList<Income> retrieve(){

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return incomes;

    }



}
