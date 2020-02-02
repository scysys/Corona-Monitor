package com.sunsolution.coronamonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sunsolution.coronamonitor.models.Data;
import com.sunsolution.coronamonitor.services.CrawlerAsyncTask;
import com.sunsolution.coronamonitor.services.JSHtmlInterface;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements CrawlerAsyncTask.CrawlerAsyncTaskListener, JSHtmlInterface.JSHtmlListener {

    private CrawlerAsyncTask crawlerAsyncTask;
    private WebView browser;

    private TextView tvTime, tvSick, tvDead, tvHeath, tvVietNamTitle, tvCountryTitle;
    private RecyclerView rcCity, rcCountry;
    private ProgressBar progressLoader;
    private ItemCityAdapter itemCityAdapter;
    private ItemCountryAdapter itemCountryAdapter;
    private Timer timerCrawl, timerBridge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFakeWebView();

        tvTime = findViewById(R.id.tvTime);
        tvSick = findViewById(R.id.tvSick);
        tvDead = findViewById(R.id.tvDead);
        tvHeath = findViewById(R.id.tvUp);
        rcCity =  findViewById(R.id.rcCity);
        rcCountry =  findViewById(R.id.rcCountry);
        tvVietNamTitle = findViewById(R.id.tvVietNamTitle);
        tvCountryTitle = findViewById(R.id.tvCountryTitle);
        progressLoader = findViewById(R.id.progressLoader);

        itemCityAdapter = new ItemCityAdapter(null);
        rcCity.setLayoutManager(new LinearLayoutManager(this));
        rcCity.setHasFixedSize(true);
        rcCity.setAdapter(itemCityAdapter);

        itemCountryAdapter = new ItemCountryAdapter(null);
        rcCountry.setLayoutManager(new LinearLayoutManager(this));
        rcCountry.setHasFixedSize(true);
        rcCountry.setAdapter(itemCountryAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        crawlData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timerCrawl != null) timerCrawl.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timerCrawl != null) timerCrawl.cancel();
        if (timerBridge != null) timerBridge.cancel();
        if (crawlerAsyncTask != null) crawlerAsyncTask.cancel(true);
    }

    private void initFakeWebView() {
        browser = new WebView(this);
        browser.setLayerType(View.LAYER_TYPE_NONE,null);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        browser.getSettings().setDomStorageEnabled(true);
        browser.addJavascriptInterface(new JSHtmlInterface(this), "JSBridge");
        browser.setWebViewClient(new SunSolutionWebClient());
        browser.setWebChromeClient(new WebChromeClient());
    }

    private void crawlData(){
        if (timerCrawl != null) timerCrawl.cancel();
        timerCrawl = new Timer();
        timerCrawl.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressLoader.setVisibility(View.VISIBLE);
                        browser.loadUrl(Constants.KOMPA_AI_DOMAIN);
                    }
                });
            }
        },0,180000);
    }

    private void startCrawler(String html) {
        if (crawlerAsyncTask != null) crawlerAsyncTask.cancel(true);
        crawlerAsyncTask = CrawlerAsyncTask.newInstance(this);
        crawlerAsyncTask.execute(html);
    }

    @Override
    public void onStartCrawler() {

    }

    @Override
    public void onErrorCrawler() {

    }

    @Override
    public void onCompleteCrawler(Data data) {
        if (TextUtils.isEmpty(data.getTimeUpdate())) return;
        progressLoader.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(data.getTimeUpdate()))
            tvTime.setText(data.getTimeUpdate());
        if (!TextUtils.isEmpty(data.getSick()))
            tvSick.setText(data.getSick());
        if (!TextUtils.isEmpty(data.getDead()))
            tvDead.setText(data.getDead());
        if (!TextUtils.isEmpty(data.getSick()))
            tvHeath.setText(data.getHealth());
        itemCityAdapter.replaceCities(data.getCityList());
        if (data.getCityList().size() > 0 ) {
            tvVietNamTitle.setText(String.format("Số ca nhiễm theo tỉnh thành ở Việt Nam (%s tỉnh thành)", data.getCityList().size()));
        }
        if (data.getCountryList().size() > 0) {
            tvCountryTitle.setText(String.format("Số ca nhiễm theo quốc gia (%s quốc gia)", data.getCountryList().size()));
        }
        itemCountryAdapter.replaceCountries(data.getCountryList());

    }

    @Override
    public void onLoadedHtml(String html) {
        startCrawler(html);
    }

    class SunSolutionWebClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (timerBridge != null) timerBridge.cancel();
            timerBridge = new Timer();
            timerBridge.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            browser.loadUrl("javascript:window.JSBridge.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                        }
                    });
                }
            },6000);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }
    }
}
