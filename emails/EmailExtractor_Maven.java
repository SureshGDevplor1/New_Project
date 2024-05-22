package com.demo;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailExtractor_Maven {

    public static Set<String> extractEmails(String jobProfile, int maxPages) {
        Set<String> emails = new HashSet<>();

        System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--disable-gpu");
        WebDriver driver = new ChromeDriver(chromeOptions);

        for (int page = 1; page <= maxPages; page++) {
            String url = "https://www.google.com/search?q=%22" + jobProfile
                    + "%22+email+%22%40gmail.com%22+-inurl%3A%22dir%2F%22+site%3Awww.linkedin.com%2Fin%2F&oq=&gs_lcrp=EgZjaHJvbWUqCQgBECMYJxjqAjIJCAAQIxgnGOoCMgkIARAjGCcY6gIyCQgCECMYJxjqAjIJCAMQIxgnGOoCMgkIBBAjGCcY6gIyCQgFECMYJxjqAjIJCAYQIxgnGOoCMgkIBxAjGCcY6gLSAQ03Nzg5OTE1MjdqMGo3qAIIsAIB&sourceid=chrome&ie=UTF-8";
            driver.get(url);
            driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

            String pageSource = driver.getPageSource();
            Pattern pattern = Pattern.compile("\\b[A-Za-z0-9._%+-]+[A-Za-z][0-9]*@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b");
            Matcher matcher = pattern.matcher(pageSource);

            while (matcher.find()) {
                String email = matcher.group();
                if (!email.contains("%")) {
                    emails.add(email);
                }
            }
        }

        driver.quit();
        return emails;
    }

    public static void main(String[] args) {
        try {
            java.util.Scanner scanner = new java.util.Scanner(System.in);
            System.out.print("Enter the job profile: ");
            String jobProfile = scanner.nextLine();
            System.out.print("Number of pages to scrape: ");
            int maxPages = scanner.nextInt();
            scanner.close();

            Set<String> emails = extractEmails(jobProfile, maxPages);
            System.out.println("Extracted emails: " + emails);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
