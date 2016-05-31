package com.show.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.show.R;
import com.show.utils.CommonUtils;
import com.parse.ParseObject;
import java.util.List;

/**
 * Created by abhijeet on 04/01/16.
 */
public class ShowAdapter extends ArrayAdapter<ParseObject> {
    List<ParseObject> items;
    Context context;

    public ShowAdapter(Context context, List<ParseObject> items) {
        super(context, -1, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.debtors, parent, false);
        }

        TextView name = (TextView) v.findViewById(R.id.name);
        TextView balance = (TextView) v.findViewById(R.id.balance);

        name.setText(items.get(position).getString("name"));
        balance.setText(CommonUtils.getCurrencyFormat().format(items.get(position).getNumber("balance")));

        return v;
    }

    public void setItems(List<ParseObject> items) {
        this.items = items;
        notifyDataSetChanged();
    }
}
