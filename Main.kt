package sorting

import java.io.File
import java.util.*

var sc = Scanner(System.`in`)
var sortType = "natural"
val sTypes = "natural,byCount"
var dataType = "word"
val dTypes = "long,line,word"
val cmds = "-sortingType, -dataType, -inputFile, -outputFile"
var dataName = ""
var inputFile = ""
var outputFile = ""

fun main(args: Array<String>) {
    args.filter { it[0] == '-' }.forEach { if (!cmds.contains(it))
        println("\"$it\" is not a valid parameter. It will be skipped.")
    }
    for (i in 0..args.lastIndex) {
        if (args[i] == "-inputFile") inputFile = args[i + 1]
        if (args[i] == "-outputFile") outputFile = args[i + 1]
        if (args[i] == "-sortingType")
            if (i != args.lastIndex && sTypes.contains(args[i+1])) {
                sortType = args[i+1]
            } else {
                println("No sorting type defined!")
                return
            }
        if (args[i] == "-dataType")
            if (i != args.lastIndex && dTypes.contains(args[i+1])) {
                dataType = args[i+1]
            } else {
                println("No data type defined!")
                return
            }
    }
    dataName = when(dataType) {
        "long" -> "numbers"
        "word" -> "words"
        else -> "lines"
    }

    if (inputFile.isNotBlank()) sc = Scanner(File(inputFile))
    if (outputFile.isNotBlank()) File(outputFile).writeText("")
    when (dataType) {
        "long" -> readNums()
        "word" -> readWords()
        "line" -> readLines()
    }
}

fun readNums() {
    val list = mutableListOf<Int>()
    while (sc.hasNext()) {
        val num = sc.next()
        if (num.matches("-?\\d+".toRegex())) {
            list.add(num.toInt())
        } else {
            println("$num is not a long. It will be skipped.")
        }
    }
    list.sort()
    sortData(list)
}

fun readWords() {
    val list = mutableListOf<String>()
    while (sc.hasNext()) {
        val ln = sc.next()
        list.add(ln)
    }
    list.sort()
    sortData(list)
}

fun readLines() {
    val list = mutableListOf<String>()
    while (sc.hasNextLine()) {
        val ln = sc.nextLine()
        list.add(ln)
    }
    list.sort()
    sortData(list)
}

fun <T> sortByCount(list: List<T>) {
    val total = list.size
    fprint("Total $dataName: $total.")
    val lset = mutableMapOf<T, Int>()
    for (l in list) {
        if (l !in lset) {
            val n = list.count { it == l }
            val p = n * 100 / total
            lset.put(l, n)
        }
    }
    lset.toList().sortedBy { (_, value) -> value }.forEach {
        fprint("${it.first}: ${it.second} time(s), ${it.second * 100 / total}%")
    }
}

fun <T> sortData(list: List<T>) {
    if (sortType == "byCount") {
        sortByCount(list)
    } else {
        val cnt = list.count()
        fprint("Total $dataName: ${list.size}")
        if (dataType == "line") {
            fprint("Sorted data:")
            list.forEach { fprint(it.toString()) }
        } else {
            fprint("Sorted data: ${list.joinToString(" ")}")
        }
    }
}

fun fprint(text: String) {
    if (outputFile.isNotBlank()) {
        File(outputFile).appendText(text)
    } else println(text)
}