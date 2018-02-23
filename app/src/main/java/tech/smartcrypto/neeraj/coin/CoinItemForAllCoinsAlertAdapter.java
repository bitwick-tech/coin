package tech.smartcrypto.neeraj.coin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.common.base.Joiner;

import java.util.List;

import utils.UtilFunctions;

/**
 * Created by neerajlajpal on 07/02/18.
 */

public class CoinItemForAllCoinsAlertAdapter extends ArrayAdapter<String> implements ListAdapter{

    private Context ctx;
    private List<String> coinIdList;
    public CoinItemForAllCoinsAlertAdapter(Context context, List<String> coinIdList) {
        super(context, R.layout.coin_layout_all_coins_row, coinIdList);
        this.ctx = context;
        this.coinIdList = coinIdList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        CoinHolder h = null;
        if (v == null) {
            // Inflate row layout
            LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.coin_layout_all_coins_row, parent, false);
            // Look for Views in the layout
            ImageView coinImageIv = v.findViewById(R.id.coin_layout_all_coins_list_coinIconIv);
            TextView coinNameTv = v.findViewById(R.id.coin_layout_all_coins_list_coinIdTv);

            h = new CoinHolder();
            h.coinImageIv = coinImageIv;
            h.coinNameTv = coinNameTv;
            v.setTag(h);
        }
        else
            h = (CoinHolder) v.getTag();

        String coinId = coinIdList.get(position);
        int imgId = UtilFunctions.getResourseId(getContext(), coinId, "drawable", getContext().getPackageName());
        if(imgId >= 0)
            h.coinImageIv.setImageResource(imgId);
        String[] info = coinId.split("__");
        info[0] = info[0].toUpperCase();
        h.coinNameTv.setText(Joiner.on("  ").join(info));

        return v;
    }

    // ViewHolder pattern
    static class CoinHolder {
        ImageView coinImageIv;
        TextView coinNameTv;
    }

}
