import java.util.LinkedList

fun main() {
  val slopeDir = mutableMapOf(
    '.' to listOf(0 to -1, 0 to 1, -1 to 0, 1 to 0),
    '>' to listOf(0 to 1),
    '<' to listOf(0 to -1),
    '^' to listOf(-1 to 0),
    'v' to listOf(1 to 0)
  )

  data class Node(
    val id: Int,
    val adjMap: MutableMap<Int, Int>,
  )
  data class Graph(
    var nodes: List<Node>
  )

  fun Graph.compress() {

    while (true) {
      val nodesMap = this.nodes.associateBy { it.id }.toMutableMap()
      var flag = false

      for(n in this.nodes){
        val adj = n.adjMap
        if (adj.size != 2) continue
        if (n.id !in nodesMap) continue

        val (k1, v1) = adj.entries.elementAt(0)
        val (k2, v2) = adj.entries.elementAt(1)

        // k1 -> now -> k2
        val n1 = nodesMap[k1]!!
        if (n.id in n1.adjMap) {
          flag = true
          n1.adjMap.remove(n.id)
          n1.adjMap[k2] = v1 + v2
        }

        // k2 -> now -> k1
        val n2 = nodesMap[k2]!!
        if (n.id in n2.adjMap) {
          flag = true
          n2.adjMap.remove(n.id)
          n2.adjMap[k1] = v1 + v2
        }

        nodesMap.remove(n.id)
      }

      this.nodes = nodesMap.values.toList()
      if (!flag) break
    }

  }

  fun List<String>.toGraph(withSlope: Boolean = true): Graph {
    val nodes = mutableListOf<Node>()

    for (i in this.indices) {
      for (j in this[0].indices) {
        if (this[i][j] == '#') continue
        val key = i * 1000 + j
        val node = Node(key, mutableMapOf())
        nodes.add(node)

        val nextDir = if (withSlope) slopeDir[this[i][j]]!! else slopeDir['.']!!
        for ((dx, dy) in nextDir) {
          val nx = i + dx
          val ny = j + dy
          val nKey = nx * 1000 + ny

          if (nx < 0 || nx >= this.size || ny < 0 || ny >= this[0].length) continue
          if (this[nx][ny] == '#') continue

          node.adjMap[nKey] = 1
        }
      }
    }

    val graph = Graph(nodes)
    graph.compress()

    return graph
  }

  fun List<String>.toStartIdAndEndId(): Pair<Int, Int> {
    var start = 0
    var end = 0

    for (j in this[0].indices) {
      if (this[0][j] == '.') start = 0 * 1000 + j
    }

    for (j in this[0].indices) {
      if (this[this.size - 1][j] == '.') end = (this.size - 1) * 1000 + j
    }

    return start to end
  }

  fun Graph.dfs(startId: Int, endId: Int): Int {
    var ans = 0
    val nodesMap = this.nodes.associateBy { it.id }

    fun doDfs(id: Int, seen: MutableSet<Int>, dist: Int) {
      if (id == endId) {
        ans = maxOf(ans, dist)
        return
      }

      val curNode = nodesMap[id]!!
      for ((nId, v) in curNode.adjMap) {
        if (nId in seen) continue

        seen.add(nId)
        doDfs(nId, seen, dist + v)
        seen.remove(nId)
      }
    }

    doDfs(startId, mutableSetOf(startId), 0)
    return ans
  }
  fun part1(input: List<String>): Int {
    val graph = input.toGraph()
    val (startId, endId) = input.toStartIdAndEndId()
    return graph.dfs(startId, endId)
  }

  fun part2(input: List<String>): Int {
    val graph = input.toGraph(false)
    val (startId, endId) = input.toStartIdAndEndId()
    return graph.dfs(startId, endId)
  }

  val testInput = readInput("Day23_test")
  check(part1(testInput) == 94)
  check(part2(testInput) == 154)

  val input = readInput("Day23")
  part1(input).println()
  part2(input).println()
}
