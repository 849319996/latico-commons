package com.latico.commons.selenium;
 
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class Itest {
    @Test
    public void main() {
 
        WebDriver driver = new HtmlUnitDriver();
        driver.get("http://www.itest.info");
 
        String title = driver.getTitle();
        System.out.printf(title);
 
        driver.close();
    }

    /**
     *
     */
    @Test
    public void name() throws Exception {
        final WebClient mWebClient = new WebClient();
        mWebClient.getOptions().setCssEnabled(false);
        mWebClient.getOptions().setJavaScriptEnabled(false);
        final HtmlPage mHtmlPage = mWebClient.getPage("https://www.baidu.com");
        System.out.println(mHtmlPage.asText());
        mWebClient.closeAllWindows();
    }
}