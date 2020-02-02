package com.sunsolution.coronamonitor.services;

public class JSHtmlInterface {

    private JSHtmlListener jsHtmlListener;

    public JSHtmlInterface(JSHtmlListener jsHtmlListener) {
        this.jsHtmlListener = jsHtmlListener;
    }

    @android.webkit.JavascriptInterface
    public void showHTML(String html) {
        if (jsHtmlListener != null) jsHtmlListener.onLoadedHtml(html);
    }

    public interface JSHtmlListener{
        void onLoadedHtml(String html);
    }
}
