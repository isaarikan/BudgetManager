package arksoft.com.budgetmanager;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Adapter.ExpenseAdapter;
import Model.Expense;


public class ExpenseActivity extends Activity {

    DatabaseReference db;
    AlarmManager alarmManager;
    FloatingActionButton fab;
    ExpenseAdapter adapter;
    ListView lv;
    Boolean saved;
    TextView toplam;
    EditText nameEditTxt, descTxt;
    Spinner kategori;
    int toplama = 0;

    public static ArrayList<Integer> toplamMiktar = new ArrayList<>();
    public static ArrayList<Expense> expenselist = new ArrayList<>();
//fatih
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_content);

        //Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        lv = (ListView) findViewById(R.id.listview);





        db = FirebaseDatabase.getInstance().getReference().child("Expense");
        db.keepSynced(true);

        
        expenselist.clear();
        getExpenses();
        adapter = new ExpenseAdapter(ExpenseActivity.this, expenselist);
        lv.setAdapter(adapter);


        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {




            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                DatabaseReference db_node = FirebaseDatabase.getInstance().getReference().child("Expense/Expense/" + expenselist.get(position).getId());
                db_node.removeValue();
                expenselist.remove(position);
                adapter.notifyDataSetChanged();
                return true;
            }

        });

        adapter.notifyDataSetChanged();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayInput();
            }
        });
    }

    public Boolean save(Expense expense) {

        if (expense == null) {

            saved = false;

        } else {

            try {


                String key = db.child("Expense").push().getKey();
                expense.setId(key);
                db.child("Expense").child(key).setValue(expense);
                adapter.notifyDataSetChanged();
                saved = true;
                toplamMiktar.add(Integer.parseInt(expense.getMikta()));
            } catch (Exception ex) {

                ex.printStackTrace();
                ;
                saved = false;

            }
        }

        return saved;


    }


    public void fetchData(DataSnapshot dataSnapshot) {
        Expense expense = null;
        expenselist.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            expense = ds.getValue(Expense.class);
            expenselist.add(expense);

            adapter.notifyDataSetChanged();


        }

    }


    public ArrayList<Expense> getExpenses() {
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
                adapter.notifyDataSetChanged();


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
        final Dialog d = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);


        d.setTitle("Gelir Girin");
        d.setContentView(R.layout.expense_layout);
        nameEditTxt = (EditText) d.findViewById(R.id.name);
        kategori = (Spinner) d.findViewById(R.id.kategori);
        descTxt = (EditText) d.findViewById(R.id.miktar);
        DatePicker datePicker = (DatePicker) d.findViewById(R.id.expense_layout_datepicker);
        Date date = new Date();
        date.setMonth(datePicker.getMonth());
        date.setYear(datePicker.getYear() - 1900);
        date.setDate(datePicker.getDayOfMonth());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        final String dateString = format.format(date);

        Button saveBtn = (Button) d.findViewById(R.id.saveBtn);


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditTxt.getText().toString();
                String categoryType = kategori.getSelectedItem().toString();
                String amount = String.valueOf(descTxt.getText().toString());

                Expense expense = new Expense(name, amount,categoryType, "", dateString);


                if (name != null && name.length() > 0) {
                    //THEN SAVE
                    if (save(expense)) {


                        //IF SAVED CLEAR EDITXT
                        d.cancel();
                        nameEditTxt.setText("");
                        descTxt.setText("");


                    }
                } else {
                    Toast.makeText(ExpenseActivity.this, "Name Must Not Be Empty", Toast.LENGTH_SHORT).show();
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
