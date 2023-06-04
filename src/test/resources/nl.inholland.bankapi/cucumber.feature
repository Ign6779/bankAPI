Feature: Everything to do with users

  Scenario: getting all users
    Given The endpoint for "users" is available for method "GET"
    When I retrieve all users
    Then I get a list of 4 users
    And I get http status 200