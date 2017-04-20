package arksoft.com.budgetmanager;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import Adapter.ExpenseAdapter;
import Model.Expense;

/**
 * Created by isaarikan on 19.04.2017.
 */

public class ExpenseActivity extends Activity{

    DatabaseReference db;

    FloatingActionButton fab;
    ExpenseAdapter adapter;
    ListView lv;
    Boolean saved;
    EditText nameEditTxt, propTxt, descTxt;
    ArrayList<Expense> expenselist = new ArrayList<Expense>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_content);

        //Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        lv = (ListView) findViewById(R.id.listview);

        db = FirebaseDatabase.getInstance().getReference();
        //isa

        expenselist.clear();
        adapter = new ExpenseAdapter(ExpenseActivity.this, expenselist);
        getExpenses();
        adapter.notifyDataSetChanged();
        lv.setAdapter(adapter);





        //isa





        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayInput();
            }
        });


    }

    public Boolean save(Expense expense){

        if(expense==null){

            saved=false;

        }else{

            try{


                db.child("Expense").push().setValue(expense);

                saved=true;
            }catch (Exception ex){

                ex.printStackTrace();;
                saved=false;

            }
        }

        return saved;



    }



    public void fetchData(DataSnapshot dataSnapshot)
    {




        for (DataSnapshot ds : dataSnapshot.getChildren())
        {

            Expense expense=ds.getValue(Expense.class);
            expenselist.add(expense);
           // adapter.notifyDataSetChanged();




        }

    }




    public ArrayList<Expense> getExpenses(){



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

        return expenselist;

    }






    private void displayInput() {
        final Dialog d=new Dialog(this);

        d.setTitle("Gelir Girin");

        d.setContentView(R.layout.expense_layout);

        nameEditTxt = (EditText) d.findViewById(R.id.name);
        propTxt = (EditText) d.findViewById(R.id.kategori);
        descTxt = (EditText) d.findViewById(R.id.miktar);
        Button saveBtn = (Button) d.findViewById(R.id.saveBtn);


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditTxt.getText().toString();
                String type = propTxt.getText().toString();
                String  amount= String.valueOf(descTxt.getText().toString());

                Expense expense=new Expense(name,type,amount);




                if (name != null && name.length() > 0) {
                    //THEN SAVE
                    if (save(expense)) {


                        //IF SAVED CLEAR EDITXT
                        d.cancel();
                        nameEditTxt.setText("");
                        propTxt.setText("");
                        descTxt.setText("");


                    }
                } else {
                    Toast.makeText(ExpenseActivity.this, "Name Must Not Be Empty", Toast.LENGTH_SHORT).show();
                }

            }
        });

        d.show();




    }


}
