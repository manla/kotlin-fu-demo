# Demo Project for Functional Programming in Kotlin

## Goal
The project illustrates with several implementation variants of the same functionality
the possible use of functional programming patterns and the [Arrow](https://arrow-kt.io) library.
To allow comparison a base implementation of a simple functionality is provided in a common 
object oriented way using [Spring Boot](https://spring.io/projects/spring-boot). Referring to that base 
implementation further implementation variants introduce aspects.
 
## Example Functionality
As example application we choose a simple effort calculator with a REST interface. As functionality, 
we have the possibility to
* indicate how many hours you have worked on which day
* query how much money is to be factured for which period

The hourly wage differs depending if the efforts are provided on  working days or during the weekend.

Example queries can be found in file [rest.http](http/rest.http).

The example covers typical aspects of an enterprise application as simple as possible.
* Controller
* Business logic 
* Persistence
* Handling of null values
* Exceptions

## Implementations

### Object Oriented Implementation with Spring and JDBC
Entry point is class [V1Controller](src/main/kotlin/com/maranin/kotlinfundemo/v1springtraditional/V1Controller.kt).
The implementation is object oriented. 
It deals with aspects like nullable values and exceptions in an object oriented programming style.

### Replace Nullable Values with Options
Entry point is class [V2Controller](src/main/kotlin/com/maranin/kotlinfundemo/v2option/V2Controller.kt).
Nullable values are replaced with Arrow's data type Option.
While the use of Option is a popular functional programming pattern in general, it competes in Kotlin 
with the explicit handling of nullable and non-nulable values with *?*. The Arrow team seems to accept 
the use of the Kotlin idioms. So the use of Option is not recommended but deprecated. 
 
### Eliminate Exceptions with Either
Entry point is class [V3Controller](src/main/kotlin/com/maranin/kotlinfundemo/v3either/V3Controller.kt).
This variant handles null values as well as exceptional cases with *Either*.
Problematic cases are coded as subtypes of sealed class 
[BadCalculation](src/main/kotlin/com/maranin/kotlinfundemo/v3either/Problems.kt).
Using sealed classes is the Kotlin way to emulate abstract data types. 

### Simplify Exception handling with IO Monad
Entry point is class [V4Controller](src/main/kotlin/com/maranin/kotlinfundemo/v4io/V4Controller.kt).
The error handling is simnplified by using an IO monad. 
An IO monad can catch and handle exceptions and provides the result as an Either.
Nice examples of IO monads can be found in 
[Monads & Composition with Arrow Fx](https://mattmoore.io/blog/arrow-io-monad)
and
[Cleaner Composition with Monad Comprehensions in Arrow Fx](https://mattmoore.io/blog/arrow-io-monad-comprehensions).

### Parallel processing
A nice examplecan be found in [Parallel Processing The Functional Way with Arrow Fx](https://mattmoore.io/blog/arrow-io-parallel).