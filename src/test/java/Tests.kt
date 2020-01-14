import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.concurrent.ConcurrentSkipListSet
import kotlin.Comparator

class TaskTestsKotlin {
    @Test
    fun testInsert() {
        val skipList = SkipList(IntComparator(), 10)
        skipList.insert(0)
        skipList.insert(2)
        skipList.insert(5)
        print(skipList.toString())
        val concurrent = ConcurrentSkipListSet<Int>()
        concurrent.add(0)
        concurrent.add(2)
        concurrent.add(5)
        Assertions.assertTrue(
                skipList.contains(0) &&
                        skipList.contains(2) &&
                        skipList.contains(5))
        val actual = setActual(skipList)
        Assertions.assertArrayEquals(concurrent.toIntArray(), actual)
    }

    @Test
    fun testIterator() {
        var skipList = SkipList(IntComparator(), 5)
        skipList.insert(0)
        skipList.insert(1)
        skipList.insert(3)
        skipList.insert(6)
        skipList.insert(8)
        skipList.insert(9)
        var expected = arrayOf(0, 1, 3, 6, 8).toIntArray()
        var actual = setActual(skipList)
        Assertions.assertArrayEquals(expected, actual)
        skipList = SkipList(IntComparator(), 2)
        skipList.insert(3)
        skipList.insert(2)
        skipList.insert(5)
        skipList.insert(0)
        expected = arrayOf(0, 2).toIntArray()
        actual = setActual(skipList)
        Assertions.assertArrayEquals(expected, actual)
    }

    private fun setActual(skipList: SkipList<Int>): IntArray {
        val it = skipList.iterator()
        val actual = mutableListOf<Int>()
        while (it.hasNext()) {
            actual.add(it.next())
        }
        return actual.toIntArray()
    }

    class IntComparator : Comparator<Int> {
        override fun compare(o1: Int?, o2: Int?): Int {
            return o1!!.compareTo(o2!!)
        }

    }

}