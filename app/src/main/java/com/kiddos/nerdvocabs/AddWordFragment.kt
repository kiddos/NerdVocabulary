package com.kiddos.nerdvocabs


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*


/**
 * This [Fragment] will add words into sqlite
 *
 */
class AddWordFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_add_word, container, false)
        val helper = WordDataHelper(context!!)

        val add = rootView.findViewById<Button>(R.id.btnAddWord)
        add.setOnClickListener {
            val word = rootView.findViewById<EditText>(R.id.etWord)
            val wordTypeChoice = rootView.findViewById<Spinner>(R.id.spWordType)
            val wordType = wordTypeChoice.selectedItem as String
            val wordDefinition = rootView.findViewById<EditText>(R.id.etWordDefinition)
            val sentence = rootView.findViewById<EditText>(R.id.etWordInSentence)

            Log.d("AddWord", "${word.text}, " +
                    "$wordType, ${wordDefinition.text}, $sentence")
            if (word.text.length >= 2 && wordDefinition.text.isNotEmpty()) {
                helper.addWord(word.text.toString(), wordType, wordDefinition.text.toString(),
                    sentence.text.toString())

                word.text.clear()
                wordDefinition.text.clear()
                sentence.text.clear()
                Toast.makeText(context!!, getString(R.string.prompt_add_word_success), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context!!, getString(R.string.prompt_add_word_fail), Toast.LENGTH_SHORT).show()
            }
        }
        return rootView
    }
}
