# Documentation: `LinkLister.java`

## Overview
The `LinkLister` class provides functionality to extract hyperlinks from a given URL. It uses the `Jsoup` library for HTML parsing and includes methods to retrieve links with additional validation for private IP addresses.

---

## Class: `LinkLister`

### Purpose
The class is designed to fetch and process hyperlinks (`<a>` tags) from a webpage. It includes two methods:
1. `getLinks`: Retrieves all hyperlinks from a given URL.
2. `getLinksV2`: Adds validation to prevent fetching links from private IP addresses.

---

## Methods

### 1. `getLinks(String url)`
#### Description
Fetches all hyperlinks (`<a>` tags) from the provided URL and returns them as a list of absolute URLs.

#### Parameters
| Name | Type   | Description                     |
|------|--------|---------------------------------|
| `url` | `String` | The URL of the webpage to parse. |

#### Returns
| Type          | Description                          |
|---------------|--------------------------------------|
| `List<String>` | A list of absolute URLs extracted from the webpage. |

#### Exceptions
| Type          | Description                          |
|---------------|--------------------------------------|
| `IOException` | Thrown if there is an issue connecting to the URL or parsing the webpage. |

#### Logic
1. Connects to the given URL using `Jsoup.connect(url).get()`.
2. Selects all `<a>` elements using `doc.select("a")`.
3. Extracts the absolute URL of each hyperlink using `link.absUrl("href")`.
4. Adds the URLs to a `List<String>` and returns it.

---

### 2. `getLinksV2(String url)`
#### Description
Fetches all hyperlinks from the provided URL, but includes validation to block URLs pointing to private IP addresses.

#### Parameters
| Name | Type   | Description                     |
|------|--------|---------------------------------|
| `url` | `String` | The URL of the webpage to parse. |

#### Returns
| Type          | Description                          |
|---------------|--------------------------------------|
| `List<String>` | A list of absolute URLs extracted from the webpage. |

#### Exceptions
| Type          | Description                          |
|---------------|--------------------------------------|
| `BadRequest`  | Thrown if the URL points to a private IP address or if any other error occurs during processing. |

#### Logic
1. Parses the URL using `new URL(url)` to extract the host.
2. Checks if the host starts with private IP address prefixes (`172.`, `192.168`, or `10.`).
   - If true, throws a `BadRequest` exception with the message "Use of Private IP".
3. If the host is valid, calls `getLinks(url)` to fetch the links.
4. Catches any exceptions and wraps them in a `BadRequest` exception.

---

## Insights

### 1. **Private IP Validation**
The `getLinksV2` method ensures that links are not fetched from private IP addresses, which is a security measure to prevent accessing internal or restricted resources.

### 2. **Dependency on Jsoup**
The class relies on the `Jsoup` library for HTML parsing, which simplifies the process of extracting elements from a webpage.

### 3. **Custom Exception Handling**
The `getLinksV2` method uses a custom exception (`BadRequest`) to handle errors, providing more meaningful error messages.

### 4. **Potential Enhancements**
- **Timeout Handling**: Add timeout settings for `Jsoup.connect()` to handle slow or unresponsive URLs.
- **URL Validation**: Validate the URL format before processing to avoid unnecessary exceptions.
- **Logging**: Replace `System.out.println` with a proper logging framework for better traceability.

---

## Dependencies
| Library | Purpose                          |
|---------|----------------------------------|
| `Jsoup` | HTML parsing and element selection. |
| `java.net` | URL parsing and host extraction. |

---

## File Metadata
| Key       | Value              |
|-----------|--------------------|
| File Name | `LinkLister.java`  |
