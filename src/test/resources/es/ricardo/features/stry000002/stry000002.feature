@stry000002
Feature: Lawyer Update And Modification
Description: The purpose of this feature is to test the modification and insertion of table values
 
Scenario: Modification and creation of lawyers
    Given an "insert" operation in table "ally_macbeal.lawyer" to insert a new lawyer with the value "Nelle" in the field "first_name" and the value "Porter" in the field "last_name"
    And an "update" operation in table "ally_macbeal.lawyer" we set the value "Ally" in the field "first_name" for the lawyer with field "id" with value 1
    When the "ally_macbeal.lawyer" data is collected 
    Then the lawyer in the table "ally_macbeal.lawyer" with field "id" with value 1 changed the field "first_name" to the value "Ally" 
