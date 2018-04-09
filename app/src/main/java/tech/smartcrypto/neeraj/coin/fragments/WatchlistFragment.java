package tech.smartcrypto.neeraj.coin.fragments;

import android.arch.lifecycle.LiveData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tech.smartcrypto.neeraj.coin.Coin;
import tech.smartcrypto.neeraj.coin.CoinItemAdapter;
import tech.smartcrypto.neeraj.coin.CoinStaticDataDao;
import utils.DatabaseHandler;
import tech.smartcrypto.neeraj.coin.R;
import utils.UtilFunctions;

public class WatchlistFragment extends Fragment {
    public static  int updateInterval = 20;
    public static final int REQUEST_CODE_FOR_LIST_COIN_FRAGMENT = 1;
    SwipeRefreshLayout mSwipeRefreshLayout;
    final Handler handler = new Handler();
    public WatchlistFragment() {
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
        View view = inflater.inflate(R.layout.fragment_watchlist, container, false);


//        AdView mAdView = view.findViewById(R.id.fragment_watchlist_adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);


        TextView addCoinTv = view.findViewById(R.id.fragment_watchlist_addCoinMsgTv);
        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        SwipeRefreshLayout.OnRefreshListener swipeRefreshListner = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                UtilFunctions.updateWatchlistCoinData(getContext());
            }
        };
        mSwipeRefreshLayout.setOnRefreshListener(swipeRefreshListner);

        Runnable refresh = new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
//                        mSwipeRefreshLayout.setRefreshing(true);
                        swipeRefreshListner.onRefresh();
                        handler.postDelayed(this, updateInterval * 1000);
                    }
                });
            }
        };
        handler.postDelayed(refresh, 5 * 1000);

        // Followed coins list
        LiveData<List<Coin>> coinList = DatabaseHandler.getInstance(getActivity()).coinDao().getAll();
        ListView coinListView = view.findViewById(R.id.coinList);
        coinList.observe(this, (List<Coin> coins) -> {
            if(coins != null && coins.size() > 0) {
                addCoinTv.setVisibility(View.GONE);
                Map<String, List<Coin>> coinsHash = UtilFunctions.getCoinsHashFromList(coins);
                List<String> coinIdList = new ArrayList<>(coinsHash.keySet());
                List<CoinStaticDataDao.CoinIdAndName> coinStaticDataList= DatabaseHandler.getInstance(getActivity()).coinStaticDataDao().getCoinIdAndNameListByIds(coinIdList.toArray(new String[coinsHash.size()]));
                Map<String, String> coinIdNameHash = UtilFunctions.getIdNameHash(coinStaticDataList);

                coinListView.setAdapter(new CoinItemAdapter(getActivity(), coinIdNameHash, coinsHash, coinIdList));
                coinListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                        String _id = coinIdList.get(position);
                        showCoinDetailFragment(_id);
                    }
                });
                coinListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View v, int position, long id) {
                        String _id = coinIdList.get(position);
                        handleLongClickOnCoinItem(_id);
                        return true;
                    }
                });
            }
            else {
                coinListView.setAdapter(new CoinItemAdapter(getActivity(), null, null, new ArrayList<>()));
                addCoinTv.setVisibility(View.VISIBLE);
            }
            if(mSwipeRefreshLayout.isRefreshing()) mSwipeRefreshLayout.setRefreshing(false);
        });


        //add coin button
        Button button = view.findViewById(R.id.button_add_coin);
        button.setOnClickListener(v -> addCoin(v));

        return view;
    }

    /** called when add coin button is clicked */
    public void addCoin(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        DialogFragment newFragment = new CoinListFragment();
        newFragment.setTargetFragment(this, REQUEST_CODE_FOR_LIST_COIN_FRAGMENT);
        newFragment.show(fragmentManager, "list_all_coins");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_FOR_LIST_COIN_FRAGMENT) {
            super.onActivityResult(requestCode, resultCode, data);
            String myData = data.getStringExtra("coinIdName");
            if(myData != null && !myData.isEmpty()) {
                String coinId = myData.split("__")[0];
                if(coinId == null || coinId.isEmpty()) return;
                showCoinDetailFragment(coinId);
            }
        }
    }

    public void showCoinDetailFragment(String coinId) {
        if(coinId == null || coinId.isEmpty()) return;
        Bundle bundle = new Bundle();
        bundle.putString("coinId", coinId);
        CoinDetailFragment coinDetailFragment= new CoinDetailFragment();
        coinDetailFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .hide(this)
                .replace(R.id.activity_main, coinDetailFragment,"coinDetailFragment")
                .addToBackStack(null)
                .commit();

    }

    private void handleLongClickOnCoinItem(String coinId) {
        new AlertDialog.Builder(getContext())
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete this coin from Watchlist?")
                .setIcon(android.R.drawable.ic_menu_delete)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        UtilFunctions.deleteCoinById(getActivity(), coinId);
                        dialog.dismiss();
                    }

                })



                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create().show();

    }

}