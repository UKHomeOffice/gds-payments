Feature: I make a payment

  Scenario: I make a payment
    Given I am on the start page for the form
    Then I can see the local form
#    When I complete the first page of the form incorrectly
#    Then I am presented with validation errors for the first page
    When I complete the local form correctly
    Then I am taken to the payment form
    When I enter my payment details
    Then I am taken to the payment confirmation page
    When I confirm my payment
    Then I am taken to the local success page
