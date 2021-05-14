package ch.guengel.keyvaluestore

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlin.reflect.KClass

class KeyValueStore {
    private val store : MutableMap<String, String> = mutableMapOf()
    private val mapper = ObjectMapper().registerKotlinModule()

    fun <T> put(key: String, value: T) {
        store[key] = mapper.writeValueAsString(value)
    }

    fun <T> get(key: String, clazz: Class<T>) : T? = store[key]?.let {
        mapper.readValue(it, clazz)
    }

    fun delete(key: String) {
        store.remove(key)
    }

    inline fun <reified T> get(key: String) : T? = get(key,T::class.java)
}
