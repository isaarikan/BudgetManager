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
import java.util.List;

import Adapter.ExpenseAdapter;
import FireBase.FireBaseHelper;
import Model.Expense;

/**
 * Created by isaarikan on 19.04.2017.
 */

public class ExpenseActivity extends Activity{

    DatabaseReference db;
    FireBaseHelper helper;
    FloatingActionButton fab;
    ExpenseAdapter adapter;
    ListView lv;
    EditText nameEditTxt, propTxt, descTxt;
    List<Expense> expenselist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_content);

        //Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        lv=(ListView)findViewById(R.id.lv);

        db= FirebaseDatabase.getInstance().getReference();
        //isa
        helper=new FireBaseHelper(db);
        adapter=new ExpenseAdapter(this,helper.getExpenses());
        lv.setAdapter(adapter);


        fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayInput();
            }
        });

        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Expense doc =dataSnapshot.getValue(Expense.class);
                expenselist.add(doc);
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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


    }

    private void displayInput() {
        final Dialog d=new Dialog(this);

        d.setTitle("Gider Girin");

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

                Expense expense=new Expense(name,type,amount);




                if (name != null && name.length() > 0) {
                    //THEN SAVE
                    if (helper.saveExpense(expense)) {
                        //IF SAVED CLEAR EDITXT

                        nameEditTxt.setText("");
                        propTxt.setText("");
                        descTxt.setText("");
                        adapter = new ExpenseAdapter(ExpenseActivity.this, helper.getExpenses());
                        lv.setAdapter(adapter);
                        d.cancel();

                    }
                } else {
                    Toast.makeText(ExpenseActivity.this, "Name Must Not Be Empty", Toast.LENGTH_SHORT).show();
                }

            }
        });

        d.show();




    }
}
