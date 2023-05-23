import _root_.com.olegych.scastie.api.runtime._
object Playground extends ScastieApp { private val instrumentationMap$ = _root_.scala.collection.mutable.Map.empty[_root_.com.olegych.scastie.api.Position, _root_.com.olegych.scastie.api.Render];def instrumentations$ = instrumentationMap$.toList.map{ case (pos, r) => _root_.com.olegych.scastie.api.Instrumentation(pos, r) };
scala.Predef.locally {val $t = 1; instrumentationMap$(_root_.com.olegych.scastie.api.Position(0, 1)) = _root_.com.olegych.scastie.api.runtime.Runtime.render($t);$t}}
