package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import Model.Income;
import arksoft.com.budgetmanager.R;




/**
 * Created by isaarikan on 18.04.2017.
 */

public class CustomAdapter extends BaseAdapter{
    Context c;
    ArrayList<Income> incomes;

    public CustomAdapter(Context c,ArrayList<Income> incomes){
        this.incomes=incomes;
        this.c=c;


    }

    @Override
    public int getCount() {
        return incomes.size();
    }

    @Override
    public Object getItem(int position) {
        return incomes.get(position);
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
        TextView propTxt= (TextView) convertView.findViewById(R.id.propellantTxt);
        TextView descTxt= (TextView) convertView.findViewById(R.id.descTxt);



        final Income s= (Income) this.getItem(position);

        nameTxt.setText(s.getName());
        propTxt.setText(s.getType());
        //descTxt.setText(s.getMiktar());

        return convertView;
    }
}
