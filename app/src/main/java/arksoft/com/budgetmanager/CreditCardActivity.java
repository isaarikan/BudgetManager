package arksoft.com.budgetmanager;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import Adapter.CreditCardAdapter;
import Model.CreditCard;


public class CreditCardActivity extends Activity {
    DatabaseReference databaseReference;
    FloatingActionButton fab;
    ListView lv;
    Boolean saved;
    Spinner category;
    CreditCardAdapter aa;
    EditText isim, baslangic;

    ArrayList<CreditCard> accountlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_content);
        lv = (ListView) findViewById(R.id.accountlistview);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("CreditCard");
        getAccounts();
        aa = new CreditCardAdapter(CreditCardActivity.this, accountlist);

        lv.setAdapter(aa);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                DatabaseReference db_node = FirebaseDatabase.getInstance().getReference().child("CreditCard/Account/"+accountlist.get(position).getId());
                db_node.removeValue();
                accountlist.remove(position);
                aa.notifyDataSetChanged();
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

    public Boolean save(CreditCard account) {

        if (account == null) {

            saved = false;

        } else {

            try {
                String key =databaseReference.child("Account").push().getKey();
                account.setId(key);
                databaseReference.child("Account").child(key).setValue(account);
                aa.notifyDataSetChanged();
                saved = true;


            } catch (Exception ex) {

                ex.printStackTrace();

                saved = false;

            }
        }

        return saved;


    }


    private void fetchData(DataSnapshot dataSnapshot) {
        lv.setAdapter(aa);
        accountlist.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            CreditCard account = ds.getValue(CreditCard.class);
            accountlist.add(account);
            aa.notifyDataSetChanged();
        }


    }


    public ArrayList<CreditCard> getAccounts(){

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
        return accountlist;
    }




    private void displayInput() {
        final Dialog d = new Dialog(this);
        d.setTitle("Hesap Türü Girin");
        d.setContentView(R.layout.account_layout);
        category = (Spinner) d.findViewById(R.id.accountspinner);
        baslangic = (EditText) d.findViewById(R.id.baslangicMiktar);
        Button saveBtn = (Button) d.findViewById(R.id.saveBtn);
        DatePicker datePicker = (DatePicker)d.findViewById(R.id.account_date_picker);
        Date date = new Date();
        date.setMonth(datePicker.getMonth());
        date.setYear(datePicker.getYear()-1900);
        date.setDate(datePicker.getDayOfMonth());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        final String dateString = format.format(date);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryType = category.getSelectedItem().toString();
                String miktar = ((baslangic.getText().toString()));
                CreditCard account = new CreditCard(categoryType, miktar,"",dateString);
                if (miktar != "" && categoryType.length() > 0) {
                    //THEN SAVE
                    if (save(account)) {


                        //IF SAVED CLEAR EDITXT
                        d.cancel();


                    }
                } else {
                    Toast.makeText(CreditCardActivity.this, "Name Must Not Be Empty", Toast.LENGTH_SHORT).show();
                }

            }
        });

        d.show();


    }


}
