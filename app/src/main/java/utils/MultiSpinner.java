package utils;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.content.DialogInterface;
import android.widget.SpinnerAdapter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import tech.smartcrypto.neeraj.coin.Coin;
import tech.smartcrypto.neeraj.coin.CoinDynamicUserData;
import tech.smartcrypto.neeraj.coin.R;

/**
 * Created by neerajlajpal on 26/02/18.
 */

public class MultiSpinner extends AppCompatSpinner implements
        DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnCancelListener {

    private List<String> items;
    private List<CoinDynamicUserData> selectedItems;
    private HashMap<String, Coin> coins;
    private String defaultText = "Exchanges";
    private boolean[] selected;
    private boolean[] tmpSelected;
    private MultiSpinnerListener listener;

    public MultiSpinner(Context context) {
        super(context);
    }

    public MultiSpinner(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    public MultiSpinner(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (isChecked)
            selected[which] = true;
        else
            selected[which] = false;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        setSelectedItems(); //reset in this case
    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Exchanges");
        builder.setMultiChoiceItems(
                items.toArray(new CharSequence[items.size()]), selected, this);
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.save,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onOkClick();
//                        dialog.cancel();
                    }
                })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    dialog.cancel();
                }
            });

        builder.setOnCancelListener(this);
        builder.show();
        return true;
    }

    private void onOkClick() {
//        StringBuffer spinnerBuffer = new StringBuffer();
//        boolean someUnselected = false;
//        for (int i = 0; i < items.size(); i++) {
//            if (selected[i] == true) {
////                spinnerBuffer.append(items.get(i));
////                spinnerBuffer.append(", ");
//            } else {
//                someUnselected = true;
//            }
//        }
//        String spinnerText;
//        if (someUnselected) {
////            spinnerText = spinnerBuffer.toString();
//            if (spinnerText.length() > 2)
//                spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
//        } else {
//            spinnerText = defaultText;
//        }
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
//                android.R.progress_animation.simple_spinner_item,
//                new String[] { defaultText });
//        setAdapter(adapter);
        listener.onItemsSelected(selected);
    }

    public void setItems(List<String> items, HashMap<String, Coin> coins,
                         MultiSpinnerListener listener) {
        this.items = items;
//        this.defaultText = allText;
        this.listener = listener;
        this.coins = coins;

        setSelectedItems();

        // all text on the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, new String[] {defaultText});
        setAdapter(adapter);
    }

    public void setSelectedItems() {
        selected = new boolean[items.size()];
        for (int i = 0; i < selected.length; i++)
            selected[i] = false;

        // Set selected items to true
        if(coins != null && coins.size() > 0) {
            for (int j = 0; j < items.size(); j++)
                if (coins.get(items.get(j)) != null && items.get(j).trim().equalsIgnoreCase(coins.get(items.get(j)).getEx())) {
                    selected[j] = true;
                }
        }
    }


    public interface MultiSpinnerListener {
        public void onItemsSelected(boolean[] selected);
    }
}