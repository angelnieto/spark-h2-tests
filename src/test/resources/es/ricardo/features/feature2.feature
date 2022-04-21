@feature_lawyers2
Feature: Lawyer Update And Modification
Description: The purpose of this feature is to test the modification and insertion of table values
 
Scenario: Modification and creation of lawyers
    Given an "insert" operation in table "lawyer" to insert a new student with the value "Nelle" in the field "first_name" and the value "Porter" in the field "last_name"
    And an "update" operation in table "lawyer" we set the value "Ally" in the field "first_name" for the student with field "id" with value 1
    When the "lawyer" data is collected 
    Then the student in the table "lawyer" with field "id" with value 1 changed the field "first_name" to the value "Ally" 
