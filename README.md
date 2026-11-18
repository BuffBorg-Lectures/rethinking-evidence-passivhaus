# RETHINKING EVIDENCE IN SCALA 3

A pilot project and companion codebase for the lecture **[Rethinking Evidence in Scala 3: Concepts, Mechanisms, and Real‑World Engineering Impact]()**.

## 📖 Overview

This repository demonstrates how to leverage **compile-time evidence** in Scala 3 within a real-world domain, **a builder engine for Passive Houses**. 

By mostly using validation at compile-time, this project explores how type-level evidence could improve:
* **Code Safety and Security**: Enforcing domain rules and architectural constraints by design.
* **Developer Productivity**: Catching invalid configurations immediately in the IDE or during compilation rather than in production.
* **Codebase Maintainability**: Replacing boilerplate runtime checks with expressive type-level guarantees.

## 🔍 What You Will Explore

Inside this codebase, you will see how the concept of *evidence* can be applied at the application level to achieve the same safety *guarantees* through different Scala 3 mechanisms:

### 1. Canonical Evidence
* **Equality Evidence (`=:=`)**: The standard, widely recognized approach to proving type relationships at compile time.

### 2. Alternative Type-Level Mechanisms
* **`[ClassTag]()`**: Runtime type retention.
* **`[Mirror.Of]()`**: Structural guarantees for generic meta-programming.
* **`[ValueOf]()`**: Literal/singleton type evidence.
* **[GADTs]() and sealed hierarchies**: Compiler‑driven type refinement in pattern matching.

## 🚀 Beyond This Repo

While this pilot project focuses on the foundational and alternative mechanisms from the Scala standard library listed above, the accompanying lecture takes this journey further. In the context of widely adopted ecosystem libraries like Shapeless 3, Tapir and ZIO, we will also explore the usage of advanced metaprogramming techniques for generating evidence:

* **`[inline](https://docs.scala-lang.org/scala3/guides/macros/inline.html)` definitions**
* **`[summon](https://docs.scala-lang.org/scala3/guides/macros/compiletime.html#summoning)` based methods**
* **[Macros](https://docs.scala-lang.org/scala3/guides/macros/macros.html) with [Quotes (`'{...}`) and Splices (`${...}`)](https://docs.scala-lang.org/scala3/guides/macros/quotes.html)**

[WELCOME](https://c.tenor.com/9R40vVZvvtoAAAAC/tenor.gif)