# creepy-crawlies

## How to run
This is a typical springboot application
1. ./gradlew clean build
2. ./gradlew bootRun
3. go to http://localhost:8080 - this will start the crawling process

## Tech choices

For this particular problem I decided to use the following frameworks/libraries

1. Springboot with Gradle
  * Straightforward to set up out of the box
  * Easy to build and run

2. JSoup
  * Good documentation on how to use
  * Up and running quickly compared to building own version
  * Well supported
  * MIT Licence
  * One issue I had using this library is that it is hard to stop it from polluting the whole web crawler with it's objects.
    It comes through from the crawler down to the page parser.
    This isn't an issue for this particular project however if it was to be removed in favour of another library,
    there would be a significant amount of overhaul.

3. Guava
  * This was used to derive the top level domain to ensure sub domains were traversed.
  * Slightly heavy for finding the TLD however it enabled me to concentrate on the rest of the logic without having
    to reinvent the wheel in multiple areas.

4. Bootstrap
  * Can also be seen as overkill for returning the list of pages however it also allowed me to get something together
    quicker without having to worry too much about look and feel of the results page.

## Design decisions

In terms of testing, I depended more on having select methods protected in order for me to be able to override them and inject
them with mock objects for testing. The downside to this is having protected methods instead of private. I felt however that this
was a fair trade-off to stop from having field level objects in the classes for testing.

My approach was to build out the core logic for parsing a single page first,
creating a single class called Crawler, and then subsequently pulling that logic out in to separate smaller classes once the
roles & responsibilities of each method became clearer and then refactoring the tests to sit in the test class that coincides
with the production class. This is a small byproduct of TDD but the benefits far outweigh any disadvantages here.

## Integration Tests

Integration tests are light here, the reason for this is that the url to process is fixed and mocking the
underlying code that's calling in to JSoup would requiring autowiring mock singletons which I decided to stay away from.
Another possibility is to have a mock server spin up for the creepy crawler to parse but felt this was outwith the scope of this
demo.

## Future considerations

1. Currently the code will only process one document at a time. A quick way to process the pages would be to spawn off multiple
threads which would take a url from the queue, grab the document, parse the page and then add that result into the list of pages
which would be synchronised.

2. Possibility of adhering to robots.txt to ensure I don't get blocked from websites!