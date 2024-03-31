# WikiTest

This is a test suite for the Wiki application. It uses Selenium WebDriver to automate browser interactions and JUnit for testing. The tests cover various scenarios such as highlighting search results, handling invalid queries, and navigating to search suggestions.

## Prerequisites
- Java Development Kit (JDK) installed
- Maven build tool installed (if you don't have Maven, you can download it from [here](https://maven.apache.org/download.cgi))

## Installation

1. Clone the repository: `git clone https://github.com/dsyurchenko96/wikiTest.git`
2. Navigate to the project directory: `cd wikiTest`
3. Build the project: `mvn clean install`

## Usage

To run all tests, execute the following command:
```bash
mvn test
```

- To run a single test, add ```-Dtest=WikiTest#method``` flag, where ```method``` is the name of the exact method you want to test;
- To run multiple tests, add ```-Dtest=WikiTest#method1+method2``` flag, where you list all the methods you need to test with a **'+'** sign.

