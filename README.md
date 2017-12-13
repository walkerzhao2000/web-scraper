# Email Scraper

Extracting email addresses from websites

## Intro

The email scraper is a java implementaion of web crawler to scrape email addresses by crawling webs with breadth-first-search.
It is used for scaping the email addresses from an input domain name.

## Dependency and Installation

* java: https://java.com/en/download/
* gradle: https://gradle.org/install/
* jsoup (maven dependency): https://jsoup.org/

## Build

On Windows
```
gradlew.bat fatJar
```

On Linux
```
gradlew fatJar
```

## Usage

The email scraper reads a domain name from command line input and scrapes email addresses from that domain only.
Please note that the sub-domains are explicited excluded from the web crawling.
So the email addresses in the websites of the sub-domains will not be discovered.

Command format:
```
java -jar path_to_your_location\email-scraper-all.jar your_domain_name
```

Example command:
```
java -jar build\libs\email-scraper-all.jar web.mit.edu
```

Example command output (given NUMBER_OF_LINKS=100):
(note: NUMBER_OF_LINKS is explained in Improvement below)
```
Found these email addresses:
expertrequests@mit.edu
search-comments@mit.edu
anthro@mit.edu
stsprogram@mit.edu
pkgcenter@mit.edu
copyright@mit.edu
mta-request@mit.edu
politicalscience@mit.edu
web-query@mit.edu
admissions@mit.edu
wgs@mit.edu
odge@mit.edu
nse-info@mit.edu
community-giving@mit.edu
tele-info@mit.edu
ir-www@mit.edu
tpass@mit.edu
gradadmissions@mit.edu
misti@mit.edu
registrar-www@mit.edu
commuting@mit.edu
campus-map@mit.edu
mitparking@mit.edu
```

## Design

* Use breadth-first-search for web crawling to avoid running out of memory.
  In contrast, depth-first-search would build up stack quickly.
  Since there are billions of websites available nowadays, it would run out of stack quickly.
  The implementaion of breath-first-search is done by Queue.

* Use HashSet for remembering the links that have been scraped from websites.
  The purpose is to avoid the loop in graph search.
  In particular, HashSet serves two goals: (1) do not allow redundency; (2) lookup time is O(1).

* Use HashSet for storing the scraped email addressed for the same two goals.

## Improvement

* The length of web crawling is limited by NUMBER_OF_LINKS.
  NUMBER_OF_LINKS is an important constant to avoid spending too much time and resources to explore the websites.
  In this version of implementation, NUMBER_OF_LINKS is just a constant defined in code.
  To improve, it can be factored out to become a command-line-input option.

* The logging needs to be refactored to log better than System.out/err.println.

* The unit test needs to be enhanced.

* A python or shell wrapper could be created to make an easy use.