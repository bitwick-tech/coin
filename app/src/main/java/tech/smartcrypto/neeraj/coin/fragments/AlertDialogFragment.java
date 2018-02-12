package tech.smartcrypto.neeraj.coin.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tech.smartcrypto.neeraj.coin.Alert;
import tech.smartcrypto.neeraj.coin.Coin;
import tech.smartcrypto.neeraj.coin.R;
import utils.ServerInteractionHandler;
import utils.UtilFunctions;

public class AlertDialogFragment extends DialogFragment {
    private View myView;
    private Alert alert = new Alert();
    private static final int REQUEST_CODE_FOR_LIST_COIN_FRAGMENT = 2;
    public AlertDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dialog_alert, container, false);
        myView = view;


        Button coinBtn = view.findViewById(R.id.fragment_dialog_alert_coinBtn);
        coinBtn.setOnClickListener(v -> onClickCoinSelect(v));

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int id = bundle.getInt("_id", -1);
            if(id > 0){
                alert =  UtilFunctions.getAlertFromDB(id, getContext());
                Coin coin = new Coin();
                coin.setId(alert.getCoinId());
                coin.setName(alert.getCoinName());
                coin.setCurrentPrice(alert.getCurrentPrice());
                coin.setCurrency(alert.getCurrency());
                inflateViewFromData(coin);
                fetchCoinInfoFromServer(coin);
            }
        }
    }

    public void onClickCoinSelect(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        DialogFragment newFragment = new CoinListFragment();
        //FragmentTransaction transaction = fragmentManager.beginTransaction();
        newFragment.setTargetFragment(this, REQUEST_CODE_FOR_LIST_COIN_FRAGMENT);
        newFragment.show(fragmentManager, "list_all_coins");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == REQUEST_CODE_FOR_LIST_COIN_FRAGMENT) {
            super.onActivityResult(requestCode, resultCode, data);
            //do what ever you want here, and get the result from intent like below
            String coinId = data.getStringExtra("coinId");
            if (coinId != null && !coinId.isEmpty()) {
                onCoinSelected(coinId);
            }
        }


        //Toast.makeText(getActivity(),data.getStringExtra("coinId"),Toast.LENGTH_SHORT).show();
    }

    private void onCoinSelected(String coinId) {
        //get coin details from API
        //create alert with empty _id
        alert.setCoinId(coinId);
        alert.setOneTime(true);
        Coin coin = new Coin();
        coin.setId(coinId);
        //show loading bar
        TextView tv = myView.findViewById(R.id.fragment_dialog_alert_currentPriceTxt);
        tv.setVisibility(View.VISIBLE);
        tv.setText("Loading...");
        tv.setTextColor(getResources().getColor(R.color.green));
        fetchCoinInfoFromServer(coin);
    }

    private void fetchCoinInfoFromServer(Coin coin) {
        String url = getString(R.string.coinApiUrl) + "?q=" + coin.getId();
        ServerInteractionHandler.getResponseFromServer(Request.Method.GET, url, null,
                new ServerInteractionHandler.VolleyCallback() {
                    @Override
                    public void onSuccessResponse(JSONObject result) {
                        // do your work with response object

                        try{
                            Log.d("callback", result.toString());
                            if(getContext() != null) {
                                JSONArray coinJsonArray = result.optJSONArray("coinData");
                                JSONObject coinJson = coinJsonArray.getJSONObject(0);
                                coin.setCurrency(coinJson.optString("currency"));
                                coin.setCurrentPrice(Float.parseFloat(coinJson.optString("cp")));
                                coin.setName(coinJson.optString("name"));
                                alert.setCoinId(coin.getId());
                                alert.setCoinName(coin.getName());
                                alert.setCurrency(coin.getCurrency());
                                alert.setCurrentPrice(coin.getCurrentPrice());
                                TextView tv = myView.findViewById(R.id.fragment_dialog_alert_currentPriceTxt);
                                tv.setVisibility(View.INVISIBLE); //hide loading bar
                                tv.setTextColor(getResources().getColor(R.color.white));
                                inflateViewFromData(coin);
                            }
                        }catch (JSONException e) {
                            //do nothing
                        }

                    }
                }, getContext().getApplicationContext());
    }


    private void inflateViewFromData(Coin coin) {
        if(myView != null){
            handleCoinBtn(coin);
            handleCurrentPriceTv(coin);
            handleGoesUp(coin);
            handleGoesBelow(coin);
            handleOneTime(coin);
            handleSaveBtn();
            handleDeleteBtn();
        }
    }


    private void handleCoinBtn(Coin coin) {
        Button coinBtn = myView.findViewById(R.id.fragment_dialog_alert_coinBtn);
        int leftDrawable = UtilFunctions.getResourseId(getContext(), coin.getId(), "drawable", getContext().getPackageName());
        if(leftDrawable != -1) {
            coinBtn.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, 0, 0, 0);
        }
        coinBtn.setText(coin.getName());
    }

    private void handleCurrentPriceTv(Coin coin) {
        TextView tv = myView.findViewById(R.id.fragment_dialog_alert_currentPriceTxt);
        tv.setVisibility(View.VISIBLE);
        tv.setText("Alert me when\n current price \n " + UtilFunctions.formatFloatTo4Decimals(coin.getCurrentPrice()) + "  " + coin.getCurrency());
    }

    private void handleSaveBtn() {
        Button saveBtn = myView.findViewById(R.id.fragment_dialog_alert_saveAlertBtn);
        saveBtn.setVisibility(View.VISIBLE);
        saveBtn.setOnClickListener(view -> {
            onSaveButtonClicked(alert);
        });
    }

    private void handleDeleteBtn() {
        Button saveBtn = myView.findViewById(R.id.fragment_dialog_alert_delAlertBtn);
        saveBtn.setVisibility(View.VISIBLE);
        saveBtn.setOnClickListener(view -> {
            onDeleteButtonClicked(alert);
        });
    }

    private void handleGoesUp(Coin coin) {
        TextView tv = myView.findViewById(R.id.fragment_dialog_alert_goesAboveTxt);
        tv.setVisibility(View.VISIBLE);
        tv.setText(getString(R.string.goes_above));

        EditText et = myView.findViewById(R.id.fragment_dialog_alert_abovePriceEdtTxt);
        et.setVisibility(View.VISIBLE);
        float cp = alert.getHighPrice();
        et.setText(UtilFunctions.formatFloatTo4Decimals(cp));
        et.setText(UtilFunctions.formatFloatTo4Decimals(alert.getCurrentPrice() * 1.1f));

    }

    private void handleGoesBelow(Coin coin) {
        TextView tv = myView.findViewById(R.id.fragment_dialog_alert_goesBelowTxt);
        tv.setVisibility(View.VISIBLE);
        tv.setText(getString(R.string.goes_below));

        EditText et = myView.findViewById(R.id.fragment_dialog_alert_goesBelowEdtTxt);
        et.setVisibility(View.VISIBLE);
        float cp = alert.getLowPrice();
        et.setText(UtilFunctions.formatFloatTo4Decimals(cp));
        et.setText(UtilFunctions.formatFloatTo4Decimals(alert.getCurrentPrice() * 0.9f));
    }

    private void handleOneTime(Coin coin) {
        TextView tv = myView.findViewById(R.id.fragment_dialog_alert_alertMeTxt);
        tv.setVisibility(View.VISIBLE);

        RadioGroup radioGroup = myView.findViewById(R.id.fragment_dialog_alert_oneTimeRadioGrp);
        radioGroup.setVisibility(View.VISIBLE);
        if(!alert.isOneTime())
            radioGroup.check(R.id.fragment_dialog_alert_contRadioBtn);

    }

    private void onSaveButtonClicked(Alert alert){
        //handle alert prices
        //for goes above
        EditText goesAbove = myView.findViewById(R.id.fragment_dialog_alert_abovePriceEdtTxt);
        float goesAbovePrice = UtilFunctions.deformatStringToFloat(goesAbove.getText().toString());
        if(goesAbovePrice >= 0.0f)
            alert.setHighPrice(goesAbovePrice);

        //for goes below
        goesAbove = myView.findViewById(R.id.fragment_dialog_alert_goesBelowEdtTxt);
        goesAbovePrice = UtilFunctions.deformatStringToFloat(goesAbove.getText().toString());
        if(goesAbovePrice >= 0.0f)
            alert.setLowPrice(goesAbovePrice);


        //handle Radio Button data onetime/cont
        RadioGroup radioGroup = myView.findViewById(R.id.fragment_dialog_alert_oneTimeRadioGrp);
        int index = radioGroup.indexOfChild(myView.findViewById(radioGroup.getCheckedRadioButtonId()));
        if(index == 1) {
            alert.setOneTime(false);
        }

        //reset the flags min and max
        alert.setStatusMax(true);
        alert.setStatusMin(true);

        if(alert.getId() <= 0) {
            UtilFunctions.addAlert(alert, getContext());
        }
        else{
            UtilFunctions.updateAlert(alert, getContext());
        }
        this.dismiss();
    }

    private void onDeleteButtonClicked(Alert alert) {
        UtilFunctions.deleteAlert(alert, getContext());
        this.dismiss();
    }
}
