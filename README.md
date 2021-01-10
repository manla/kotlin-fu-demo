# Demo Project for Functional Programming in Kotlin Using the *Arrow* Library

## Goal
The project illustrates with several implementation variants of the same functionality
the possible use of functional programming patterns and the [Arrow](https://arrow-kt.io) library.
To allow comparison a base implementation of a simple functionality is provided in a common 
object oriented way using [Spring Boot](https://spring.io/projects/spring-boot). Referring to that base 
implementation further implementation variants introduce some functional idioms or pattern.
 
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
The error handling is simplified by using an IO monad from Arrow FX. 
IO monads can catch exceptions and provide results as Either, thus distinguishing errors and valid results.
Further they provide support for async execution.

There are some peculiarities of Arrow IO monads
* They only hold the *description* of calculation and have to be run explicitly.
  They rely on a separation of description and execution of functionality.
* When handling monads, the syntax involving *flatMap()* can become tedious. It can be replaced with that of 
  *for comprehensions*, which provides a simple,  imperative flavour.  
* When writing tests for IO monads, keep two things in mind
  * Don't forget to run the monad. If you do, the test cases are not run and errors will never show up. 
  * You have to unwrap the values contained in the monad to test them 
* IO monads assume the convention that non-pure functions ( i.e. functions with side effects) are declared as *suspend* functions.
  In doing so, they ty the functional concept of IO monad to artefacts of Kotlin coroutines.
  We did not follow that convention because it did conflict with Spring constructs for methods used as REST Endpoints or repository methods.

Nice examples illustrating the use of IO monads can be found in 
[Monads & Composition with Arrow Fx](https://mattmoore.io/blog/arrow-io-monad)
and
[Cleaner Composition with Monad Comprehensions in Arrow Fx](https://mattmoore.io/blog/arrow-io-monad-comprehensions).


### Parallel processing
Entry point is class [V5Controller](src/main/kotlin/com/maranin/kotlinfundemo/v5par/V5Controller.kt).
The example illustrates the use of parallelism with Arrow FX's function *parTraverse()* while keeping the use of IO monads.
The example is a bit artificial. The invoice for a period of time is calculated, where the efforts for the single days 
are accessed and calculated in parallel. 

Another nice example for the use of parTraverse() can be found in 
[Parallel Processing The Functional Way with Arrow Fx](https://mattmoore.io/blog/arrow-io-parallel).