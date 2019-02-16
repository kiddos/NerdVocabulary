package com.kiddos.nerdvocabs


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*


/**
 * [Fragment] that read from word data and make small quiz.
 *
 */
class QuizFragment : Fragment() {
    private lateinit var helper: WordDataHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_quiz, container, false)
        helper = WordDataHelper(context!!)

        rootView.findViewById<Button>(R.id.btnNext).setOnClickListener {
            rootView.findViewById<RadioButton>(R.id.rb1).isSelected = false
            rootView.findViewById<RadioButton>(R.id.rb2).isSelected = false
            rootView.findViewById<RadioButton>(R.id.rb3).isSelected = false
            rootView.findViewById<RadioButton>(R.id.rb4).isSelected = false
            chooseQuestion(rootView)
        }

        initCollection(rootView)

        chooseQuestion(rootView)
        return rootView
    }

    private fun chooseQuestion(rootView: View) {
        val spinner = rootView.findViewById<Spinner>(R.id.spCollection)
        val selected = spinner.selectedItem.toString()
        Log.d("selected", selected)
        if (selected.equals(resources.getString(R.string.my_words))) {
            val words = helper.getWords()
            val question = helper.getQuestion(words)
            prepareQuestion(rootView, question)
        } else {
            val words = helper.loadFromAsset(selected)
            val question = helper.getQuestion(words)
            prepareQuestion(rootView, question)
        }
    }

    private fun initCollection(rootView: View) {
        val assets = context!!.assets.list("")
        val collection = ArrayList<String>()
        for (a : String in assets) {
            if (a.endsWith(".txt")) {
                collection.add(a)
            }
        }

        val words = helper.getWords()
        if (words.size >= 4) {
            collection.add(resources.getString(R.string.my_words))
        }
        rootView.findViewById<Spinner>(R.id.spCollection).adapter = ArrayAdapter<String>(context!!,
            android.R.layout.simple_spinner_dropdown_item, collection)
    }

    private fun prepareQuestion(rootView: View, question: Question?) {
        if (question != null) {
            rootView.findViewById<TextView>(R.id.tvAnswer).text = getString(R.string.choose_answer)
            rootView.findViewById<TextView>(R.id.tvAnswer).setTextColor(resources.getColor(R.color.colorAnswer))
            rootView.findViewById<TextView>(R.id.tvWord).text = question.word
            rootView.findViewById<TextView>(R.id.tvChoice1).text = question.choices[0]
            rootView.findViewById<TextView>(R.id.tvChoice2).text = question.choices[1]
            rootView.findViewById<TextView>(R.id.tvChoice3).text = question.choices[2]
            rootView.findViewById<TextView>(R.id.tvChoice4).text = question.choices[3]

            val select = rootView.findViewById<RadioGroup>(R.id.radioGroup)
            select.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
                override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                    val answer = rootView.findViewById<TextView>(R.id.tvAnswer)
                    var position = 0
                    when (checkedId) {
                        R.id.rb1 -> position = 0
                        R.id.rb2 -> position = 1
                        R.id.rb3 -> position = 2
                        R.id.rb4 -> position = 3
                    }
                    if (position == question.answer) {
                        answer.text = getString(R.string.correct_answer)
                        answer.setTextColor(resources.getColor(R.color.colorCorrect))
                    } else {
                        answer.text = getString(R.string.incorrect_answer)
                        answer.setTextColor(resources.getColor(R.color.colorWrong))
                    }
                }
            })
        }
    }
}
