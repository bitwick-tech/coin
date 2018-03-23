package tech.smartcrypto.neeraj.coin.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.internal.Util;
import tech.smartcrypto.neeraj.coin.Alert;
import tech.smartcrypto.neeraj.coin.Coin;
import tech.smartcrypto.neeraj.coin.R;
import utils.DatabaseHandler;
import utils.ServerInteractionHandler;
import utils.UtilFunctions;
import utils.VolleyCallback;

public class AlertDialogFragment extends DialogFragment {
    private View myView;
    private Alert alert;
    private String _coinName;
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
        // Inflate the progress_animation for this fragment
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
                _coinName = DatabaseHandler.getInstance(getActivity()).coinStaticDataDao().getNameById(alert.getCoinId());
                handleCoinBtn();
                inflateViewFromData();
                fetchCoinInfoFromServer();
            }
            else {
                onClickCoinSelect(null);
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
            String coinIdName = data.getStringExtra("coinIdName");
            String coinId = coinIdName.split("__")[0];
            _coinName = coinIdName.split("__")[1];
            if (coinId != null && !coinId.isEmpty()) {
                onCoinSelected(coinId);
            }
        }


        //Toast.makeText(getActivity(),data.getStringExtra("coinId"),Toast.LENGTH_SHORT).show();
    }

    private void onCoinSelected(String coinId) {
        //get coin details from API
        //create alert with empty _id
        String ex = "";
        if(alert != null)
            ex = alert.getEx();
        if(alert == null) alert = new Alert();
        alert.setCoinId(coinId);
        alert.setCurr(UtilFunctions.getDataFromSharedPref(getContext(), "defaultCurrency"));
        alert.setOneTime(true);
        if(!ex.isEmpty()) alert.setEx(ex);
        _coinName = DatabaseHandler.getInstance(getActivity()).coinStaticDataDao().getNameById(alert.getCoinId());
        handleCoinBtn();

        Button exBtn = myView.findViewById(R.id.fragment_dialog_alert_exBtn);
        exBtn.setVisibility(View.VISIBLE);
        exBtn.setOnClickListener(v -> onClickExSelect(v));
        onClickExSelect(null);
    }

    private void onClickExSelect(View view) {
        List<String> exItems = DatabaseHandler.getInstance(getActivity()).coinStaticDataDao().getAllExchangesOfACoinList(alert.getCoinId());
        String[] items = exItems.toArray(new String[exItems.size()]);
        boolean flag = false;
        for(String item: items) {
            if(item.equalsIgnoreCase(alert.getEx())) {
                flag = true;
            }
        }
        if((!flag && items.length > 0 && !alert.getEx().equalsIgnoreCase("international market")) || items.length == 1) {
            alert.setEx(items[0]);
            handleLoadingText();
            fetchCoinInfoFromServer();
            handleExBtn();
            if(!flag && items.length > 1) {
                showExchangeAlert(items);
            }
        }
        else {
            showExchangeAlert(items);
        }

    }

    private void showExchangeAlert(String[] items) {
        for(int i = 0; i < items.length; i++) {
            items[i] = "        " + items[i];
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, items);

        new AlertDialog.Builder(getContext())
                .setTitle("Select an exchange")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i = 0; i < items.length; i++) {
                            items[i] = items[i].substring(8);
                        }

                        alert.setEx(items[which]);

