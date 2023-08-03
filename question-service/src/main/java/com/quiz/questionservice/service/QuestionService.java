package com.quiz.questionservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.quiz.questionservice.model.Question;
import com.quiz.questionservice.model.QuestionWrapper;
import com.quiz.questionservice.model.Response;
import com.quiz.questionservice.repository.QuestionRepo;

import jakarta.transaction.Transactional;

@Service

public class QuestionService {

	@Autowired
	QuestionRepo repo;
	
	public ResponseEntity<List<Question>> getAllQuestions() 
	{
		try {
		return  new ResponseEntity<>(repo.findAll(),HttpStatus.OK);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return  new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
	}
	
	public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
		
		try {
			return  new ResponseEntity<>(repo.findByCategory(category),HttpStatus.OK);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return  new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
	
	}
	
	public ResponseEntity<Question> addQuestion(Question question) {
		
		return new ResponseEntity<>(repo.save(question),HttpStatus.CREATED);
	}
	public Question updateQuestion(Question question) {
		
		return repo.save(question);
	}
	public String deleteQuestion(int id) {
		repo.deleteQuestionById(id);
		return "deleted";
	}

	public ResponseEntity<List<Integer>> getQuestionsForQuiz(String category, Integer noQ) {
		List<Integer> questions=repo.findRandomQuestionsByCategory(category,noQ);
		return new ResponseEntity<>(questions,HttpStatus.OK);
	}
	
	public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Integer> questionIds) 
	{
		List<QuestionWrapper> wrappers=new ArrayList<>();
		List<Question>questions=new ArrayList<>();
		
		for(Integer id: questionIds)
		{
			questions.add(repo.findById(id).get());
		}
		for(Question question:questions)
		{
			QuestionWrapper wrapper=new QuestionWrapper();
			wrapper.setId(question.getId());
			wrapper.setQuestionTitle(question.getQuestionTitle());
			wrapper.setOption1(question.getOption1());
			wrapper.setOption2(question.getOption2());
			wrapper.setOption3(question.getOption3());
			wrapper.setOption4(question.getOption4());
			wrappers.add(wrapper);
		}
		
		return new ResponseEntity<>(wrappers,HttpStatus.OK);
	}

	public ResponseEntity<Integer> getScore(List<Response> responses) {
		
		int right=0;
	
		for(Response response:responses)
		{
			Question questions=repo.findById(response.getId()).get();
			if(response.getResponse().equals(questions.getRightAnswer()))
			
				right++;
		}
		return new ResponseEntity<>(right,HttpStatus.OK);
	}
	

}
