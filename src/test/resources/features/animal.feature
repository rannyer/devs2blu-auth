Feature: Animal

  Scenario: Create animal with a VET token
    Given a user existst with login "vetAnimal2" and password "123" and role "VET"
    And the user is athenticated at "/auth/login" with the credentials "vetAnimal2" and password "123"
    When the user sends a POST request to "/animais" with authenticated token and name "Buddy", species "Dog"
    Then the response status code should be 200
