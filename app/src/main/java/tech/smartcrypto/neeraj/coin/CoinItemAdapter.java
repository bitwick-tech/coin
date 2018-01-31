package tech.smartcrypto.neeraj.coin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by neeraj on 28/1/18.
 */

public class CoinItemAdapter extends ArrayAdapter<Coin> {

    private Context ctx;
    private List<Coin> coinList;
    public CoinItemAdapter(Context context, List<Coin> coinList) {
        super(context, R.layout.coin_layout);
        this.ctx = context;
        this.coinList = coinList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        coinHolder h = null;
        if (v == null) {
            // Inflate row layout
            LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.coin_layout, parent, false);
            // Look for Views in the layout
            ImageView iv = (ImageView) v.findViewById(R.id.tagView);
            TextView nameTv = (TextView) v.findViewById(R.id.idView);
            TextView descrView = (TextView) v.findViewById(R.id.descriptionView);
            //TextView dateView = (TextView) v.findViewById(R.id.dateView);
            h = new coinHolder();
            h.tagView = iv;
            h.idView = nameTv;
            h.descriptionView = descrView;
            //h.dateView = dateView;
            v.setTag(h);
        }
        else
            h = (coinHolder) v.getTag();

        h.idView.setText(coinList.get(position).getId());
        h.descriptionView.setText(coinList.get(position).getDescription());
        h.tagView.setBackgroundResource(coinList.get(position).getTag().getTagColor());
        //h.dateView.setText(sdf.format(itemList.get(position).getDate()));

        return v;
    }

    // ViewHolder pattern
    static class coinHolder {
        ImageView tagView;
        TextView idView;
        TextView descriptionView;
        //TextView dateView;
    }





}
