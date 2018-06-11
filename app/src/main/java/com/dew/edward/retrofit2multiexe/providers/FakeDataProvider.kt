package com.dew.edward.retrofit2multiexe.providers

import com.dew.edward.retrofit2multiexe.models.Answer
import com.dew.edward.retrofit2multiexe.models.Question

/*
 * Created by Edward on 6/11/2018.
 */

class FakeDataProvider {

    companion object {
        fun getQuestions(): List<Question>{
            val questions = arrayListOf<Question>()
            for (i in 0 until 10){
                val question = Question().apply{
                    questionId = i.toString()
                    title = i.toString()
                    body = i.toString() + "Body"
                }

                questions.add(question)
            }
            return questions
        }

        fun getAnswers(): List<Answer> {
            val answers = arrayListOf<Answer>()
            for (i in 0 until 10){
                val answer = Answer().apply{
                    answerId = i
                    accepted = false
                    score = i
                }

                answers.add(answer)
            }
            return answers
        }
    }

}