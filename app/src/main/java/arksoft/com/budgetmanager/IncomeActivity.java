package arksoft.com.budgetmanager;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import Adapter.CustomAdapter;
import Model.Income;

public class IncomeActivity extends AppCompatActivity {
    DatabaseReference db;

    FloatingActionButton fab;
    CustomAdapter adapter;
    ListView lv;
    Boolean saved;
    EditText nameEditTxt, propTxt, descTxt;
    ArrayList<Income> incomelist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_content);

        //Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        lv = (ListView) findViewById(R.id.lv);

        db = FirebaseDatabase.getInstance().getReference();
        //isa

        adapter = new CustomAdapter(IncomeActivity.this, getIncomes());
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayInput();
            }
        });


    }

    public Boolean save(Income income){

        if(income==null){

            saved=false;

        }else{

            try{


                db.child("Income").push().setValue(income);

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
        incomelist.clear();


        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            Income income=ds.getValue(Income.class);
            incomelist.add(income);
            adapter.notifyDataSetChanged();

        }

    }




    public ArrayList<Income> getIncomes(){

        db.addChildEventListener(new ChildEventListener() {
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
        return incomelist;

    }






    private void displayInput() {
        final Dialog d=new Dialog(this);

        d.setTitle("Gelir Girin");

        d.setContentView(R.layout.inputlayout);

        nameEditTxt = (EditText) d.findViewById(R.id.nameEditText);
        propTxt = (EditText) d.findViewById(R.id.propellantEditText);
        descTxt = (EditText) d.findViewById(R.id.descEditText);
        Button saveBtn = (Button) d.findViewById(R.id.saveBtn);


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditTxt.getText().toString();
                String type = propTxt.getText().toString();
                String  amount= String.valueOf(descTxt.getText().toString());

                Income income=new Income(name,type,amount);




                if (name != null && name.length() > 0) {
                    //THEN SAVE
                    if (save(income)) {


                        //IF SAVED CLEAR EDITXT
                        d.cancel();
                        nameEditTxt.setText("");
                       propTxt.setText("");
                        descTxt.setText("");


                    }
                } else {
                    Toast.makeText(IncomeActivity.this, "Name Must Not Be Empty", Toast.LENGTH_SHORT).show();
                }

            }
        });

        d.show();




    }



}
