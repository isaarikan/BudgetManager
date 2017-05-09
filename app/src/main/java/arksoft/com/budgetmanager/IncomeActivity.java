package arksoft.com.budgetmanager;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Adapter.IncomeAdapter;
import Model.Income;

public class IncomeActivity extends AppCompatActivity  {
    DatabaseReference db;
    Button update, delete;
    FloatingActionButton fab;
    IncomeAdapter adaptera;
    ListView lv;

    public static int incomesize;
    Boolean saved;
    EditText nameEditTxt, descTxt;
    Spinner kategori;

    public static ArrayList<Integer> toplamGelir = new ArrayList<>();
    public static ArrayList<Income> incomelist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_content);

        //Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        lv = (ListView) findViewById(R.id.lv);

        //Database oluşturuldu


        db = FirebaseDatabase.getInstance().getReference().child("Income");
        db.keepSynced(true);


        //fatih
        getIncomes();
        adaptera = new IncomeAdapter(IncomeActivity.this, incomelist);
        lv.setAdapter(adaptera);


        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                DatabaseReference db_node = FirebaseDatabase.getInstance().getReference().child("Income/Income/"+incomelist.get(position).getId());
                db_node.removeValue();
                incomelist.remove(position);
                adaptera.notifyDataSetChanged();
                return true;
            }

        });


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayInput();
            }
        });





    }

    //Kayıt ekleme
    public Boolean save(Income income) {

        if (income == null) {

            saved = false;

        } else {

            try {


                String key =db.child("Income").push().getKey();
                income.setId(key);
                db.child("Income").child(key).setValue(income);
                adaptera.notifyDataSetChanged();
                saved = true;
            } catch (Exception ex) {

                ex.printStackTrace();

                saved = false;

            }
        }

        return saved;


    }


    private void veriyiGetir(DataSnapshot dataSnapshot) {
        incomelist.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Income income = ds.getValue(Income.class);
            incomelist.add(income);
            toplamGelir.add(Integer.parseInt(income.getMiktar()));
            adaptera.notifyDataSetChanged();


        }





    }


    public ArrayList<Income> getIncomes() {

        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                veriyiGetir(dataSnapshot);


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                veriyiGetir(dataSnapshot);

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

        //diyalog oluştur
        final Dialog d=new Dialog(this,android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);

        d.setTitle("Gelir Girin");

        //diyalogu n arayüzünü set et
        d.setContentView(R.layout.inputlayout);

        nameEditTxt = (EditText) d.findViewById(R.id.nameEditText);
        kategori = (Spinner) d.findViewById(R.id.kategori);
        descTxt = (EditText) d.findViewById(R.id.descEditText);
        Button saveBtn = (Button) d.findViewById(R.id.saveBtn);
        DatePicker datePicker = (DatePicker)d.findViewById(R.id.inputlayout_datepicker);


        final Date date = new Date();
        date.setMonth(datePicker.getMonth());
        date.setYear(datePicker.getYear()-1900);
        date.setDate(datePicker.getDayOfMonth());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        final String dateString = format.format(date);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditTxt.getText().toString();
                String categoryType = kategori.getSelectedItem().toString();
                String amount = (descTxt.getText().toString());

                Income income = new Income(name, amount,categoryType,"",dateString);
                if (name != null && name.length() > 0) {
                    //THEN SAVE
                    if (save(income)) {
                        //IF SAVED CLEAR EDITXT
                        d.cancel();
                        nameEditTxt.setText("");

                        descTxt.setText("");


                    }
                } else {
                    Toast.makeText(IncomeActivity.this, "Name Must Not Be Empty", Toast.LENGTH_SHORT).show();
                }

            }
        });

        d.show();


    }

    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }



}
