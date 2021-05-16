package ch.guengel.keyvaluestore

import assertk.assertThat
import assertk.assertions.*
import com.fasterxml.jackson.databind.JsonMappingException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class KeyValueStoreTest {
    private lateinit var keyValueStore: KeyValueStore

    @BeforeEach
    fun setUp() {
        keyValueStore = KeyValueStore()
    }

    @Test
    fun `should return null on non-existing key`() {
        val value: Any? = keyValueStore.get("must-not-exist")
        assertThat(value)
            .isNull()
    }

    @Test
    fun `should die on type mismatch`() {
        keyValueStore.put("test-value", "string")

        assertThat { keyValueStore.get<Number?>("test-value") }
            .isFailure()
            .isInstanceOf(JsonMappingException::class.java)
    }

    @Test
    fun `should store null`() {
        keyValueStore.put("test-value", null)

        val value: Int? = keyValueStore.get("test-value")
        assertThat(value)
            .isNull()
    }

    @Test
    fun `should store string`() {
        keyValueStore.put("test-string", "string")

        val value: String? = keyValueStore.get("test-string")
        assertThat(value)
            .isNotNull()
            .isEqualTo("string")
    }

    @Test
    fun `should store double`() {
        keyValueStore.put("test-double", 42.42)

        val value: Double? = keyValueStore.get("test-double")
        assertThat(value)
            .isNotNull()
            .isEqualTo(42.42)
    }

    @Test
    fun `should store list`() {
        keyValueStore.put("test-list", listOf("a", "b", "3"))

        val value: List<String>? = keyValueStore.get("test-list")
        assertThat(value)
            .isNotNull()
            .isEqualTo(listOf("a", "b", "3"))
    }

    @Test
    fun `should store object`() {
        data class TestClass(val str: String, val num: Int, val dec: Double)

        val testValue = TestClass("answer", 42, 42.0)
        keyValueStore.put("test-object", testValue)

        val actualValue: TestClass? = keyValueStore.get("test-object")
        assertThat(actualValue)
            .isNotNull()
            .isEqualTo(testValue)
    }

    @Test
    fun `should handle any object`() {
        data class TestClass(val str: String, val num: Int, val dec: Double)

        val testValue = TestClass("answer", 42, 42.0)
        keyValueStore.put("test-any", testValue)

        val actualValue: Any? = keyValueStore.get("test-any")

        @Suppress("UNCHECKED_CAST")
        val hashMap = actualValue as LinkedHashMap<String,Any>
        assertThat(actualValue["str"]).isEqualTo("answer")
        assertThat(actualValue["num"]).isEqualTo(42)
        assertThat(actualValue["dec"]).isEqualTo(42.0)
    }

    @Test
    fun `should remove key and value`() {
         keyValueStore.put("test-string", "string")

        val value: String? = keyValueStore.get("test-string")
        assertThat(value)
            .isNotNull()
            .isEqualTo("string")

        keyValueStore.delete("test-string")

        val deletedValue: String? = keyValueStore.get("test-string")
        assertThat(deletedValue)
            .isNull()
    }


}
