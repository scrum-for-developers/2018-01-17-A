Meta:
@themes Book

Narrative:
In order to add new books to the library
As a librarian
I want to add books through the website

Scenario:

Given an empty library
When a librarian adds a book with title <title>, desciption <description>, author <author>, edition <edition>, year <year> and isbn <isbn>
Then The booklist contains a book with values title <title>, desciption <description>, author <author>, year <year>, edition <edition>, isbn <isbn>

Examples:
 
| isbn       | author           | title     | description   | edition   | year  |
| 0552131075 | Terry Pratchett  | Sourcery  | Beispiel      | 1         | 1989  |






