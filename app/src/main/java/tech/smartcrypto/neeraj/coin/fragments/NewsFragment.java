package tech.smartcrypto.neeraj.coin.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import java.util.zip.Inflater;

import tech.smartcrypto.neeraj.coin.R;


public class NewsFragment extends Fragment{

    public NewsFragment() {
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



        View view = inflater.inflate(R.layout.fragment_news, container, false);
       // WebView webview = view.findViewById(R.id.webview);
        //webview.getSettings().setJavaScriptEnabled(true);

        //webview.loadUrl("https://news.google.com/news/search/section/q/cryptocurrency%20news/cryptocurrency%20news?ned=us&gl=US&hl=en");

        return view;
    }

}