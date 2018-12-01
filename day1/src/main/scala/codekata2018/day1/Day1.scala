package codekata2018.day1

import scala.annotation.tailrec
import scala.collection.immutable.Stream.Empty

object Day1 {
  // Convert the input into a sequence of signed integers by splitting on newlines, trimming whitespace,
  // and removing empty entries.
  val inputLines = Day1.in.split("\n").map(_.replaceAll(" ", "")).filter(_.nonEmpty).map(_.toInt)

  object Part1 {
    // Duh.  Just sum the integers
    val solution = inputLines.sum
  }

  object Part2 {
    // Solve this tail recursively (FPers <3 tail recursion).  At each step, if we've run out of stuff, then there's
    // no solution (this will never happen).  Otherwise...
    @tailrec
    def recAdjustFreq(freqs: Stream[Int], curFreq: Int, seen: Set[Int]): Option[Int] = freqs match {
      case Empty => None  // Should never happen, since we're streaming forever
      // Pattern match the non-empty stream
      case h #:: t =>
        // ... look at the head element from the lazily-evaluated stream.  If the current frequency plus the
        // delta (head) has already been seen, then we're done...
        if (seen(h + curFreq)) Option(h + curFreq)
          // ...otherwise, recursively call ourselves with the rest (tail) of the stream, adding the new frequency
          // to the set of frequencies we've already seen.
        else recAdjustFreq(t, h + curFreq, seen + (h + curFreq))
    }

    val solution = recAdjustFreq(
      // Part two indicates that we loop over the input repeadly once we hit the end.
      Stream.continually(inputLines.toStream).flatten,  // Treat input as a stream, then loop it (flatten the nesting)
      0,  // Starting frequency
      Set.empty)  // Starting set of "seen frequencies"
  }

  lazy val in: String =
    // Puzzle input goes here.
    """
    """.stripMargin
}
