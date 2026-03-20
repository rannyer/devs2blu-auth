Feature:Authentication

  Scenario: Successful login
    Given a user exists with login "vet13" and password "123" and role "VET"
    When the user sends a POST request to "/auth/login" with the credentials
    Then the response status code should be 200

  Scenario: Login with invalid password
    Given a user exists with login "vet13" and password "234" and role "VET"
    When the user sends a POST request to "/auth/login" with the credentials "vet13" and password "999"
    Then the response status code should be 403

  Scenario: Register with invalid role
    When a user sends a POST request to "/auth/register" with login "vet14", password "123", and role "INVALID_ROLE"
    Then the response status code should be 400
