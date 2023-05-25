---
marp: true
paginate: true
footer: Marwan Azuz -- 29/06/23
---

# Welcome!
# Implementing Scala-CLI on Scastie
## Semester bachelor project at the Scala-Center

---

# What is [Scastie](https://scastie.scala-lang.org/)?

* A demo is better than a long paragraph…

---

# How Scastie works?

![](../architecture.svg)

<!-- Quick overview -->

--- 

# Steps

1. Create the Scala-CLI runner
2. Create UI components for Scala-CLI
3. Make directives work with Metals

![](./toc_presentation.svg)

---

# 1. The Scala-CLI Runner

![](./zoom_sbt_runner.svg)

---

## 1. A first ~~stupid~~ idea

![](../scli_1.svg)

* The issue?
→ Compilation errors are not machine readable. Hard to handle them and forward them nicely to the users.

---

## Previous implementation with SBT behavior

:warning: The runner was **parsing the process' output**! Crazy people… I'm lazy.

---

## 2. Let's start from scratch and do it properly

__Idea__: How do IDEs talk to compilers?

__Solution__: A protocol?

---

# Is there something like this already? Someone must have thought of it.

OSS people are crazy.



---

The [Build Server Protocol](https://build-server-protocol.github.io/) comes to the rescue!

> The Build Server Protocol (BSP) provides endpoints for IDEs and build tools to communicate about directory layouts, external dependencies, compile, test and more.

Or to be short:

> Talk to compilers.

---

## ![width:50](https://build-server-protocol.github.io/img/bsp-logo.svg)  Let's do it properly with the Build Server Protocol  

It's nice!

---

# BSP

A protocol to talk to compilers. Inspired by the Language Server Protocol.

* Did someone also made a library for this? Of course!

---

# BSP 2


