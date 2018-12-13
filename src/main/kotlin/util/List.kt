package util

fun <E> List<E>.getNext(e: E): E {
    return this[(this.indexOf(e) + 1) % this.size]
}


fun <E> List<E>.getPrev(e: E): E {
    return this[(this.indexOf(e) - 1 + this.size) % this.size]
}