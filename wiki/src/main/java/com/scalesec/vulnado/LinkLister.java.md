# Documentation: `LinkLister` Class

## Overview
The `LinkLister` class is a utility designed to extract hyperlinks (`<a>` tags) from a given URL. It provides two methods for retrieving links, with one of them (`getLinksV2`) adding additional validation to prevent the use of private IP addresses.

## Class: `LinkLister`

### Package
`com.scalesec.vulnado`

### Dependencies
The class relies on the following libraries:
- **JSoup**: For parsing and extracting HTML content.
- **Java Standard Libraries**:
  - `java.util.ArrayList` and `java.util.List`: For managing collections of links.
  - `java.io.IOException`: For handling I/O exceptions.
  - `java.net.URL`: For URL parsing and validation.

---

## Methods

### 1. `getLinks(String url)`
#### Description
Extracts all hyperlinks (`<a>` tags) from the HTML content of the provided URL.

#### Parameters
| Name | Type   | Description                     |
|------|--------|---------------------------------|
| `url` | `String` | The URL of the webpage to parse. |

#### Returns
| Type          | Description                              |
|---------------|------------------------------------------|
| `List<String>` | A list of absolute URLs extracted from the webpage. |

#### Exceptions
| Exception       | Description                                      |
|-----------------|--------------------------------------------------|
| `IOException`   | Thrown if there is an issue connecting to the URL or retrieving its content. |

#### Logic
1. Connects to the provided URL using `Jsoup.connect(url).get()`.
2. Selects all `<a>` elements from the HTML document using `doc.select("a")`.
3. Extracts the absolute URL of each link using `link.absUrl("href")`.
4. Adds the extracted URLs to a `List<String>` and returns it.

---

### 2. `getLinksV2(String url)`
#### Description
An enhanced version of `getLinks` that validates the URL to ensure it does not point to a private IP address before extracting links.

#### Parameters
| Name | Type   | Description                     |
|------|--------|---------------------------------|
| `url` | `String` | The URL of the webpage to parse. |

#### Returns
| Type          | Description                              |
|---------------|------------------------------------------|
| `List<String>` | A list of absolute URLs extracted from the webpage. |

#### Exceptions
| Exception       | Description                                      |
|-----------------|--------------------------------------------------|
| `BadRequest`    | Thrown if the URL points to a private IP address or if any other error occurs during processing. |

#### Logic
1. Parses the URL using `new URL(url)` to extract the host.
2. Checks if the host starts with any of the following private IP address ranges:
   - `172.`
   - `192.168`
   - `10.`
3. If the host matches a private IP range, throws a `BadRequest` exception with the message "Use of Private IP".
4. If the host is valid, calls the `getLinks` method to extract links.
5. Catches any exceptions during processing and rethrows them as a `BadRequest` exception.

---

## Custom Exception: `BadRequest`
The `BadRequest` exception is used to handle invalid requests, such as when a private IP address is detected or when other errors occur during URL processing.

---

## Insights

### Security Considerations
- **Private IP Address Validation**: The `getLinksV2` method ensures that private IP addresses are not processed, which is a common security measure to prevent Server-Side Request Forgery (SSRF) attacks.
- **Error Handling**: The `getLinksV2` method wraps all exceptions in a custom `BadRequest` exception, providing a consistent error-handling mechanism.

### Usage Scenarios
- **Web Scraping**: The `LinkLister` class can be used to extract all hyperlinks from a webpage for further processing.
- **Security-Conscious Applications**: The `getLinksV2` method is suitable for applications that need to enforce restrictions on private IP address usage.

### Limitations
- **No Rate Limiting**: The class does not implement any rate-limiting mechanism, which could lead to issues when scraping multiple URLs in quick succession.
- **No Validation for Malformed URLs**: While the `URL` class is used for parsing, additional validation for malformed URLs could be beneficial.

### Dependencies
- The class depends on the `Jsoup` library for HTML parsing. Ensure that the library is included in the project dependencies.

---

## Example Usage

### Extracting Links Using `getLinks`
```java
try {
    List<String> links = LinkLister.getLinks("https://example.com");
    for (String link : links) {
        System.out.println(link);
    }
} catch (IOException e) {
    e.printStackTrace();
}
```

### Extracting Links Using `getLinksV2`
```java
try {
    List<String> links = LinkLister.getLinksV2("https://example.com");
    for (String link : links) {
        System.out.println(link);
    }
} catch (BadRequest e) {
    System.err.println("Error: " + e.getMessage());
}
```
