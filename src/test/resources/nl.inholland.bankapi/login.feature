Feature: Everything to do with login

  Scenario: Log in with valid user
    Given I have a valid login object with valid user and valid password
    When I call the application login endpoint
    Then I receive a token
    And I get http status 200

  Scenario: Login with valid username but invalid password
    Given I have a valid username but invalid password
    When I call the application login endpoint
    Then I get http status 403

  Scenario: Login with invalid username and valid password
    Given I have an invalid username and valid password
    When I call the application login endpoint
    Then I get http status 403

