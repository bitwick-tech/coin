package tech.smartcrypto.neeraj.coin.fragments;

import android.arch.lifecycle.LiveData;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.internal.Util;
import tech.smartcrypto.neeraj.coin.Coin;
import tech.smartcrypto.neeraj.coin.ExchangeItemForCoinDetailAdapter;
import tech.smartcrypto.neeraj.coin.R;
import utils.DatabaseHandler;
import utils.MultiSpinner;
import utils.UtilFunctions;

/**
 * Created by neerajlajpal on 26/02/18.
 */

public class CoinDetailFragment extends Fragment {

    private String _coinId;
    private List<String> items;
//    private List<CoinDynamicUserData> selectedItems;
    private HashMap<String, Coin> selectedItemsHash;
    private String defaultCurrency;
    private boolean[] newSelectedEx;
    public CoinDetailFragment() {} // Required empty public constructor

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the progress_animation for this fragment
        View view = inflater.inflate(R.layout.fragment_coin_detail, container, false);


        AdView mAdView = view.findViewById(R.id.fragment_coin_detail_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String coinId = bundle.getString("coinId", "");
            if(coinId != null && !coinId.isEmpty()) {
                _coinId = coinId;

                defaultCurrency = UtilFunctions.getDataFromSharedPref(getActivity(),"defaultCurrency");
                ImageView backButtonIv = view.findViewById(R.id.fragment_coin_detail_backButtonIv);
                backButtonIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow_back_black_24dp));
                DrawableCompat.setTint(backButtonIv.getDrawable(), ContextCompat.getColor(getContext(), R.color.white));
                backButtonIv.setOnClickListener(v -> {
                    getActivity().onBackPressed();
                });

                ImageView coinImgIv = view.findViewById(R.id.fragment_coin_detail_coinImageTv);
                Picasso.get().load(getResources().getString(R.string.staticImgURL) + _coinId + ".png").into(coinImgIv);

                TextView coinNameTv = view.findViewById(R.id.fragment_coin_detail_coinNameTv);
                coinNameTv.setText(_coinId.toUpperCase());

                MultiSpinner multiSpinner = view.findViewById(R.id.fragment_coin_detail_exSpinner);

                LiveData<List<Coin>> coinList = DatabaseHandler.getInstance(getActivity()).coinDao().getAllByCoinId(_coinId);
                ImageView selectExchangeIv = view.findViewById(R.id.fragment_coin_detail_selectExchangeIv);
                if(coinList == null || coinList.getValue() == null || coinList.getValue().size() == 0) {
                    String showExchangeIv = UtilFunctions.getDataFromSharedPref(getContext(), "s_e_iv");
                    if(showExchangeIv == null) {
                        selectExchangeIv.setVisibility(View.VISIBLE);
                        Map<String, String> tmp = new HashMap<>();
                        tmp.put("s_e_iv", "1");
                        UtilFunctions.addDataToSharedPref(tmp, getContext());
                    }

                }
                ListView exListView = view.findViewById(R.id.fragment_coin_detail_exchangeList);
                coinList.observe(this, (List<Coin> coins) -> {
                    selectedItemsHash = UtilFunctions.convertListToHash(coins);
                    if(selectedItemsHash != null && selectedItemsHash.size() > 0 ) {
                        selectExchangeIv.setVisibility(View.INVISIBLE);
                    }
                    exListView.setAdapter(new ExchangeItemForCoinDetailAdapter(getActivity(), coins));
                    items = DatabaseHandler.getInstance(getActivity()).coinStaticDataDao().getAllExchangesOfACoinList(_coinId);
                    multiSpinner.setItems(items, selectedItemsHash, new MultiSpinner.MultiSpinnerListener() {
                        @Override
                        public void onItemsSelected(boolean[] selected) {
                            newSelectedEx = selected;
                            handleSelectedItems();
                        }
                    });
                });
            }
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    private void handleSelectedItems() {
        ArrayList<String> newEx = new ArrayList<>();
        ArrayList<Coin> deletedEx = new ArrayList<>();
        for(int i = 0; i < newSelectedEx.length; i++) {
            if(newSelectedEx[i] && (selectedItemsHash == null || selectedItemsHash.get(items.get(i)) == null)) {
                newEx.add(items.get(i));
                continue;
            }
            if(!newSelectedEx[i] && selectedItemsHash != null && selectedItemsHash.get(items.get(i)) != null) {
                deletedEx.add(selectedItemsHash.get(items.get(i)));
            }
        }

        Runnable task = () -> {
            if(newEx != null && newEx.size() > 0) {
                Coin[] newCoinsArray = new Coin[newEx.size()];
                Coin coin;
                for (int i = 0; i < newEx.size(); i++) {
                    coin = new Coin();
                    coin.setCId(_coinId);
                    coin.setEx(newEx.get(i));
                    coin.setCurr(defaultCurrency);
                    newCoinsArray[i] = coin;
                }
                DatabaseHandler.getInstance(getActivity()).coinDao().insertAll(newCoinsArray);
            }

            if(deletedEx != null && deletedEx.size() > 0) {
                DatabaseHandler.getInstance(getActivity()).coinDao().deleteMany(deletedEx.toArray(new Coin[deletedEx.size()]));
            }

            UtilFunctions.updateWatchlistCoinData(getContext(), _coinId);
        };
        Thread thread = new Thread(task);
        thread.start();
    }

}
