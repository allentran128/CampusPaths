### HW5 Feedback

**CSE 331 17sp**

**Name:** <student name> (alltran)

**Graded By:** Isaac Ahn (esa9405@cs.washington.edu)

### Score: 73/82
---

**Problem 1 - Written Excercises:** 9/13

- a. this.size >= this.entries.length? upper bound for this.front?  <Oh, forgot to specify that size cannot exceed the entries length>

- b. Good
- c. (1) No, int is pass by value, thus no rep exposure. <So we're looking for potential pass-by-refs>
     (2) It may expose representation but not because of the boolean, it may expose because the return String Array may be used internally, and since array is mutable.  <Mutable returns may affect the internals/expose the underlying data structures>
     (4) It wouldn't expose representation since String is immutable in Java (might be mutable in other languages, ex. Python) <Ah, key point of rep exposure the question of mutability!>
     (6) If the elements of cards are not copied when Deck is constructed, and Card is mutable, representation is exposed. If the list passed to the constructor is used by Deck without being copied, then the client could retain a reference to Deck’s internal representation. <Again, exposing references to the internals is bad!>

     General: being/returning an object doesn't mean it exposes representation (object could be guarded by immutability, aka. immutable object)
     Moreover, method parameters have nothing to do with representation exposure. It is about if the object gives clients things that clients can modify to break the representation. (thus the object becomes invalid and may perform wrong)
     Invalid method parameters should be guarded internally by exception (with @throws) or @requires tag.

**Problem 2 - Graph Specification:** 24/25

- API/Javadoc: 14/15
  - Missing Javadoc for Graph Interface (anything public needs Javadoc documentation for clients)
- Writeup: 10/10
  - Good

**Problem 3 - Tests :** 11/12
- Always use Junit asserts in Junit tests. ("assert" is Java assert)
- Review section slides to know how to assert for exceptions.

**Problem 4 - Graph Implementation:** 22/25

- Correctness: 10/10
  - Good
- Style: 7/10
  - Missing Abstraction Function in StringNode
  - Missing checkRep for addNode, addEdge (or any other modifications)  <Check rep for all modification functions>
- Writeup: 5/5
  - Good

**Problem 5 - Test Driver:** 5/5
- Good

**Turnin:** 2/2

