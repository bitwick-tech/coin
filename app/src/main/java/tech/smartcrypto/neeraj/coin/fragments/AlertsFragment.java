package tech.smartcrypto.neeraj.coin.fragments;

import android.arch.lifecycle.LiveData;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import tech.smartcrypto.neeraj.coin.Alert;
import tech.smartcrypto.neeraj.coin.AlertItemAdapter;
import utils.DatabaseHandler;
import tech.smartcrypto.neeraj.coin.R;


public class AlertsFragment extends Fragment{

    private static final String TAG_ALERT_DAILOG_FRAGMENT = "";
    public AlertsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_alerts, container, false);

        // Alerts list
        LiveData<List<Alert>> alertList = DatabaseHandler.getInstance(getActivity()).alertDao().getAll();
        ListView alertListView = view.findViewById(R.id.alertList);
        alertList.observe(this, (List<Alert> alerts) -> {
                alertListView.setAdapter(new AlertItemAdapter(getActivity(), alerts));
                alertListView.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                        int _id = alerts.get(position).getId();
                        showAlertDetailFragment(_id);
                    }
                });
        });
        // "add alert" button
        Button button = view.findViewById(R.id.button_add_alert);
        button.setOnClickListener(v -> addAlert(v));

        return view;
    }

    public void showAlertDetailFragment(int id) {
        Bundle bundle = new Bundle();
        if(id > 0) bundle.putInt("_id", id);
        AlertDialogFragment fragment = new AlertDialogFragment();
        fragment.setArguments(bundle);
        fragment.show(getFragmentManager(), TAG_ALERT_DAILOG_FRAGMENT);
    }

    /** called when add alert button is clicked */
    public void addAlert(View view) {
        showAlertDetailFragment(-1);
    }


}