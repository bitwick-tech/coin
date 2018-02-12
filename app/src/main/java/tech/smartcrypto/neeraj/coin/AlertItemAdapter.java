package tech.smartcrypto.neeraj.coin;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.Joiner;

import java.util.List;

import utils.UtilFunctions;

/**
 * Created by neeraj on 28/1/18.
 */

public class AlertItemAdapter extends ArrayAdapter<Alert> {

    private Context ctx;
    private List<Alert> alertList;
    public AlertItemAdapter(Context context, List<Alert> alertList) {
        super(context, R.layout.alert_layout_user_list_row, alertList);
        this.ctx = context;
        this.alertList = alertList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        AlertHolder h = null;
        if (v == null) {
            // Inflate row layout
            LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.alert_layout_user_list_row, parent, false);
            // Look for Views in the layout


            TextView alertIdTv = v.findViewById(R.id.alert_layout_user_list_alertId); //always hidden... only to fetch id to access alert from database
            ImageView coinIv = v.findViewById(R.id.alert_layout_user_list_coinIV);
            TextView coinIdTv = v.findViewById(R.id.alert_layout_user_list_coinIdTv);
            //TextView currencyTv = v.findViewById(R.id.alert_layout_user_list_currencyTv);
            ImageView upIv = v.findViewById(R.id.alert_layout_user_list_upIv);
            ImageView downIv = v.findViewById(R.id.alert_layout_user_list_downIv);
            TextView cpTv = v.findViewById(R.id.alert_layout_user_list_cpTv);
            ImageView upWithPriceIv = v.findViewById(R.id.alert_layout_user_list_upWithPriceIv);
            ImageView downWithPriceIv = v.findViewById(R.id.alert_layout_user_list_downWithPriceIv);
            TextView hpTv = v.findViewById(R.id.alert_layout_user_list_hpTv);
            TextView lpTv = v.findViewById(R.id.alert_layout_user_list_lpTv);


            h = new AlertHolder();
            h.alertIdTv = alertIdTv; //always hidden... only to fetch id to access alert from database
            h.coinIv = coinIv ;
            h.coinIdTv = coinIdTv;
            //h.currencyTv = currencyTv;
            h.upIv = upIv;
            h.downIv = downIv;
            h.cpTv = cpTv;
            h.upWithPriceIv = upWithPriceIv;
            h.downWithPriceIv = downWithPriceIv;
            h.hpTv = hpTv;
            h.lpTv = lpTv;

            v.setTag(h);
        }
        else
            h = (AlertHolder) v.getTag();

        Alert alert = alertList.get(position);

        h.alertIdTv.setText(Integer.toString(alert.getId()));
        int imgId = UtilFunctions.getResourseId(getContext(), alert.getCoinId(), "drawable", getContext().getPackageName());
        if(imgId >= 0) h.coinIv.setImageResource(imgId);
        String[] coinName = alert.getCoinId().split("__");
        coinName[0] = coinName[0].toUpperCase();
        h.coinIdTv.setText(Joiner.on(" ").join(coinName));
        //h.currencyTv.setText(alert.getCurrency());
        if(alert.getHighPrice() <= 0.0f) {
            h.hpTv.setVisibility(View.INVISIBLE);
            h.upIv.setVisibility(View.INVISIBLE);
        }
        if(alert.getLowPrice() <= 0.0f) {
            h.lpTv.setVisibility(View.INVISIBLE);
            h.downIv.setVisibility(View.INVISIBLE);
        }
        //h.downIv.setImageResource();
        h.cpTv.setText(" INR " + UtilFunctions.formatFloatTo4Decimals(alert.getCurrentPrice()));
        if(alert.getLowPrice() > 0.0f && alert.getLowPrice() > alert.getCurrentPrice()) h.cpTv.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
        else if(alert.getHighPrice() > 0.0f && alert.getHighPrice() < alert.getCurrentPrice()) h.cpTv.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
        else h.cpTv.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        //h.upWithPriceIv.setImageResource();
        //h.downWithPriceIv.setImageResource();
        h.hpTv.setText(UtilFunctions.formatFloatTo4Decimals(alert.getHighPrice()));
        h.lpTv.setText(UtilFunctions.formatFloatTo4Decimals(alert.getLowPrice()));

        return v;
    }

    // ViewHolder pattern
    static class AlertHolder {
        TextView alertIdTv; //always hidden... only to fetch id to access alert from database
        ImageView coinIv;
        TextView coinIdTv;
        TextView currencyTv;
        ImageView upIv;
        ImageView downIv;
        TextView cpTv;
        ImageView upWithPriceIv;
        ImageView downWithPriceIv;
        TextView hpTv;
        TextView lpTv;
    }

}
