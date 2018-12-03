package codekata2018.day2

import cats._
import cats.syntax.foldable._
import cats.syntax.monoid._
import cats.instances.list._
import cats.instances.map._
import cats.instances.int._
import scala.annotation.tailrec

object Day2 {
  val inputLines = in.split("\n").map(_.replaceAll(" ", "")).filter(_.nonEmpty)

  object Part1 {
    val (twos, threes) = inputLines.map { line =>
      // Turn each line into a map of "char" to "occurances"
      val counts = implicitly[Foldable[List]].foldMap(line.toList){ c => Map(c -> 1) }
      // Map each line into tuple of bools (a char has exactly 2, a char has exactly 3)
      (counts.exists(_._2 == 2), counts.exists(_._2 == 3))
    }
    // Unzip the seq of pairs of bools into a pair of seqs.
    .unzip

    // Part one requires the product of the number of input lines with chars repeated exactly {2, 3} times.
    val solution = twos.filter(identity).size * threes.filter(identity).size
  }

  object Part2 {
    // Each line of input has the same number (L) of characters.  We can create L sets, with each Ith set corresponding
    // to "dropping the Ith character from the input string", and look for collisions upon trying to insert to the set.
    // This will be O(N*L^2) (for N input lines), which is better than the naive O(L^2 * N^2) approach, but still seems
    // shitty to me.
    //
    // A alternate (better?) approach would be to map in 2 passes per line, each line into L perfect hashes, each
    // without the contributions of a particular character index in the hash.  Something like:
    //  lineHash = product{ p(i) ^ charAt(line, i), i } // where p(i) is the iTH prime.
    //  lineHashes = (1 to l) map (lineHash / (p(i) ^ charAt(line, i))).
    // Then, a single set can be used, and a single linear pass over the input, and the first collision on value in
    // the set is the collision we're looking for.
    def primeStream(s: Stream[Int]): Stream[Int] =
      Stream.cons(s.head, primeStream(s.tail filter { _ % s.head != 0 }))
    // A safer way to do this would be to memoize rather than using `take()` and directly indexing the result
    private val primes = primeStream(Stream.from(2)).take(30).toIndexedSeq
    def stringToHash(s: String) = s.zipWithIndex.map { case (c, i) => BigInt(primes(i)).pow(c.toInt) }.product
    def stringToHashes(s: String) = {
      val bigHash = stringToHash(s)
      s.zipWithIndex.map { case (c, i) => bigHash / BigInt(primes(i)).pow(c.toInt) }
    }

    @tailrec
    def recAddString(
        lineHashes: Stream[(String, BigInt)],
        seen: Map[BigInt, String]): Option[(String, String)] = lineHashes match {
      case Stream.Empty => None
      case (str, hash) #:: t =>
        if (seen.contains(hash)) Option((str, seen(hash)))
        else recAddString(t, seen + (hash -> str))
    }

    val stringHashStream =
      inputLines
        .toStream
        .flatMap { l => stringToHashes(l).map { h => (l, h) }.toStream }

    val solution = {
      val result = recAddString(stringHashStream, Map.empty)
      result.map { case (a, b) => a.zip(b).filter(p => p._1 == p._2).map(_._1).mkString("") }
    }
  }

  def in:String =
    """
    """.stripMargin

}
