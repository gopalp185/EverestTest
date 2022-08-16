Price and Availability Comparator for books: Amazon and Flipkart - Using API Testing and UI Testing:

API and UI testing of amazon.in and Flipkart using Java with Selenium and RestAssured in a Maven Project. This document demonstrates how to structure, set up, tear down, and conduct testing.

Some Inportant Libraries Used over Java:

1.Selenium 2.Rest Assured 3.OpenCSV 4.Junit 5.Jsoup 6.Log4J 7.Gson 8.Maven Surefire plugin

Prerequisites:

1.Need an input CSV sheet with Book Name and Quantity(bookList.csv)

2.Need 2 arguments to be parametrized in the runnable configurations: I. args[0] : Test Type - API/UI Ii. args[1]: Browser - Currently supports Safari/Chrome/Firefox, if anything else is mentioned or it's blank, API will be triggered 3.Specify the JSON path for logs: Sample:

"book1: 978-1472150936" { "Book Name":"The Candy House", "Publisher":"Corsair", "Author":"Jennifer Egan", "Qty":10, "Amazon":{ "Price":"₹1789", "Is available":"true", "Quantity":"10" }, "Flipkart":{ "Price":"₹509", "Is available":"true", "Quantity":"10" } },

4.Specify the CSV path for comparison results: Sample:

Running the tests: I. It can run through the command line by calling the main method Ii. We can use any IDE to execute the same Iii. You can use Maven Test or generate a build and schedule it through Jenkins

Step 1: After setting up the maven project, need to supply inputs(book name) and Quantity in the bookList.csv

Step 2: Provide the test type and browser name in the arguments Sample: UI Safari API None If API and correct browser name is provided, still it will execute API testing only

Step 3: If UI is selected, check for the specific Selenium driver in the source, and check for the version of driver and browser. The driver should be compatible before it launches the test case in the browser

Step 4: Run the main method(EverestMain.java) or trigger the Maven Test if using iDE

Step 5: Check for the logs and comparator csv post completion

Structure: Each Activity is assigned to a separate package for smooth debugging and traversing.

Connect : Mail me at: gopal.krishna185@gmail.com for further understanding and discussion

Footer
© 2022 GitHub, Inc.
Footer navigation
Terms
