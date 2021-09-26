package com.example.myapplication

fun main(args: Array<String>) {
    val graph = arrayOf(
        intArrayOf(0, 4, 0, 0, 0, 0, 0, 8, 0),
        intArrayOf(4, 0, 8, 0, 0, 0, 0, 11, 0),
        intArrayOf(0, 8, 0, 7, 0, 4, 0, 0, 2),
        intArrayOf(0, 0, 7, 0, 9, 14, 0, 0, 0),
        intArrayOf(0, 0, 0, 9, 0, 10, 0, 0, 0),
        intArrayOf(0, 0, 4, 14, 10, 0, 2, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 2, 0, 1, 6),
        intArrayOf(8, 11, 0, 0, 0, 0, 1, 0, 7),
        intArrayOf(0, 0, 2, 0, 0, 0, 6, 7, 0)
    )
    val i = ShortestPath()
    i.dijkstra(graph, 0)
    println("all my distance")
    println(i.distanceList)
    println("all the path")
    i.path.forEach {
        println(it)
    }

    var minimum = Int.MAX_VALUE
    var minimumIndex = -1
    i.distanceList.forEachIndexed { index, distance ->
        if (distance < minimum) {
            minimum = distance
            minimumIndex = index
        }
    }
}


internal class ShortestPath {

    var distanceList = mutableListOf<Int>()
    var path = mutableListOf(mutableListOf<Int>())


    private fun getMinimumIndex(distanceList: MutableList<Int>, isSure: MutableList<Boolean>): Int {
        var minimum = Int.MAX_VALUE
        var minimumIndex = -1
        distanceList.forEachIndexed { index, distance ->
            if (distance < minimum && !isSure[index]) {
                minimum = distance
                minimumIndex = index
            }
        }
        return minimumIndex
    }

    fun dijkstra(graph: Array<IntArray>, source: Int, dest :Int = -1): MutableList<Int> {
        val size = graph.size
        val isSure = MutableList(size) { false }
        path = MutableList(size) { mutableListOf(source) }
        distanceList = MutableList(size) { Int.MAX_VALUE }
        distanceList[0] = source
        for (i in 0 until size) {
            val minimumIndex = getMinimumIndex(distanceList, isSure)
            isSure[minimumIndex] = true
            if (minimumIndex == dest)
                return path[minimumIndex]
            distanceList.forEachIndexed { index, distance ->
                if (!isSure[index] && graph[minimumIndex][index] != 0 && (distanceList[minimumIndex] + graph[minimumIndex][index] < distance)) {
                    distanceList[index] = distanceList[minimumIndex] + graph[minimumIndex][index]
                    path[index] = (path[minimumIndex] + index).toMutableList()
                }
            }
        }
        return mutableListOf()
    }

}