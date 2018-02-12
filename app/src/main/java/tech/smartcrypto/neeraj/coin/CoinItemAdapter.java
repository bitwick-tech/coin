package tech.smartcrypto.neeraj.coin;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import utils.UtilFunctions;

/**
 * Created by neeraj on 28/1/18.
 */

public class CoinItemAdapter extends ArrayAdapter<Coin> {

    private Context ctx;
    private List<Coin> coinList;
    public CoinItemAdapter(Context context, List<Coin> coinList) {
        super(context, R.layout.coin_layout_user_list_row, coinList);
        this.ctx = context;
        this.coinList = coinList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        CoinHolder h = null;
        if (v == null) {
            // Inflate row layout
            LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.coin_layout_user_list_row, parent, false);
            // Look for Views in the layout
            ImageView coinImageIv = v.findViewById(R.id.coin_layout_user_list_coinIconIv);
            TextView coinNameTv = v.findViewById(R.id.coin_layout_user_list_coinNameTv);
            TextView coinIdTv = v.findViewById(R.id.coin_layout_user_list_coinIdTv);
            TextView coinPriceTv = v.findViewById(R.id.coin_layout_user_list_cpTv);
            TextView coinPriceChangeTv = v.findViewById(R.id.coin_layout_user_list_cpChangeTv);
            ImageView upOrDownIv = v.findViewById(R.id.coin_layout_user_list_upOrDownIv);

            h = new CoinHolder();
            h.coinImageIv = coinImageIv;
            h.coinNameTv = coinNameTv;
            h.coinIdTv = coinIdTv;
            h.coinPriceTv = coinPriceTv;
            h.coinPriceChangeTv = coinPriceChangeTv;
            h.upOrDownIv = upOrDownIv;

            v.setTag(h);
        }
        else h = (CoinHolder) v.getTag();

        Coin coin = coinList.get(position);
        int imgId = UtilFunctions.getResourseId(getContext(), coin.getId(), "drawable", getContext().getPackageName());
        if(imgId >= 0) h.coinImageIv.setImageResource(imgId);
        h.coinNameTv.setText(coin.getName());
        h.coinIdTv.setText(coin.getId().split("__")[0].toUpperCase());
        h.coinPriceTv.setText("INR  " + UtilFunctions.formatFloatTo4Decimals(coin.getCurrentPrice()));
        //TODO add percentage change
        if(true){//coin.getOpenPrice() > 0.0f) {
            float percentageChange = ((coin.getCurrentPrice() - coin.getOpenPrice()) * 100) / coin.getOpenPrice();
            percentageChange = -2.2f;
            h.coinPriceChangeTv.setVisibility(View.VISIBLE);
            h.coinPriceChangeTv.setText(UtilFunctions.formatFloatTo4Decimals(percentageChange) + " %");
            //TODO add logic and image for up or down price change
            if(percentageChange != 0.0f) h.upOrDownIv.setVisibility(View.VISIBLE);
            if(percentageChange > 0.0f) {
                h.coinPriceChangeTv.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
                h.upOrDownIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow_drop_up_black_24dp));
                DrawableCompat.setTint(h.upOrDownIv.getDrawable(), ContextCompat.getColor(getContext(), R.color.green));
            } else if(percentageChange < 0.0f) {
                h.coinPriceChangeTv.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                h.upOrDownIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow_drop_down_black_24dp));
                DrawableCompat.setTint(h.upOrDownIv.getDrawable(), ContextCompat.getColor(getContext(), R.color.red));
            }
        }

        return v;
    }

    // ViewHolder pattern
    static class CoinHolder {
        ImageView coinImageIv;
        TextView coinNameTv;
        TextView coinIdTv;
        TextView coinPriceTv;
        TextView coinPriceChangeTv;
        ImageView upOrDownIv;
    }



}
