---
marp: true
paginate: true
footer: Marwan Azuz -- 29/06/23
---

# Welcome!
# Implementing Scala-CLI on Scastie
## Semester bachelor project at the Scala-Center

> Please try to avoid printing this document, use the online version for links and saving trees 🌳

---

# What is [Scastie](https://scastie.scala-lang.org/)?

* A demo is worth a thousand words…

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

## 1. A first <!-- joke --> ~~stupid~~ <!-- joke end --> idea 

![](../scli_1.svg)

* The issue?
→ Compilation errors are not machine readable. Hard to handle them and forward them nicely to the users.
→ Might create some obvious issues with my colleague…

---

## Previous implementation with SBT

:warning: The runner was **parsing the process' output**! Crazy people… I'm lazy.

---

## 2. Let's start from scratch and do it properly

__Idea__: How do IDEs talk to compilers?

__Solution__: A protocol?

<!-- joke -->

---

# Is there something like this already? Someone must have thought of it.

Open-source software people are crazy.

<!-- joke end -->


---

The [Build Server Protocol](https://build-server-protocol.github.io/) comes to the rescue!

> The Build Server Protocol (BSP) provides endpoints for IDEs and build tools to communicate about directory layouts, external dependencies, compile, test and more.

Or to be short:

> Talk to compilers.

---

## ![width:50](https://build-server-protocol.github.io/img/bsp-logo.svg) Let's do it properly with the Build Server Protocol  

It's nice!

---

# BSP

A protocol to talk to compilers. Inspired by the Language Server Protocol.

* Did someone also made a library for this? Of course!

<!-- joke -->
---

# Why BSP?

* Because Jędrzej (❤️) told me so…

---

# Please stop joking, it's a serious presentation.

<!-- joke end -->

---

# Why BSP?

* Trigger compilations (`buildTarget/compile`)
* Trigger runs (`buildTarget/run`)
* Get diagnostics such as compilation errors and warnings (notifications on `build/publishDiagnotics`)

→ Looks perfect!

---

# What's more? Extensions! 💕

* If the compiler has specific capabilities, some endpoints are defined. In the case of Scala and this project, the endpoint `buildTarget/scalaMainClasses` was useful.
* Why? Wait for a bit… Everything in its time!

---

Side note: Futures are great in Scala.

---

# How does the actual code looks like? 1/2

```scala
    for (
      r <- reloadWorkspace;
      buildTarget <- getBuildTargetId;

      // Compile
      compilationResult <- withShortCircuit(buildTarget, target => compile(id, target));

      // Get main class
      // Note: it is combined to compilationResult so if compilationResult fails,
      // then we do not continue
      mainClass <- withShortCircuit[(BuildTargetIdentifier, CompileResult), ScalaMainClass](
        combineEither(buildTarget, compilationResult),
        {
          case ((tId: BuildTargetIdentifier, _)) => getMainClass(tId)
        }
      );

      // …
```

---

# How does the actual code looks like? 2/2

```scala
      // Get JvmRunEnv
      jvmRunEnv <- withShortCircuit[(BuildTargetIdentifier, ScalaMainClass), JvmEnvironmentItem](
        combineEither(buildTarget, mainClass),
        {
          case ((tId: BuildTargetIdentifier, _)) => getJvmRunEnvironment(tId)
        }
      )
```

**Why?!** Everything in its time… 😉 Trust me.

---

# Instrumentation? With which magic we end-up with this?

![](../scastie_instrumentation.png)

---

# Instrumentation walk-through 1/3

Suppose that we execute 
```scala
1
```

We are minimalist

---

# Instrumentation walk-through 2/3

🧙 Some magic…

:warning: The shown result will be a beautified one. Refer to the report for details.

---

# Instrumentation walk-through 3/3

Result:

```scala
import com.olegych.scastie.api.runtime._

object Main {
    def suppressUnusedWarnsScastie = Html
    val playground = Playground
    def main(args: Array[String]): Unit = {
        playground.main(Array())
        scala.Predef.println(<instrumentations result>)
    }
}

object Playground extends ScastieApp {
    private val instrumentationMap$ = Map[Position, Render].empty
    def instrumentations$ = instrumentationMap$.toList.map {
        case (pos, r) => Instrumentation(pos, r)
    }

    locally {
        val $t = 1
        instrumentationMap$(Position(0, 1)) = render($t)
        $t
    }
}
```

---

# Remarks on the instrumentation

* We have new objects
* Comments will end-up in the `object Playground`.
  - if comments ends up here, directives too! We have to put them at the top of the file.

---

# How to do it?

Fairly easy thanks to the Scala standard library.

```scala

```