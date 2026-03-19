Feature:Authentication

  Scenario: Successful login
    Given a user exists with login "vet13" and password "123" and role "VET"
    When the user sends a POST request to "/auth/login" with the credentials
    Then the response status code should be 200



