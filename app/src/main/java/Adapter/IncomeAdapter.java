package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import Model.Income;
import arksoft.com.budgetmanager.R;




public class IncomeAdapter extends BaseAdapter{





    Context c;
    Button update,delete;
    ArrayList<Income> incomes;
   public static int incomesize=0;


    public IncomeAdapter(){}

    public IncomeAdapter(Context c, ArrayList<Income> incomes){


        this.incomes=incomes;
        this.c=c;


    }

    //kac tane eleman varsa sayısını döndür
    @Override
    public  int getCount() {
        incomesize=incomes.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            //Tasarım dosyasını al donuştür.
            convertView= LayoutInflater.from(c).inflate(R.layout.income_model,parent,false);
        }


        //Gelen nesneye göre bunları ata
        TextView nameTxt= (TextView) convertView.findViewById(R.id.nameTxt);
        TextView propTxt= (TextView) convertView.findViewById(R.id.aciklama);
        TextView descTxt= (TextView) convertView.findViewById(R.id.descTxt);
        TextView tarih=(TextView)convertView.findViewById(R.id.tarih);



        //Pozisyonuna göre nesneyi al textviewlara at
         Income s= (Income) this.getItem(position);
        nameTxt.setText(s.getType());
        propTxt.setText((String.valueOf(s.getMiktar())));
        descTxt.setText(s.getName());
        tarih.setText(s.getDate());


        return convertView;
    }
}
