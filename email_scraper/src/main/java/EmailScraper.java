/*
 * This Java source file was generated by the Gradle 'init' task.
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailScraper {

    private HashSet<String> links;
    private Queue<String> queue;
    private HashSet<String> emails;
    private String domain;
    private int link_count;

    // TODO: factor out NUMBER_OF_LINKS as a cli option with default value
    private final static int NUMBER_OF_LINKS = 100;

    public EmailScraper(String url) {
        links = new HashSet<>();
        queue = new LinkedList<>();
        emails = new HashSet<>();
        domain = url.substring(7); // strip off "http://"
        link_count = 0;
//        System.out.println("domain = " + domain);
    }

    public String getHelp() {
        return "java -jar EmailScraper-all.jar [my_url]";
    }

    // find all urls that start with domain and add them to the HashSet
    private void getPageLinks() {
        // list of web pages to be examined
        queue.add("http://" + domain);

        while (!queue.isEmpty()) {
            String v = queue.remove();

            // skip https to avoid jsoup's exception: "java.net.UnknownHostException: https"
            // potential solution: https://stackoverflow.com/questions/11392614/unknownhostexception-while-accessing-https-url-using-jsoup
            if (v.startsWith("https://")) {
                continue;
            }
//            System.out.println(v);

            try {
                final Document doc = Jsoup.connect(v).get();
                getEmails(doc);
                ++link_count;

                // visit links only if total number of visited links is not greater than NUMBER_OF_LINKS
                if (link_count > NUMBER_OF_LINKS) {
                    break;
                }

                final Elements links2go = doc.select("a[href]");
                for (Element link : links2go) {
                    final String link_str = link.attr("abs:href");

                    // visit links only if not being visited
                    if (links.add(link_str)) {
//                        System.out.println(getDomainName(link_str));

                        // visit links only if not sub-domain
                        if (getDomainName(link_str).equals(domain)) {
                            queue.add(link_str);
//                            System.out.println(link_str);
                        }
                    }
                }
            } catch (IOException e) {
                // handle "org.jsoup.HttpStatusException: HTTP error fetching URL. Status=404, ..."
//                System.err.println("For '" + v + "': " + e.getMessage());
            }
        }
    }

    public String getDomainName(String url) {
        try {
            URI uri = new URI(url);
            String domain = uri.getHost();
            if (domain != null) {
                return domain.startsWith("www.") ? domain : "www." + domain;
            } else {
                return "";
            }
        } catch (URISyntaxException e) {
//            System.err.println("For '" + url + "': " + e.getMessage());
            return "";
        }
    }

    private void getEmails(Document doc) {
        Pattern p = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
        Matcher matcher = p.matcher(doc.text());
        while (matcher.find()) {
            String email_str = matcher.group();
            if (email_str.endsWith(".")) {
                email_str = email_str.substring(0, email_str.length() - 1);
            }
            if (emails.add(email_str)) {
                System.out.println(email_str);
            }
        }
    }

    private void printEmails() {
        System.out.println("Found these email addresses:");
        for (String email: emails) {
            System.out.println(email);
        }
    }

    public static void main(String[] args) {
        // generate url starting with http://www.
        // note: tentatively ignore https for simplicity
        String url = new CliOpts(args).invoke();
        if(!url.startsWith("www.")&& !url.startsWith("http://")){
            url = "www."+url;
        }
        if(!url.startsWith("http://")){
            url = "http://"+url;
        }

        // scrape emails by web crawler agent
        EmailScraper es = new EmailScraper(url);
        es.getPageLinks();
//        es.printEmails();
    }
}
