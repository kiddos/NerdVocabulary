package com.kiddos.nerdvocabs

import android.content.ContentValues
import android.content.Context
import android.content.res.AssetManager
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import java.util.Random

class Word {
    var wordId: Long = 0
    var word: String = ""
    var wordType: String = ""
    var definition: String = ""
    var sentence: String? = null
}

class Question {
    var word: String = ""
    var answer: Int = -1
    var choices: ArrayList<String> = ArrayList()
}

class WordDataHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object WordEntry : BaseColumns {
        const val TABLE_NAME = "words"
        const val COLUMN_NAME_WORD = "word"
        const val COLUMN_NAME_WORD_TYPE = "type"
        const val COLUMN_NAME_WORD_DEFINITION = "definition"
        const val COLUMN_NAME_WORD_IN_SENTENCE = "sentence"

        const val DATABASE_NAME = "nerd_vocabs.db"
        const val DATABASE_VERSION = 1
    }

    private val SQL_CREATE_TABLE =
        "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "$COLUMN_NAME_WORD TEXT NOT NULL," +
                "$COLUMN_NAME_WORD_TYPE TEXT NOT NULL," +
                "$COLUMN_NAME_WORD_DEFINITION TEXT NOT NULL," +
                "$COLUMN_NAME_WORD_IN_SENTENCE TEXT)"

    private val SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS $TABLE_NAME"

    private var assetManager: AssetManager  = context.assets
    private val rand: Random = Random(System.currentTimeMillis())

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        db.execSQL(SQL_CREATE_TABLE)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        db.execSQL(SQL_CREATE_TABLE)
    }

    fun deleteAll() {
        this.writableDatabase.execSQL(SQL_DELETE_ENTRIES)
        this.createTable()
    }

    fun createTable() {
        this.writableDatabase.execSQL(SQL_CREATE_TABLE)
    }

    fun addWord(word: Word) {
        addWord(word.word, word.wordType, word.definition, word.sentence)
    }

    fun addWord(word: String, type: String, definition: String, sentence: String?) : Long? {
        // find the word
        // if exists
        // replace
        // else add the new word
        var db = this.readableDatabase
        var projection = arrayOf(BaseColumns._ID, COLUMN_NAME_WORD)
        val selection = "$COLUMN_NAME_WORD = ?"
        val selectionArgs = arrayOf(word)
        var cursor = db.query(TABLE_NAME,
            projection,
            selection,
            selectionArgs, null, null, null)

        db = this.writableDatabase
        if (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID))
            cursor.close()
            val entry = ContentValues().apply {
                put(COLUMN_NAME_WORD_TYPE, type)
                put(COLUMN_NAME_WORD_DEFINITION, definition)
                put(COLUMN_NAME_WORD_IN_SENTENCE, sentence)
            }
            db?.update(TABLE_NAME, entry, selection, selectionArgs)
            return id
        } else {
            cursor.close()

            val entry = ContentValues().apply {
                put(COLUMN_NAME_WORD, word)
                put(COLUMN_NAME_WORD_TYPE, type)
                put(COLUMN_NAME_WORD_DEFINITION, definition)
                put(COLUMN_NAME_WORD_IN_SENTENCE, sentence)
            }
            return db?.insert(TABLE_NAME, null, entry)
        }
    }

    fun getWords(): ArrayList<Word> {
        val db = this.readableDatabase
        val projection = arrayOf(BaseColumns._ID, COLUMN_NAME_WORD,
            COLUMN_NAME_WORD_TYPE, COLUMN_NAME_WORD_DEFINITION, COLUMN_NAME_WORD_IN_SENTENCE)
        var cursor = db.query(TABLE_NAME, projection, null, null, null, null, null)


        val words = ArrayList<Word>()
        with (cursor) {
            while (moveToNext()) {
                val w = Word()
                w.wordId = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                w.word = getString(getColumnIndexOrThrow(COLUMN_NAME_WORD))
                w.wordType = getString(getColumnIndexOrThrow(COLUMN_NAME_WORD_TYPE))
                w.definition = getString(getColumnIndexOrThrow(COLUMN_NAME_WORD_DEFINITION))
                w.sentence = getString(getColumnIndexOrThrow(COLUMN_NAME_WORD_IN_SENTENCE))
                words.add(w)
            }
        }
        return words
    }

    fun loadFromAsset(asset: String) : ArrayList<Word> {
        val inputs = this.assetManager.open(asset)
        val reader = inputs.bufferedReader()
        val lines = reader.readLines()
        var content = lines.joinToString("\n")

        val entries = content.split("\n\n")
        val words = ArrayList<Word>()
        var id = 1L
        for (e in entries) {

            val wordData = e.split("\n")
            val w = Word()
            w.wordId = id;
            id += 1;
            w.word = wordData[0]
            w.wordType = wordData[1]
            w.definition = wordData[2]
            if (wordData.size > 3) {
                w.sentence = wordData[3]
            }
            words.add(w)
        }
        return words
    }

    fun loadFromAssets(): ArrayList<Word> {
        val outputs = ArrayList<Word>()
        val assets = arrayOf("gre01.txt", "toefl01.txt")
        for (a : String in assets) {
            val words = loadFromAsset(a)
            outputs.addAll(words)
        }
        return outputs
    }

    fun getQuestion(words : MutableList<Word>): Question? {
        if (words.size > 3) {
            val wordIndex = rand.nextInt(words.size)
            val q = Question()
            q.word = words[wordIndex].word

            val added = ArrayList<Int>()
            added.add(wordIndex)
            for (i in 0..2) {
                var choiceIndex = rand.nextInt(words.size);
                while (added.contains(choiceIndex))
                    choiceIndex = rand.nextInt(words.size);

                q.choices.add(words[choiceIndex].definition)
                added.add(choiceIndex)
            }
            q.answer = rand.nextInt(4)
            q.choices.add(q.answer, words[wordIndex].definition)

            return q
        }
        return null
    }
}