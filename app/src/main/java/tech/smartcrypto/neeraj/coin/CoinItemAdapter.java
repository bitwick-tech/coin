package tech.smartcrypto.neeraj.coin;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

import utils.UtilFunctions;

/**
 * Created by neeraj on 28/1/18.
 */

public class CoinItemAdapter extends ArrayAdapter<String> {

    private Context ctx;
    private Map<String, List<Coin>> coinsHash;
    private Map<String, String> coinIdNameHash;
    private List<String> coinIdList;

    public CoinItemAdapter(Context context, Map<String, String> coinIdNameHash, Map<String, List<Coin>> coinsHash, List<String> coinIdList) {
        super(context, R.layout.coin_layout_user_list_row, coinIdList);
        this.ctx = context;
        this.coinIdList = coinIdList;
        this.coinsHash = coinsHash;
        this.coinIdNameHash = coinIdNameHash;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        CoinHolder h = null;
        if (v == null) {
            // Inflate row progress_animation
            LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.coin_layout_user_list_row, parent, false);
            // Look for Views in the progress_animation
            ImageView coinImageIv = v.findViewById(R.id.coin_layout_user_list_coinIconIv);
            TextView coinNameTv = v.findViewById(R.id.coin_layout_user_list_coinNameTv);
            TextView coinIdTv = v.findViewById(R.id.coin_layout_user_list_coinIdTv);
            RelativeLayout e1Ll = v.findViewById(R.id.coin_layout_user_list_e1Ll);
            TextView e1Tv = v.findViewById(R.id.coin_layout_user_list_e1Tv);
            TextView e1CpTv = v.findViewById(R.id.coin_layout_user_list_e1CpTv);
            ImageView e1UpOrDownIv = v.findViewById(R.id.coin_layout_user_list_e1UpOrDownIv);
            TextView e1PercentageTv = v.findViewById(R.id.coin_layout_user_list_e1PercentageTv);

            RelativeLayout e2Ll = v.findViewById(R.id.coin_layout_user_list_e2Ll);
            TextView e2Tv = v.findViewById(R.id.coin_layout_user_list_e2Tv);
            TextView e2CpTv = v.findViewById(R.id.coin_layout_user_list_e2CpTv);
            ImageView e2UpOrDownIv = v.findViewById(R.id.coin_layout_user_list_e2UpOrDownIv);
            TextView e2PercentageTv = v.findViewById(R.id.coin_layout_user_list_e2PercentageTv);

            RelativeLayout e3Ll = v.findViewById(R.id.coin_layout_user_list_e3Ll);
            TextView e3Tv = v.findViewById(R.id.coin_layout_user_list_e3Tv);
            TextView e3CpTv = v.findViewById(R.id.coin_layout_user_list_e3CpTv);
            ImageView e3UpOrDownIv = v.findViewById(R.id.coin_layout_user_list_e3UpOrDownIv);
            TextView e3PercentageTv = v.findViewById(R.id.coin_layout_user_list_e3PercentageTv);

            h = new CoinHolder();
            h.coinImageIv = coinImageIv;
            h.coinNameTv = coinNameTv;
            h.coinIdTv = coinIdTv;
            h.e1Ll = e1Ll;
            h.e1Tv = e1Tv;
            h.e1CpTv = e1CpTv;
            h.e1UpOrDownIv = e1UpOrDownIv;
            h.e1PercentageTv = e1PercentageTv;
            h.e2Ll = e2Ll;
            h.e2Tv = e2Tv;
            h.e2CpTv = e2CpTv;
            h.e2UpOrDownIv = e2UpOrDownIv;
            h.e2PercentageTv = e2PercentageTv;
            h.e3Ll = e3Ll;
            h.e3Tv = e3Tv;
            h.e3CpTv = e3CpTv;
            h.e3UpOrDownIv = e3UpOrDownIv;
            h.e3PercentageTv = e3PercentageTv;
//            h.exLv = exLv;
//            h.coinPriceTv = coinPriceTv;
//            h.coinPriceChangeTv = coinPriceChangeTv;
//            h.upOrDownIv = upOrDownIv;

            v.setTag(h);
        }
        else h = (CoinHolder) v.getTag();

        if(coinIdList == null || coinIdList.size() < 1 || position < 0) return v;

        String coinId = coinIdList.get(position);

        Picasso.get()
                .load(ctx.getResources().getString(R.string.staticImgURL) + coinId + ".png")
                .into(h.coinImageIv);

        h.coinIdTv.setText(coinId.toUpperCase());
        h.coinNameTv.setText(coinIdNameHash.get(coinId));

