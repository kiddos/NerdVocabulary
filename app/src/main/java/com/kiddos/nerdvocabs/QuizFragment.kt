package com.kiddos.nerdvocabs


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView


/**
 * [Fragment] that read from word data and make small quiz.
 *
 */
class QuizFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_quiz, container, false)
        val helper = WordDataHelper(context!!)
        val question = helper.getQuestion()
        prepareQuestion(rootView, question)

        rootView.findViewById<Button>(R.id.btnNext).setOnClickListener {
            val nextQuestion = helper.getQuestion()
            prepareQuestion(rootView, nextQuestion)
        }
        return rootView
    }

    fun prepareQuestion(rootView: View, question: Question?) {
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
