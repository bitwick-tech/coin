package tech.smartcrypto.neeraj.coin.fragments;

import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import java.util.List;

import tech.smartcrypto.neeraj.coin.Coin;
import tech.smartcrypto.neeraj.coin.CoinItemAdapter;
import utils.DatabaseHandler;
import tech.smartcrypto.neeraj.coin.R;
import utils.UtilFunctions;

import static utils.UtilFunctions.addCoinToDb;


public class WatchlistFragment extends Fragment {

    private static final int REQUEST_CODE_FOR_LIST_COIN_FRAGMENT = 1;
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_watchlist, container, false);

        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        UtilFunctions.updateWatchlistCoinData(getContext());
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

        Runnable refresh = new Runnable() {
            @Override
            public void run() {
                UtilFunctions.updateWatchlistCoinData(getContext());
                mSwipeRefreshLayout.setRefreshing(true);
                mSwipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        handler.postDelayed(this, 60 * 1000);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        };

        handler.postDelayed(refresh, 60 * 1000);

        UtilFunctions.updateWatchlistCoinData(getContext());
        // Followed coins list
        LiveData<List<Coin>> coinList = DatabaseHandler.getInstance(getActivity()).coinDao().getAll();
        ListView coinListView = view.findViewById(R.id.coinList);
        coinList.observe(this, (List<Coin> coins) -> {
                coinListView.setAdapter(new CoinItemAdapter(getActivity(), coins));
                mSwipeRefreshLayout.setRefreshing(false);
        });
        if(coinList == null || coinList.getValue() == null || coinList.getValue().size() == 0) mSwipeRefreshLayout.setRefreshing(false);

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
            //do what ever you want here, and get the result from intent like below
            String myData = data.getStringExtra("coinId");
            // TODO get data from server and store updated values to DB
            if(myData != null && !myData.isEmpty()) {
                Coin coin = new Coin();
                coin.setId(myData);
                addCoinToDb(coin, getContext());
                //Toast.makeText(getActivity(), data.getStringExtra("coinId"), Toast.LENGTH_SHORT).show();
                //UtilFunctions.updateWatchlistCoinData(getContext());
            }
        }
    }



}