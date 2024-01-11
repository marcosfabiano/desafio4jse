package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import com.devsuperior.dsmovie.tests.ScoreFactory;
import com.devsuperior.dsmovie.tests.UserFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class ScoreServiceTests {
	
	@InjectMocks
	private ScoreService service;
	@Mock
	private UserService userService;
	@Mock
	private MovieRepository movieRepository;
	@Mock
	private ScoreRepository scoreRepository;

	private long existingMovieId, nonExistingMovieId;

	private Double sum;
	private UserEntity user;
	private MovieEntity movieEntity;
	private MovieDTO movieDTO;
	private ScoreEntity scoreEntity;

	private ScoreDTO scoreDTO;

	@BeforeEach
	void setUp() throws Exception {

		existingMovieId = 1L;
		nonExistingMovieId = 2L;

		sum = 0.0;

		user = UserFactory.createUserEntity();

		movieEntity = MovieFactory.createMovieEntity();
		movieDTO = new MovieDTO(movieEntity);

		scoreEntity = ScoreFactory.createScoreEntity();
		scoreDTO = new ScoreDTO(scoreEntity);

		Mockito.when(movieRepository.findById(existingMovieId)).thenReturn(Optional.of(movieEntity));
		Mockito.when(movieRepository.findById(nonExistingMovieId)).thenThrow(ResourceNotFoundException.class);

		Mockito.when(scoreRepository.saveAndFlush(any())).thenReturn(scoreEntity);
		Mockito.when(movieRepository.save(any())).thenReturn(movieEntity);
	}
	
	@Test
	public void saveScoreShouldReturnMovieDTO() {

		MovieDTO result = service.saveScore(scoreDTO);
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {

		scoreEntity.setMovie(new MovieEntity());
		scoreDTO = new ScoreDTO(scoreEntity);

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.saveScore(scoreDTO);
		});

	}
}
