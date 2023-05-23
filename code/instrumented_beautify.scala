import com.olegych.scastie.api.runtime._

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