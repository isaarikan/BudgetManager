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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Adapter.CustomAdapter;
import FireBase.FireBaseHelper;
import Model.Income;

public class MainActivity extends AppCompatActivity {
    DatabaseReference db;
    FireBaseHelper helper;
    FloatingActionButton fab;
    CustomAdapter adapter;
    ListView lv;
    EditText nameEditTxt, propTxt, descTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_content);

        //Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        lv=(ListView)findViewById(R.id.lv);

        db= FirebaseDatabase.getInstance().getReference();
        helper=new FireBaseHelper(db);
        adapter=new CustomAdapter(this,helper.retrieve());
        lv.setAdapter(adapter);

        adapter = new CustomAdapter(MainActivity.this, helper.retrieve());
        lv.setAdapter(adapter);
        fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayInput();
            }
        });




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
                int  amount= Integer.parseInt(descTxt.getText().toString());

                Income income=new Income(name,type,amount);

                income.setName(name);
                income.setType(type);
                income.setMiktar(amount);


                if (name != null && name.length() > 0) {
                    //THEN SAVE
                    if (helper.save(income)) {
                        //IF SAVED CLEAR EDITXT
                        d.cancel();
                        nameEditTxt.setText("");
                        propTxt.setText("");
                        descTxt.setText("");
                        adapter = new CustomAdapter(MainActivity.this, helper.retrieve());
                        lv.setAdapter(adapter);

                    }
                } else {
                    Toast.makeText(MainActivity.this, "Name Must Not Be Empty", Toast.LENGTH_SHORT).show();
                }

            }
        });

        d.show();




    }
}
