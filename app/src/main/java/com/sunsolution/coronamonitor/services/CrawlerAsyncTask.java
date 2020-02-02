package com.sunsolution.coronamonitor.services;

import android.os.AsyncTask;

import com.sunsolution.coronamonitor.Constants;
import com.sunsolution.coronamonitor.models.City;
import com.sunsolution.coronamonitor.models.Country;
import com.sunsolution.coronamonitor.models.Data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class CrawlerAsyncTask extends AsyncTask<String, Void, Data> {

    private CrawlerAsyncTaskListener crawlerAsyncTaskListener;
    private static CrawlerAsyncTask instance;

    public static CrawlerAsyncTask newInstance(CrawlerAsyncTaskListener crawlerAsyncTaskListener) {
        if (instance == null) instance = new CrawlerAsyncTask(crawlerAsyncTaskListener);
        return instance;
    }

    private CrawlerAsyncTask(CrawlerAsyncTaskListener crawlerAsyncTaskListener) {
        this.crawlerAsyncTaskListener = crawlerAsyncTaskListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (crawlerAsyncTaskListener != null) crawlerAsyncTaskListener.onStartCrawler();
    }

    @Override
    protected Data doInBackground(String... s) {
        Data dataCrawler = new Data();
        Document root = Jsoup.parse(s[0]);
        Element timeUpdated = root.body().getElementsByClass("title-widget").first();
        if (timeUpdated != null) dataCrawler.setTimeUpdate(timeUpdated.text().replace("Cập nhật lần cuối lúc:","").trim());
        Elements sickElements = root.body().getElementsByClass("content-widget");
        if (sickElements != null && sickElements.size() == 4) {
            dataCrawler.setSick(sickElements.get(1).text());
            dataCrawler.setDead(sickElements.get(2).text());
            dataCrawler.setHealth(sickElements.get(3).text());
        }

        Element cityElement = root.body().getElementsByClass("list-group").first();
        if (cityElement != null) {
            Elements cityDetailElement = cityElement.getElementsByTag("li");
            if (cityDetailElement != null) {
                List<City> cities = new ArrayList<>();
                for (Element e : cityDetailElement) {
                    cities.add(new City(
                            e.text().replace(e.getElementsByTag("span").first().text(),""),
                            e.getElementsByTag("span").first().text())
                    );
                }
                dataCrawler.setCityList(cities);
            }
        }

        Element countryElement = root.body().getElementsByClass("list-group").get(1);
        if (countryElement != null) {
            Elements countryDetailElement = countryElement.getElementsByTag("li");
            if (countryDetailElement != null) {
                List<Country> countries = new ArrayList<>();
                for (Element e : countryDetailElement) {
                    countries.add(new Country(
                            e.text().replace(e.getElementsByTag("span").first().text(),""),
                            e.getElementsByTag("span").first().text(),
                            e.selectFirst("img").attr("src").replace("..", Constants.KOMPA_AI_DOMAIN))
                    );
                }
                dataCrawler.setCountryList(countries);
            }
        }

        return dataCrawler;
    }

    @Override
    protected void onPostExecute(Data data) {
        super.onPostExecute(data);
        crawlerAsyncTaskListener.onCompleteCrawler(data);

    }

    public interface CrawlerAsyncTaskListener {
        void onStartCrawler();

        void onErrorCrawler();

        void onCompleteCrawler(Data data);
    }
}
