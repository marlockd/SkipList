import java.lang.StringBuilder
import java.util.*

class SkipList<T>(private val comparator: Comparator<T>, private val maxHeight: Int) {
    var curHeight: Int = 1
    val head: Node<T> = Node(null, maxHeight)
    private val rnd: Random = Random(System.currentTimeMillis())

    fun insert(value: T) {
        val prev: Array<Node<T>?> = arrayOfNulls(maxHeight)
        var x = findGreaterOrEqual(value, prev)

        assert(x == null || comparator.compare(x.value, value) >= 0)

        val height = randomHeight()
        if (height > curHeight) {
            for (i in curHeight until height) {
                prev[i] = head
            }
            curHeight = height
        }

        x = Node(value, height)
        for (i in 0 until height) {
            x.next[i] = prev[i]!!.next[i]
            prev[i]!!.next[i] = x
        }
    }

    fun contains(value: T): Boolean {
        val x = findGreaterOrEqual(value, null)
        if (x != null && x.value == value) {
            return true
        }
        return false
    }

    fun size(): Int{
        return curHeight
    }

    fun iterator() = SkipListIterator(maxHeight, head)

    class SkipListIterator<T>(private val maxHeight: Int,  head: Node<T>): Iterator<T>{
        var current: Node<T>? = head
        var curHeight = 0
        override fun hasNext(): Boolean {
            if (current!!.next.isEmpty() || curHeight >= maxHeight) return false
            for (node in current!!.next){
                if (node != null) return true
            }
            return false
        }

        override fun next(): T {
            for (node in current!!.next) if (node?.value != null) {
                current = node
                curHeight++
                return current!!.value!!
            }
            throw IndexOutOfBoundsException("has no value")
        }
    }

    private fun randomHeight(): Int {
        val branching = 4
        var level = 1

        while (level < maxHeight && rnd.nextInt() % branching == 0) {
            level++
        }
        return level
    }

    private fun valueIsAfterNode(value: T, node: Node<T>?): Boolean {
        if (node != null) {
            return comparator.compare(value, node.value) > 0
        }
        return false
    }

    private fun findGreaterOrEqual(value: T, prev: Array<Node<T>?>?): Node<T>? {
        var x = head
        var level = curHeight - 1
        while (true) {
            val next = x.next[level]
            if (valueIsAfterNode(value, next)) {
                if (next != null) {
                    x = next
                }
            } else {
                if (prev != null) prev[level] = x
                if (level == 0) {
                    return next
                } else {
                    level--
                }
            }
        }
    }

    private fun findLessThan(value: T): Node<T> {
        var x = head
        var level = curHeight - 1
        while (true) {
            val next = x.next[level]
            if (valueIsAfterNode(value, next!!)) {
                x = next
            } else {
                if (level == 0) {
                    return x
                } else {
                    level--
                }
            }
        }
    }

    private fun findLast(): Node<T> {
        var x = head
        val level = curHeight - 1
        while (true) {
            val next = x.next[level]
            x = next!!
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SkipList<*>

        if (comparator != other.comparator) return false
        if (curHeight != other.curHeight) return false
        if (head != other.head) return false
        if (rnd != other.rnd) return false

        return true
    }

    override fun hashCode(): Int {
        var result = comparator.hashCode()
        result = 31 * result + curHeight
        result = 31 * result + head.hashCode()
        result = 31 * result + rnd.hashCode()
        return result
    }

    override fun toString(): String {
        val sb = StringBuilder()
        var x = head
        val level = curHeight - 1
        var next: Node<T>
        var s = 0
        while (true) {
            sb.append("\n")
            for (i in 0..s) sb.append("   ")
            s++
            sb.append(x.toString())
            if (x.next.size >= level) break
            next = x.next[level]!!
            x = next
        }
        return sb.toString()
    }

    class Node<T>(val value: T?, height: Int) {
        val next: Array<Node<T>?> = arrayOfNulls(height)
        override fun toString(): String {
            return "Node(value=$value, \n\tnext=${next.contentToString()})"
        }

    }

}