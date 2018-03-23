package tech.smartcrypto.neeraj.coin;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import utils.UtilFunctions;

/**
 * Created by neeraj on 28/1/18.
 */

public class ExchangeItemForCoinDetailAdapter extends ArrayAdapter<Coin> {

    private Context ctx;
    private List<Coin> coinList;
    public ExchangeItemForCoinDetailAdapter(Context context, List<Coin> coinList) {
        super(context, R.layout.coin_layout_user_list_row, coinList);
        this.ctx = context;
        this.coinList = coinList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        CoinHolder h = null;
        if (v == null) {
            // Inflate row progress_animation
            LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.exchange_layout_coin_detail_row, parent, false);
            // Look for Views in the progress_animation
            ImageView exIv = v.findViewById(R.id.exchange_layout_coin_detail_exIconIv);
            TextView exNameTv = v.findViewById(R.id.exchange_layout_coin_detail_exNameTv);
//            Spinner currSp = v.findViewById(R.id.exchange_layout_coin_detail_currSp);
            TextView coinPriceTv = v.findViewById(R.id.exchange_layout_coin_detail_cpTv);
            TextView coinPriceChangeTv = v.findViewById(R.id.exchange_layout_coin_detail_percentageTv);
            ImageView upOrDownIv = v.findViewById(R.id.exchange_layout_coin_detail_upOrDownIv);

            h = new CoinHolder();
            h.exIv = exIv;
            h.exNameTv = exNameTv;
//            h.currSp = currSp;
            h.coinPriceTv = coinPriceTv;
            h.coinPriceChangeTv = coinPriceChangeTv;
            h.upOrDownIv = upOrDownIv;

            v.setTag(h);
        }
        else h = (CoinHolder) v.getTag();

        Coin coin = coinList.get(position);
        h.exNameTv.setText(UtilFunctions.capitalizeFirstChar(coin.getEx()));
        Picasso.get().load(ctx.getResources().getString(R.string.staticImgURL) + coin.getEx() + ".png").into(h.exIv);
//        int imgId = UtilFunctions.getResourseId(getContext(), coin.getId(), "drawable", getContext().getPackageName());
//        if(imgId >= 0) h.coinImageIv.setImageResource(imgId);
//        if(coin.getName() != null) {
//            int lastIndexOfSpace = coin.getName().lastIndexOf( " ");
//            if(lastIndexOfSpace == -1)  lastIndexOfSpace = coin.getName().length();
//            h.coinNameTv.setText(coin.getName().substring(0, lastIndexOfSpace));
//        }
//        String[] idArr = coin.getId().split("__");
//        h.coinIdTv.setText(idArr[0].toUpperCase() + " " + idArr[1]);
        h.coinPriceTv.setText("INR " + UtilFunctions.formatFloatTo4Decimals(coin.getCp()));
        if(coin.getOp() > 0.0f) {
            float percentageChange = ((coin.getCp() - coin.getOp()) * 100) / coin.getOp();
            //percentageChange = 2.2f;
            h.coinPriceChangeTv.setVisibility(View.VISIBLE);
            h.coinPriceChangeTv.setText(UtilFunctions.formatFloatTo4DecimalsPercentage(percentageChange) + " %");
            if(percentageChange >= 0.0f) {
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
        ImageView exIv;
        TextView exNameTv;
//        Spinner currSp;
        TextView coinPriceTv;
        ImageView upOrDownIv;
        TextView coinPriceChangeTv;
    }



}
