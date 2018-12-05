package util


open class DefaultMap<K, V>(val delegate: MutableMap<K, V>, val defaultValue: V) : MutableMap<K, V> by delegate {
    override fun get(key: K): V {
        return delegate.getOrDefault(key, defaultValue)
    }
}

class CountMap<K> : DefaultMap<K, Int>(hashMapOf(), 0) {
    fun inc(key: K) {
        delegate[key] = this[key] + 1
    }
}