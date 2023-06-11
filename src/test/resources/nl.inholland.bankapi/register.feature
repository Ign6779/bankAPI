Feature: Everything to do with register

  Scenario: registering a user
    Given I have a valid register object with valid first name "joe" and last name "doe" and email "doe@gmail.com" and phone "0990924" and password "test"
    When I call the application register endpoint
    Then I receive a user
    And I get http status 200

