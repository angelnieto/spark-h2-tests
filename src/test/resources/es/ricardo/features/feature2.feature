Feature: Student Update
Description: The purpose of this feature is to test the modification of table values
 
Scenario: Modification and creation of students
    Given an "insert" operation in table "student" to insert a new student with the value "Nelle" in the field "first_name" and the value "Porter" in the field "last_name"
    And an "update" operation in table "student" we set the value "Ally" in the field "first_name" for the student with field "id" with value 1
    When the student data is collected 
    Then the student in the table "student" with field "id" with value 1 changed the field "first_name" to the value "Ally" 