//                        handleExBtn();
                        //show loading bar
                        handleLoadingText();
                        fetchCoinInfoFromServer();
                        dialog.dismiss();
                    }
                }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if(alert != null && alert.getEx() != null && !alert.getEx().equalsIgnoreCase("international market")) {
                    handleLoadingText();
                    fetchCoinInfoFromServer();
                }
            }
        })
                .create().show();
    }



    private void fetchCoinInfoFromServer() {
        String url = getString(R.string.coinPriceAPIURL) + "?q=" + alert.getCoinId() + "__" + alert.getEx() + "__" + alert.getCurr();
        ServerInteractionHandler.getDataFromServer(url, getContext().getApplicationContext(),
                new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(JSONObject result) {
                        try{
                            if(getContext() != null) {
                                inflateViewFromVolleyResponse(result);
                            }
                        }catch (JSONException e) {
                            //do nothing
                        }
                    }
                    @Override
                    public void onErrorResponse(VolleyError result) {
                        //on error response
                    }
                });
    }

    private void inflateViewFromVolleyResponse(JSONObject result) throws JSONException {
        if(alert == null) return;
        JSONObject jsonObject = result.optJSONObject("coinData");
//        alert.setCurr(coinJson.optString("currency"));
        alert.setCp(Float.parseFloat(jsonObject.optJSONObject(alert.getCoinId()).optJSONObject(alert.getEx()).optJSONObject(alert.getCurr()).optString("cp")));
//        alert.setCoinId(coinJson.optString("id"));
        TextView tv = myView.findViewById(R.id.fragment_dialog_alert_currentPriceTxt);
        tv.setVisibility(View.INVISIBLE); //hide loading bar
        tv.setTextColor(getResources().getColor(R.color.white));
        inflateViewFromData();
    }


    private void inflateViewFromData() {
        if(myView != null){
            handleExBtn();
            handleCurrentPriceTv();
            handleGoesUp();
            handleGoesBelow();
            handleOneTime();
            handleSaveBtn();
            handleDeleteBtn();
        }
    }

    private void handleLoadingText() {
        TextView tv = myView.findViewById(R.id.fragment_dialog_alert_currentPriceTxt);
        tv.setVisibility(View.VISIBLE);
        tv.setText("Loading...");
        tv.setTextColor(getResources().getColor(R.color.green));
    }

    private void handleExBtn() {
        Button exBtn = myView.findViewById(R.id.fragment_dialog_alert_exBtn);
        exBtn.setVisibility(View.VISIBLE);
        exBtn.setOnClickListener(v -> onClickExSelect(v));
//        int leftDrawable = UtilFunctions.getResourseId(getContext(), coin.getId(), "drawable", getContext().getPackageName());
//        if(leftDrawable != -1) {
//            coinBtn.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, 0, 0, 0);
//        }
        ImageView exIv = (ImageView) myView.findViewById(R.id.fragment_dialog_alert_exIv);
        exIv.setVisibility(View.VISIBLE);
        Picasso.get().load(getResources().getString(R.string.staticImgURL) + alert.getEx() + ".png").into(exIv);
        exBtn.setText(alert.getEx());
    }

    private void handleCoinBtn() {
        Button coinBtn = myView.findViewById(R.id.fragment_dialog_alert_coinBtn);
//        int leftDrawable = UtilFunctions.getResourseId(getContext(), coin.getId(), "drawable", getContext().getPackageName());
//        if(leftDrawable != -1) {
//            coinBtn.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, 0, 0, 0);
//        }
        Picasso.get().load(getResources().getString(R.string.staticImgURL) + alert.getCoinId() + ".png").into((ImageView) myView.findViewById(R.id.fragment_dialog_alert_coinIv));
        coinBtn.setText(_coinName);
    }

    private void handleCurrentPriceTv() {
        TextView tv = myView.findViewById(R.id.fragment_dialog_alert_currentPriceTxt);
        tv.setVisibility(View.VISIBLE);
        tv.setText("Alert me when\n current price \n " + UtilFunctions.formatFloatTo4Decimals(alert.getCp()) + "  " + alert.getCurr().toUpperCase());
    }

    private void handleSaveBtn() {
        Button saveBtn = myView.findViewById(R.id.fragment_dialog_alert_saveAlertBtn);
        saveBtn.setVisibility(View.VISIBLE);
        saveBtn.setOnClickListener(view -> {
            onSaveButtonClicked();
        });
    }

    private void handleDeleteBtn() {
        Button saveBtn = myView.findViewById(R.id.fragment_dialog_alert_delAlertBtn);
        saveBtn.setVisibility(View.VISIBLE);
        saveBtn.setOnClickListener(view -> {
            onDeleteButtonClicked();
        });
    }

    private void handleGoesUp() {
        TextView tv = myView.findViewById(R.id.fragment_dialog_alert_goesAboveTxt);
        tv.setVisibility(View.VISIBLE);
        tv.setText(getString(R.string.goes_above));

        EditText et = myView.findViewById(R.id.fragment_dialog_alert_abovePriceEdtTxt);
        et.setVisibility(View.VISIBLE);
        float cp = alert.getHp();
        et.setText(UtilFunctions.formatFloatTo4Decimals(cp));
        et.setText(UtilFunctions.formatFloatTo4Decimals(alert.getCp() * 1.1f));

    }

    private void handleGoesBelow() {
        TextView tv = myView.findViewById(R.id.fragment_dialog_alert_goesBelowTxt);
        tv.setVisibility(View.VISIBLE);
        tv.setText(getString(R.string.goes_below));

        EditText et = myView.findViewById(R.id.fragment_dialog_alert_goesBelowEdtTxt);
        et.setVisibility(View.VISIBLE);
        float cp = alert.getLp();
        et.setText(UtilFunctions.formatFloatTo4Decimals(cp));
        et.setText(UtilFunctions.formatFloatTo4Decimals(alert.getCp() * 0.9f));
    }

    private void handleOneTime() {
        TextView tv = myView.findViewById(R.id.fragment_dialog_alert_alertMeTxt);
        tv.setVisibility(View.VISIBLE);

        RadioGroup radioGroup = myView.findViewById(R.id.fragment_dialog_alert_oneTimeRadioGrp);
        radioGroup.setVisibility(View.VISIBLE);
        if(!alert.isOneTime())
            radioGroup.check(R.id.fragment_dialog_alert_contRadioBtn);

    }

    private void onSaveButtonClicked(){
        //handle alert prices
        //for goes above
        EditText goesAbove = myView.findViewById(R.id.fragment_dialog_alert_abovePriceEdtTxt);
        float goesAbovePrice = UtilFunctions.deformatStringToFloat(goesAbove.getText().toString());
        if(goesAbovePrice >= 0.0f)
            alert.setHp(goesAbovePrice);

        //for goes below
        goesAbove = myView.findViewById(R.id.fragment_dialog_alert_goesBelowEdtTxt);
        goesAbovePrice = UtilFunctions.deformatStringToFloat(goesAbove.getText().toString());
        if(goesAbovePrice >= 0.0f)
            alert.setLp(goesAbovePrice);


        //handle Radio Button data onetime/cont
        RadioGroup radioGroup = myView.findViewById(R.id.fragment_dialog_alert_oneTimeRadioGrp);
        int index = radioGroup.indexOfChild(myView.findViewById(radioGroup.getCheckedRadioButtonId()));
        if(index == 1) {
            alert.setOneTime(false);
        }

        //reset the flags min and max
        alert.setSh(true);
        alert.setSl(true);

        if(alert.getId() <= 0) {
            UtilFunctions.addAlert(alert, getContext());
        }
        else{
            UtilFunctions.updateAlert(alert, getContext());
        }
        this.dismiss();
    }

    private void onDeleteButtonClicked() {
        UtilFunctions.deleteAlert(alert, getContext());
        this.dismiss();
    }
}
