package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import Model.Expense;
import arksoft.com.budgetmanager.R;

/**
 * Created by isaarikan on 19.04.2017.
 */

public class ExpenseAdapter extends BaseAdapter{

    Context c;
    ArrayList<Expense> expenses;



    public ExpenseAdapter(){}

    public ExpenseAdapter(Context c,ArrayList<Expense> expenses){
        this.expenses=expenses;
        this.c=c;


    }

    @Override
    public int getCount() {
        return expenses.size();
    }

    @Override
    public Object getItem(int position) {
        return expenses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView= LayoutInflater.from(c).inflate(R.layout.model,parent,false);
        }

        TextView nameTxt= (TextView) convertView.findViewById(R.id.nameTxt);
        TextView propTxt= (TextView) convertView.findViewById(R.id.aciklama);
        TextView descTxt= (TextView) convertView.findViewById(R.id.descTxt);



        Expense s= (Expense)getItem(position);
        nameTxt.setText(s.getIsim());
        propTxt.setText(s.getKategori());
        descTxt.setText((String.valueOf(s.getMikta())));

        return convertView;
    }
}
