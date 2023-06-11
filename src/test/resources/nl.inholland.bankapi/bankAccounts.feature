Feature: Everything to do with bankAccounts

  Scenario: getting all bankAccounts
    Given I log in as employee
    And The endpoint for "bankAccounts" is available for method "GET"
    When I retrieve all bankAccounts
    Then I get a list of 8 bankAccounts
    And I get http status 200


    Scenario: creating bank account
      Given I log in as employee
      And The endpoint for "bankAccounts" is available for method "POST"
      When I provide a bank account with a user that has 1 and has absoluteLimit 100.00 and balance 0.0
      Then Then the balance of the bank account is 0
      And I get http status 200

      Scenario: updating a bank account
        Given I log in as employee
        And The endpoint for "bankAccounts" is available for method "PUT"
        And There is a bank account with iban "NLINHO0219447354" with absolute limit 100.0 in the system
        When I update the absolute limit of the bank account "NLINHO0219447354" to 500.0
        Then The absolute limit of bank account "NLINHO0219447354" is 500.0
        And I get http status 200
