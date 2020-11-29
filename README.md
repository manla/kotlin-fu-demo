# Demo Project for Functional Programming in Kotlin

## Goal
The project illustrates with several implementation variants of the same functionality
the possible use of functional programming patterns and the [Arrow](https://arrow-kt.io) library.
To allow comparison a base implementation of a simple functionality is provided in a common 
object oriented way using [Spring Boot](https://spring.io/projects/spring-boot). Referring to that base 
implementation further implementation variants introduce aspects.
 
## Example Functionality
As example application we choose a simple effort calculator with a EST interface. As functionality, 
we have the possibility to
* indicate how many hours you have worked on which day
* query how much money is to be factured for which period

Example queries can be found in file [rest.http](http/rest.http).

Das Beispiel versucht einerseits, Aspekte einer typischen Backend-Anwendung abzubilden (Controller, Geschäftslogik und Persistenz).
Andererseits ist es so einfach wie möglich gehalten, um die hier wichtigen Aspekte hervorzuheben.

## Implementations

### Object Oriented Implementation with Spring and JDBC
Entry point is class [V1Controller](src/main/kotlin/com/maranin/kotlinfundemo/v1springtraditional/V1Controller.kt).
The implementation is object oriented.

 ### Replace Nullable Values with Options
Entry point is class [V2Controller](src/main/kotlin/com/maranin/kotlinfundemo/v2option/V2Controller.kt).
Nullable values are replaced with Arrow's data type Option.
While the use of Option is generally a popular functional programming pattern, it competes in Kotlin with the explicit handling 
of nullable and non-nulable values with *?*. The Arrow team seems to accept the use of the Kotlin idioms. 
So the use of Option is not recommended but deprecated. 
 
