package com.kiddos.nerdvocabs

import android.support.test.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class WordDataHelperTest {
    lateinit var helper : WordDataHelper

    @Before
    fun setUp() {
        helper = WordDataHelper(InstrumentationRegistry.getTargetContext())
    }

    @After
    fun tearDown() {
        helper.deleteAll()
    }

    @Test
    fun testAddWord() {
        val word = Word()
        word.word = "endeavor"
        word.wordType = "verb"
        word.definition = "try hard to do or achieve something"
        word.sentence = "In an endeavor to ensure privacy, we'll meet your requirements."

        helper.addWord(word.word, word.wordType, word.definition, word.sentence)

        val words = helper.getWords()
        assertEquals(words.size, 1)

        val w = words[0]
        assertEquals(w.word, word.word)
        assertEquals(w.wordType, word.wordType)
        assertEquals(w.definition, word.definition)
        assertEquals(w.sentence, word.sentence)
    }

    @Test
    fun testAddSameWord() {
        val word = Word()
        word.word = "endeavor"
        word.wordType = "verb"
        word.definition = "try hard to do or achieve something"
        word.sentence = "In an endeavor to ensure privacy, we'll meet your requirements."

        helper.addWord(word.word, word.wordType, word.definition, word.sentence)
        helper.addWord(word.word, word.wordType, word.definition, word.sentence)

        val words = helper.getWords()
        assertEquals(words.size, 1)

        val w = words[0]
        assertEquals(w.word, word.word)
        assertEquals(w.wordType, word.wordType)
        assertEquals(w.definition, word.definition)
        assertEquals(w.sentence, word.sentence)
    }

    @Test
    fun testAdd2Words() {
        val w1 = Word()
        w1.word = "endeavor"
        w1.wordType = "verb"
        w1.definition = "try hard to do or achieve something"
        w1.sentence = "In an endeavor to ensure privacy, we'll meet your requirements."

        val w2 = Word()
        w2.word = "vilify"
        w2.wordType = "verb"
        w2.definition = "speak or write about in an abusively disparaging manner"
        w2.sentence =
                "Why are people so quick to vilify those on the \"other side\" of the issue—and why do we even think in terms of sides?"

        helper.addWord(w1)
        helper.addWord(w2)

        val words = helper.getWords()
        assertEquals(words.size, 2)

        var w = words[0]
        assertEquals(w.word, w1.word)
        assertEquals(w.wordType, w1.wordType)
        assertEquals(w.definition, w1.definition)
        assertEquals(w.sentence, w1.sentence)

        w = words[1]
        assertEquals(w.word, w2.word)
        assertEquals(w.wordType, w2.wordType)
        assertEquals(w.definition, w2.definition)
        assertEquals(w.sentence, w2.sentence)
    }

    @Test
    fun testLoadAssets() {
        val words = helper.loadFromAssets()
        assertNotEquals(words.size, 0)
    }
}