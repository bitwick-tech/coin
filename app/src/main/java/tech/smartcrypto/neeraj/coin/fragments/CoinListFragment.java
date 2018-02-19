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
import tech.smartcrypto.neeraj.coin.CoinItemForAllCoinsAlertAdapter;
import tech.smartcrypto.neeraj.coin.R;

public class CoinListFragment extends DialogFragment {

    //String colors[] = new String[] {"BTC--ZEBPAY", "BTC--UNOCOIN", "BTC--KOINEX",
      //      "XRP--KOINEX", "BCH--KOINEX", "ETH--KOINEX", "LTC--KOINEX", "OMG--KOINEX", "MIOTA--KOINEX"};
    public CoinListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_dialog_list_coin, container, false);
//    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        List<String> allCoins = new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.all_coins)));
        ListAdapter adapter = new CoinItemForAllCoinsAlertAdapter(getContext(), allCoins);
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Pick a coin");
        alert.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        if (which >= 0) {
                            Intent intent = new Intent();
                            intent.putExtra("coinId", allCoins.get(which));// [which]);
                            CoinListFragment.this.getTargetFragment().onActivityResult(CoinListFragment.this.getTargetRequestCode(), 1, intent);
                        }
                    }
        });

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog_list_coin, null);
        alert.setView(view);

        return alert.create();
    }

}
