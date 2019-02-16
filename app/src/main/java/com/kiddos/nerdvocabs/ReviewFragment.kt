package com.kiddos.nerdvocabs


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*


/**
 * This [Fragment] will fetch word data from sqlite
 * and display using listview
 *
 */
class ReviewFragment : Fragment() {
    private lateinit var helper : WordDataHelper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_review, container, false)
        val wordList = rootView.findViewById<ListView>(R.id.lvReviewVocabs)
        wordList.emptyView = rootView.findViewById(R.id.tvReviewEmpty)

        helper = WordDataHelper(context!!)
        initCollection(rootView)
        initReview(rootView)
        return rootView
    }

    private fun initCollection(rootView: View) {
        val assets = context!!.assets.list("")
        val collection = ArrayList<String>()
        collection.add(resources.getString(R.string.my_words))
        for (a : String in assets) {
            if (a.endsWith(".txt")) {
                collection.add(a)
            }
        }

        val spinner = rootView.findViewById<Spinner>(R.id.spCollection)
        spinner.adapter = ArrayAdapter<String>(context!!,
            android.R.layout.simple_spinner_dropdown_item, collection)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                initReview(rootView)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun initReview(rootView: View) {
        val selected = rootView.findViewById<Spinner>(R.id.spCollection).selectedItem.toString()
        if (selected.equals(resources.getString(R.string.my_words))) {
            val words = helper.getWords()
            val content = rootView.findViewById(R.id.lvReviewVocabs) as ListView
            content.adapter = WordAdapter(this.context!!, words)
        } else {
            val words = helper.loadFromAsset(selected)
            val content = rootView.findViewById(R.id.lvReviewVocabs) as ListView
            content.adapter = WordAdapter(this.context!!, words)
        }
    }

    inner class WordAdapter(context: Context, words: MutableList<Word>) : BaseAdapter() {
        var words: MutableList<Word> = words
        var inflater: LayoutInflater = LayoutInflater.from(context)

        override fun getCount(): Int {
            return words.size
        }

        override fun getItem(position: Int): Any {
            return words[position].word
        }

        override fun getItemId(position: Int): Long {
            return words[position].wordId
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var view: View? = convertView
            if (view == null) {
                view = this.inflater.inflate(R.layout.word_item, parent, false)
            }

            val word = view?.findViewById(R.id.tvWord) as TextView
            word.text = words[position].word

            val wordType = view?.findViewById(R.id.tvWordType) as TextView
            wordType.text = words[position].wordType

            val wordDef = view?.findViewById(R.id.tvDefinition) as TextView
            wordDef.text = words[position].definition

            val sentence = view?.findViewById(R.id.tvSentence) as TextView
            sentence.text = words[position].sentence
            return view!!
        }
    }
}
