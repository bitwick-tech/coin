package tech.smartcrypto.neeraj.coin.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import tech.smartcrypto.neeraj.coin.Coin;
import tech.smartcrypto.neeraj.coin.CoinDao;
import tech.smartcrypto.neeraj.coin.CoinItemForAllCoinsAlertAdapter;
import tech.smartcrypto.neeraj.coin.CoinStaticData;
import tech.smartcrypto.neeraj.coin.CoinStaticDataDao;
import tech.smartcrypto.neeraj.coin.R;
import utils.DatabaseHandler;
import utils.UtilFunctions;

public class CoinListFragment extends DialogFragment {

    public CoinListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        List<CoinStaticDataDao.CoinIdAndName> coins;
        if(CoinListFragment.this.getTargetRequestCode() == WatchlistFragment.REQUEST_CODE_FOR_LIST_COIN_FRAGMENT) {
            coins = UtilFunctions.removeAlreadyAddedCoins(getActivity());
        }
        else {
            coins = DatabaseHandler.getInstance(getActivity()).coinStaticDataDao().getAllCoinIdAndNameList();
        }

        ListAdapter adapter = new CoinItemForAllCoinsAlertAdapter(getContext(), coins);
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Pick a coin");
        alert.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        if (which >= 0) {
                            Intent intent = new Intent();
                            intent.putExtra("coinIdName", coins.get(which).coinId+"__"+coins.get(which).coinName);
                            CoinListFragment.this.getTargetFragment().onActivityResult(CoinListFragment.this.getTargetRequestCode(), 1, intent);
                        }
                    }
        });

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog_list_coin, null);
        alert.setView(view);

        return alert.create();
    }

}