        int exSize = 0;
        if(coinsHash.get(coinId) != null && coinsHash.get(coinId).size() > 0) {
            exSize = coinsHash.get(coinId).size();
            if(exSize >= 1) {
//                if(exSize == 1) {
//                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) h.e1Ll.getLayoutParams();
//                    params.addRule(RelativeLayout.CENTER_VERTICAL);
//                    h.e1Ll.setLayoutParams(params);
//                }
                h.e1Tv.setText(UtilFunctions.capitalizeFirstChar(coinsHash.get(coinId).get(0).getEx()));
                float oP = coinsHash.get(coinId).get(0).getOp();
                float cP = coinsHash.get(coinId).get(0).getCp();
                h.e1CpTv.setText(UtilFunctions.formatFloatTo4Decimals(cP));
                float percentageChange = 0.0f;
                if(oP > 0.0f) {
                    percentageChange = ((cP - oP) * 100) / oP;
                }
//                percentageChange = 2.2f;
                h.e1PercentageTv.setText(UtilFunctions.formatFloatTo4DecimalsPercentage(percentageChange) + " %");
                if(percentageChange > 0.0f) {
                    h.e1PercentageTv.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
                    h.e1UpOrDownIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow_drop_up_black_24dp));
                    DrawableCompat.setTint(h.e1UpOrDownIv.getDrawable(), ContextCompat.getColor(getContext(), R.color.green));
                } else if(percentageChange < 0.0f) {
                    h.e1PercentageTv.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                    h.e1UpOrDownIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow_drop_down_black_24dp));
                    DrawableCompat.setTint(h.e1UpOrDownIv.getDrawable(), ContextCompat.getColor(getContext(), R.color.red));
                }

                if(exSize == 1) {
                    h.e2Ll.setVisibility(View.GONE);
                    h.e3Ll.setVisibility(View.GONE);
                }
            }
            if(exSize >= 2) {
                h.e2Ll.setVisibility(View.VISIBLE);
                h.e2Tv.setText(UtilFunctions.capitalizeFirstChar(coinsHash.get(coinId).get(1).getEx()));
                float oP = coinsHash.get(coinId).get(1).getOp();
                float cP = coinsHash.get(coinId).get(1).getCp();
                h.e2CpTv.setText(UtilFunctions.formatFloatTo4Decimals(cP));
                float percentageChange = 0.0f;
                if(oP > 0.0f) {
                    percentageChange = ((cP - oP) * 100) / oP;
                }
                h.e2PercentageTv.setText(UtilFunctions.formatFloatTo4DecimalsPercentage(percentageChange) + " %");
                if(percentageChange > 0.0f) {
                    h.e2PercentageTv.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
                    h.e2UpOrDownIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow_drop_up_black_24dp));
                    DrawableCompat.setTint(h.e2UpOrDownIv.getDrawable(), ContextCompat.getColor(getContext(), R.color.green));
                } else if(percentageChange < 0.0f) {
                    h.e2PercentageTv.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                    h.e2UpOrDownIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow_drop_down_black_24dp));
                    DrawableCompat.setTint(h.e2UpOrDownIv.getDrawable(), ContextCompat.getColor(getContext(), R.color.red));
                }

                if(exSize == 2) {
                    h.e3Ll.setVisibility(View.GONE);
                }
            }
            
            if(exSize >= 3) {
                h.e3Ll.setVisibility(View.VISIBLE);
                h.e3Tv.setText(UtilFunctions.capitalizeFirstChar(coinsHash.get(coinId).get(2).getEx()));
                float oP = coinsHash.get(coinId).get(2).getOp();
                float cP = coinsHash.get(coinId).get(2).getCp();
                h.e3CpTv.setText(UtilFunctions.formatFloatTo4Decimals(cP));
                float percentageChange = 0.0f;
                if(oP > 0.0f) {
                    percentageChange = ((cP - oP) * 100) / oP;
                }
                h.e3PercentageTv.setText(UtilFunctions.formatFloatTo4DecimalsPercentage(percentageChange) + " %");
                if(percentageChange > 0.0f) {
                    h.e3PercentageTv.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
                    h.e3UpOrDownIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow_drop_up_black_24dp));
                    DrawableCompat.setTint(h.e3UpOrDownIv.getDrawable(), ContextCompat.getColor(getContext(), R.color.green));
                } else if(percentageChange < 0.0f) {
                    h.e3PercentageTv.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                    h.e3UpOrDownIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow_drop_down_black_24dp));
                    DrawableCompat.setTint(h.e3UpOrDownIv.getDrawable(), ContextCompat.getColor(getContext(), R.color.red));
                }
            }

        }




//        h.exLv.setAdapter(new ExchangeItemForWatchlistAdapter(ctx, coinsHash.get(coin.coinId)));

//        String[] idArr = coin.getId().split("__");

//        h.coinPriceTv.setText("INR " + UtilFunctions.formatFloatTo4Decimals(coin.getCurrentPrice()));
//        if(coin.getOpenPrice() > 0.0f) {
//            float percentageChange = ((coin.getCurrentPrice() - coin.getOpenPrice()) * 100) / coin.getOpenPrice();
//            //percentageChange = 2.2f;
//            h.coinPriceChangeTv.setVisibility(View.VISIBLE);
//            h.coinPriceChangeTv.setText(UtilFunctions.formatFloatTo4Decimals(percentageChange) + " %");
//            if(percentageChange != 0.0f) h.upOrDownIv.setVisibility(View.VISIBLE);
//            if(percentageChange > 0.0f) {
//                h.coinPriceChangeTv.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
//                h.upOrDownIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow_drop_up_black_24dp));
//                DrawableCompat.setTint(h.upOrDownIv.getDrawable(), ContextCompat.getColor(getContext(), R.color.green));
//            } else if(percentageChange < 0.0f) {
//                h.coinPriceChangeTv.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
//                h.upOrDownIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow_drop_down_black_24dp));
//                DrawableCompat.setTint(h.upOrDownIv.getDrawable(), ContextCompat.getColor(getContext(), R.color.red));
//            }
//        }

        return v;
    }

    // ViewHolder pattern
    static class CoinHolder {
        ImageView coinImageIv;
        TextView coinNameTv;
        TextView coinIdTv;
//        TextView coinPriceTv;
//        TextView coinPriceChangeTv;
//        ImageView upOrDownIv;
//        ListView exLv;
        RelativeLayout e1Ll;
        TextView e1Tv;
        TextView e1CpTv;
        ImageView e1UpOrDownIv;
        TextView e1PercentageTv;

        RelativeLayout e2Ll;
        TextView e2Tv;
        TextView e2CpTv;
        ImageView e2UpOrDownIv;
        TextView e2PercentageTv;

        RelativeLayout e3Ll;
        TextView e3Tv;
        TextView e3CpTv;
        ImageView e3UpOrDownIv;
        TextView e3PercentageTv;
        
    }



}
