import android.os.Build
import androidx.annotation.RequiresApi
import java.lang.Math.pow
import kotlin.math.sqrt
import java.util.*


data class Cell(
    var parent: Pair<Int, Int> = Pair(-1, -1),
    var f: Double = (-1).toDouble(),
    var g: Double = (-1).toDouble(),
    var h: Double = (-1).toDouble()
)

val x = 9
val y = 10
fun isValid(grid : Array<Array<Int>>, src: Pair<Int, Int>): Boolean {
    return (src.first >= 0) && (src.first < x)
            && (src.second >= 0)
            && (src.second < y)

}

fun isUnblocked(grid : Array<Array<Int>>, src: Pair<Int, Int>): Boolean {
    return isValid(grid, src)
            && grid[src.first][src.second] == 1;
}

fun calculateH(src: Pair<Int, Int>, dest: Pair<Int, Int>): Double {
    return sqrt(pow((src.first.toDouble() - dest.first.toDouble()), 2.0)
            + pow((src.second.toDouble() - dest.second.toDouble()), 2.0))
}

fun tracePath(grid: Array<Array<Cell>>, dest: Pair<Int, Int>): Stack<Pair<Int, Int>> {
    val Path = Stack<Pair<Int, Int>>()
    var row = dest.second
    var col = dest.second
    var next_node: Pair<Int, Int> = grid[row][col].parent
    do {
        Path.push(next_node)
        next_node = grid[row][col].parent
        row = next_node.first
        col = next_node.second
    } while (grid[row][col].parent !== next_node)

    Path.add(Pair(row, col))
    val tmp = Path
  // println(Path.size)
    Path.forEachIndexed { index, pair ->
        val p: Pair<Int, Int> = Path[(Path.size-1) - index]
        //tmp.pop()
        print("-> (${p.first},${p.second}) ")
    }
    println("")
    return Path
}

val comparator = object : Comparator<Triple<Double, Int, Int>> {
    override fun compare(p0: Triple<Double, Int, Int>, p1: Triple<Double, Int, Int>): Int {
        if (p0.first != p1.first) {
            return (p0.first - p1.first).toInt()
        } else if (p0.second != p1.second) {
            return (p0.second - p1.second)
        } else {
            return (p0.third - p1.third)
        }
    }
}


fun printPath(grid : Array<Array<Int>>, path: Stack<Pair<Int, Int>>) {
    grid.forEachIndexed { y, ints ->
        ints.forEachIndexed { x, i ->

            if (path.contains(Pair(y, x))) {
                print("9 ")
            } else
                print("$i ")
        }

        print("\n")
    }
}
@RequiresApi(Build.VERSION_CODES.N)
fun AStar(grid : Array<Array<Int>>, src: Pair<Int, Int>, dest: Pair<Int, Int>) {
    val closedList : MutableList<MutableList<Boolean>> = MutableList(x) { MutableList(y) { false } }

    val cell: Array<Array<Cell>> =Array(x) { Array(y) { Cell() } }
    var i = src.first
    var j = src.second
    cell.get(i).get(j).f = 0.0
    cell.get(i).get(j).g = 0.0
    cell.get(i).get(j).h = 0.0
    cell.get(i).get(j).parent = Pair(i, j)



    val openList = PriorityQueue<Triple<Double, Int, Int>>(comparator)
    openList.add(Triple(0.0, i ,j))
    while (!openList.isEmpty()) {
        val p = openList.last()
        i = p.second
        j = p.third
        openList.remove(openList.last())
        closedList[i][j] = true
        for (add_x in -1..1) {
            for (add_y in -1..1) {
                val neighbour = Pair(i +add_x, j +add_y)
                if (isValid(grid, neighbour)) {
                    if (neighbour == dest) {
                        cell[neighbour.first][neighbour.second].parent =
                            Pair(i, j)
                       // println("$i $j $dest")
                        val path = tracePath(cell, dest)
                        printPath(grid, path)
                        return
                    }
                    else if (!closedList[neighbour.first]
                                [neighbour.second]
                        && isUnblocked(grid,
                            neighbour)) {
                        val gNew = cell[i][j].g + 1.0
                        val hNew = calculateH(neighbour,
                            dest)
                        val fNew = gNew + hNew
                        if (cell[neighbour.first][neighbour.second].f == (-1).toDouble() || cell[neighbour.first][neighbour.second].f > fNew
                        ) {
                            openList.add(
                                Triple(fNew, neighbour.first,
                                neighbour.second
                            ))

                            cell[neighbour.first][neighbour.second].g = gNew
                            cell[neighbour.first][neighbour.second].h = hNew
                            cell[neighbour.first][neighbour.second].f = fNew
                            cell[neighbour.first][neighbour.second].parent =
                                Pair(i, j)
                        }

                    }
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.N)
fun main() {
    val grid = arrayOf(
        arrayOf (1, 0, 1, 1, 1, 1, 0, 1, 1, 1),
        arrayOf(1, 1, 1, 0, 1, 1, 1, 0, 1, 1),
        arrayOf(1, 1, 1, 0, 1, 1, 0, 1, 0, 1),
        arrayOf(0, 0, 1, 0, 1, 0, 0, 0, 0, 1),
        arrayOf(1, 1, 1, 0, 1, 1, 1, 0, 1, 0),
        arrayOf(1, 0, 1, 1, 1, 1, 0, 1, 0, 0),
        arrayOf(1, 0, 0, 0, 0, 1, 0, 0, 0, 1),
        arrayOf(1, 0, 1, 1, 1, 1, 0, 1, 1, 1),
        arrayOf(1, 1, 1, 0, 0, 0, 1, 0, 0, 1)
    )
   val src = Pair(8, 0)
    val dest = Pair (0, 0)
    AStar(grid, src, dest)

}