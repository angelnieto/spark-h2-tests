@regression
Feature: Lawyer Update
Description: The purpose of this feature is to test the insertion of table values
 
Scenario: Modification and creation of lawyers
    Given an "insert" operation in table "ally_macbeal.lawyer" to insert a new lawyer with the value "Ling" in the field "first_name" and the value "Woo" in the field "last_name"
    When the "ally_macbeal.lawyer" data is collected 
    Then the lawyer in the table "ally_macbeal.lawyer" with field "first_name" with value "Ling" has the value "Woo" in the field "last_name" 
