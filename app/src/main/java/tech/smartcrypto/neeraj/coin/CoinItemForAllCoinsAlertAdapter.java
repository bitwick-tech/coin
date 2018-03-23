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
import com.squareup.picasso.Picasso;

import java.util.List;

import utils.UtilFunctions;

/**
 * Created by neerajlajpal on 07/02/18.
 */

public class CoinItemForAllCoinsAlertAdapter extends ArrayAdapter<CoinStaticDataDao.CoinIdAndName> implements ListAdapter{

    private Context ctx;
    private List<CoinStaticDataDao.CoinIdAndName> coinIdList;
    public CoinItemForAllCoinsAlertAdapter(Context context, List<CoinStaticDataDao.CoinIdAndName> coinIdList) {
        super(context, R.layout.coin_layout_all_coins_row, coinIdList);
        this.ctx = context;
        this.coinIdList = coinIdList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        CoinHolder h = null;
        if (v == null) {
            // Inflate row progress_animation
            LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.coin_layout_all_coins_row, parent, false);
            // Look for Views in the progress_animation
            ImageView coinImageIv = v.findViewById(R.id.coin_layout_all_coins_list_coinIconIv);
            TextView coinNameTv = v.findViewById(R.id.coin_layout_all_coins_list_coinIdTv);
            TextView coinIdTv = v.findViewById(R.id.coin_layout_all_coins_list_coinNameTv);

            h = new CoinHolder();
            h.coinImageIv = coinImageIv;
            h.coinNameTv = coinNameTv;
            h.coinIdTv = coinIdTv;
            v.setTag(h);
        }
        else
            h = (CoinHolder) v.getTag();

        String coinId = coinIdList.get(position).coinId;
        String coinName = coinIdList.get(position).coinName;
        Picasso.get()
                .load(ctx.getResources().getString(R.string.staticImgURL) + coinId + ".png")
                .into(h.coinImageIv);
//        int imgId = UtilFunctions.getResourseId(getContext(), coinId, "drawable", getContext().getPackageName());
//        if(imgId >= 0)
//            h.coinImageIv.setImageResource(imgId);
//        String[] info = coinId.split("__");
//        info[0] = info[0].toUpperCase();
//        h.coinNameTv.setText(Joiner.on("  ").join(info));
        h.coinIdTv.setText(coinId.toUpperCase());
        h.coinNameTv.setText(coinName);

        return v;
    }

    // ViewHolder pattern
    static class CoinHolder {
        ImageView coinImageIv;
        TextView coinIdTv;
        TextView coinNameTv;
    }

}
