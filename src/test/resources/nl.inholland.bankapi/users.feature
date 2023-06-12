Feature: Everything to do with users

  Scenario: getting all users
    Given The endpoint for "users" is available for method "GET"
    When I retrieve all users
    Then I get a list of 4 users
    And I get http status 200

    Scenario: updating a user
      Given The endpoint for "users" is available for method "PUT"
      And I log in as employee
      And There is a user with ID "62eeb3fd-b1a1-4671-970c-e02efa2fb5e1" with first name "El jefe" in the system
      When I update the firstname of the user "62eeb3fd-b1a1-4671-970c-e02efa2fb5e1" to "El patron"
      Then The first name of user "62eeb3fd-b1a1-4671-970c-e02efa2fb5e1" is "El patron"
      And I get http status 200


      Scenario: deleting a user
        Given The endpoint for "users" is available for method "Delete"
        And I log in as employee
        And There is a user with ID "62eeb3fd-b1a1-4671-970c-e02efa2fb5e1" with first name "El jefe" in the system
        When I delete the user with ID "62eeb3fd-b1a1-4671-970c-e02efa2fb5e1" 
        Then The user with ID "62eeb3fd-b1a1-4671-970c-e02efa2fb5e1" is not found in the system
        And I get http status 200



