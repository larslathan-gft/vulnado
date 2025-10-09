# Documentation: `LinkLister` Class

## Overview
The `LinkLister` class provides functionality to extract hyperlinks (`<a>` tags) from a given URL. It includes two methods: one for basic link extraction and another with additional validation to prevent the use of private IP addresses.

---

## Class: `LinkLister`

### Package
`com.scalesec.vulnado`

### Dependencies
- **Jsoup**: Used for parsing and extracting HTML content.
- **java.net.URL**: Used for URL parsing and validation.
- **java.io.IOException**: Handles input/output exceptions.
- **Custom Exception**: `BadRequest` is used for handling specific validation errors.

---

## Methods

### 1. `getLinks(String url)`
#### Description
Extracts all hyperlinks (`<a>` tags) from the HTML content of the given URL.

#### Parameters
| Name | Type   | Description                     |
|------|--------|---------------------------------|
| `url` | `String` | The URL of the webpage to parse. |

#### Returns
| Type          | Description                                      |
|---------------|--------------------------------------------------|
| `List<String>` | A list of absolute URLs extracted from the page. |

#### Exceptions
| Exception       | Description                                      |
|-----------------|--------------------------------------------------|
| `IOException`   | Thrown if there is an issue connecting to the URL or retrieving its content. |

#### Logic
1. Connects to the provided URL using `Jsoup.connect(url).get()`.
2. Selects all `<a>` elements from the HTML document.
3. Extracts the absolute URL (`absUrl("href")`) from each `<a>` tag.
4. Returns a list of all extracted URLs.

---

### 2. `getLinksV2(String url)`
#### Description
An enhanced version of `getLinks` that validates the URL to ensure it does not point to a private IP address before extracting links.

#### Parameters
| Name | Type   | Description                     |
|------|--------|---------------------------------|
| `url` | `String` | The URL of the webpage to parse. |

#### Returns
| Type          | Description                                      |
|---------------|--------------------------------------------------|
| `List<String>` | A list of absolute URLs extracted from the page. |

#### Exceptions
| Exception       | Description                                      |
|-----------------|--------------------------------------------------|
| `BadRequest`    | Thrown if the URL points to a private IP address or if any other error occurs. |

#### Logic
1. Parses the URL using `java.net.URL`.
2. Extracts the host from the URL.
3. Checks if the host starts with any of the following private IP address ranges:
   - `172.`
   - `192.168`
   - `10.`
4. If the host matches a private IP range, throws a `BadRequest` exception with the message "Use of Private IP".
5. If the host is valid, calls the `getLinks` method to extract links.
6. Catches any exceptions and rethrows them as a `BadRequest` exception.

---

## Insights

### Security Considerations
- **Private IP Validation**: The `getLinksV2` method ensures that private IP addresses are not used, which is a common security measure to prevent SSRF (Server-Side Request Forgery) attacks.
- **Exception Handling**: The use of a custom `BadRequest` exception provides a clear mechanism for handling invalid input or errors during processing.

### Dependencies
- The class relies on the `Jsoup` library for HTML parsing, which simplifies the process of extracting elements from web pages.
- The `java.net.URL` class is used for parsing and validating URLs, ensuring proper handling of hostnames and IP addresses.

### Potential Enhancements
- **Timeout Handling**: Add timeout settings to the `Jsoup.connect` call to prevent long waits for unresponsive URLs.
- **Error Logging**: Implement logging for exceptions to provide better debugging and monitoring capabilities.
- **Customizable Validation**: Allow users to specify additional validation rules for URLs (e.g., allowed domains or protocols).

### Use Cases
- Web scraping to collect hyperlinks from a webpage.
- Validating and filtering URLs based on specific criteria (e.g., avoiding private IPs).
- Building tools for SEO analysis or link auditing.

---

## Exception: `BadRequest`
The `BadRequest` exception is a custom exception used to handle invalid input or errors during URL validation and processing.

#### Usage in `getLinksV2`
- Thrown when the URL points to a private IP address.
- Thrown when any other exception occurs during URL processing.


