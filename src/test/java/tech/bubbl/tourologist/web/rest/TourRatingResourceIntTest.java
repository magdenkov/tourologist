package tech.bubbl.tourologist.web.rest;

import tech.bubbl.tourologist.TourologistApp;

import tech.bubbl.tourologist.domain.TourRating;
import tech.bubbl.tourologist.repository.TourRatingRepository;
import tech.bubbl.tourologist.service.TourRatingService;
import tech.bubbl.tourologist.service.dto.TourRatingDTO;
import tech.bubbl.tourologist.service.mapper.TourRatingMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TourRatingResource REST controller.
 *
 * @see TourRatingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TourologistApp.class)
public class TourRatingResourceIntTest {

    private static final Integer DEFAULT_RATE = 0;
    private static final Integer UPDATED_RATE = 1;

    private static final String DEFAULT_FEEDBACK = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_FEEDBACK = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_TIME_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_TIME);

    @Inject
    private TourRatingRepository tourRatingRepository;

    @Inject
    private TourRatingMapper tourRatingMapper;

    @Inject
    private TourRatingService tourRatingService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTourRatingMockMvc;

    private TourRating tourRating;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TourRatingResource tourRatingResource = new TourRatingResource();
        ReflectionTestUtils.setField(tourRatingResource, "tourRatingService", tourRatingService);
        this.restTourRatingMockMvc = MockMvcBuilders.standaloneSetup(tourRatingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TourRating createEntity(EntityManager em) {
        TourRating tourRating = new TourRating()
                .rate(DEFAULT_RATE)
                .feedback(DEFAULT_FEEDBACK)
                .time(DEFAULT_TIME);
        return tourRating;
    }

    @Before
    public void initTest() {
        tourRating = createEntity(em);
    }

    @Test
    @Transactional
    public void createTourRating() throws Exception {
        int databaseSizeBeforeCreate = tourRatingRepository.findAll().size();

        // Create the TourRating
        TourRatingDTO tourRatingDTO = tourRatingMapper.tourRatingToTourRatingDTO(tourRating);

        restTourRatingMockMvc.perform(post("/api/tour-ratings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tourRatingDTO)))
                .andExpect(status().isCreated());

        // Validate the TourRating in the database
        List<TourRating> tourRatings = tourRatingRepository.findAll();
        assertThat(tourRatings).hasSize(databaseSizeBeforeCreate + 1);
        TourRating testTourRating = tourRatings.get(tourRatings.size() - 1);
        assertThat(testTourRating.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testTourRating.getFeedback()).isEqualTo(DEFAULT_FEEDBACK);
        assertThat(testTourRating.getTime()).isEqualTo(DEFAULT_TIME);
    }

    @Test
    @Transactional
    public void checkRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = tourRatingRepository.findAll().size();
        // set the field null
        tourRating.setRate(null);

        // Create the TourRating, which fails.
        TourRatingDTO tourRatingDTO = tourRatingMapper.tourRatingToTourRatingDTO(tourRating);

        restTourRatingMockMvc.perform(post("/api/tour-ratings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tourRatingDTO)))
                .andExpect(status().isBadRequest());

        List<TourRating> tourRatings = tourRatingRepository.findAll();
        assertThat(tourRatings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTourRatings() throws Exception {
        // Initialize the database
        tourRatingRepository.saveAndFlush(tourRating);

        // Get all the tourRatings
        restTourRatingMockMvc.perform(get("/api/tour-ratings?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tourRating.getId().intValue())))
                .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE)))
                .andExpect(jsonPath("$.[*].feedback").value(hasItem(DEFAULT_FEEDBACK.toString())))
                .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME_STR)));
    }

    @Test
    @Transactional
    public void getTourRating() throws Exception {
        // Initialize the database
        tourRatingRepository.saveAndFlush(tourRating);

        // Get the tourRating
        restTourRatingMockMvc.perform(get("/api/tour-ratings/{id}", tourRating.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tourRating.getId().intValue()))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE))
            .andExpect(jsonPath("$.feedback").value(DEFAULT_FEEDBACK.toString()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingTourRating() throws Exception {
        // Get the tourRating
        restTourRatingMockMvc.perform(get("/api/tour-ratings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTourRating() throws Exception {
        // Initialize the database
        tourRatingRepository.saveAndFlush(tourRating);
        int databaseSizeBeforeUpdate = tourRatingRepository.findAll().size();

        // Update the tourRating
        TourRating updatedTourRating = tourRatingRepository.findOne(tourRating.getId());
        updatedTourRating
                .rate(UPDATED_RATE)
                .feedback(UPDATED_FEEDBACK)
                .time(UPDATED_TIME);
        TourRatingDTO tourRatingDTO = tourRatingMapper.tourRatingToTourRatingDTO(updatedTourRating);

        restTourRatingMockMvc.perform(put("/api/tour-ratings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tourRatingDTO)))
                .andExpect(status().isOk());

        // Validate the TourRating in the database
        List<TourRating> tourRatings = tourRatingRepository.findAll();
        assertThat(tourRatings).hasSize(databaseSizeBeforeUpdate);
        TourRating testTourRating = tourRatings.get(tourRatings.size() - 1);
        assertThat(testTourRating.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testTourRating.getFeedback()).isEqualTo(UPDATED_FEEDBACK);
        assertThat(testTourRating.getTime()).isEqualTo(UPDATED_TIME);
    }

    @Test
    @Transactional
    public void deleteTourRating() throws Exception {
        // Initialize the database
        tourRatingRepository.saveAndFlush(tourRating);
        int databaseSizeBeforeDelete = tourRatingRepository.findAll().size();

        // Get the tourRating
        restTourRatingMockMvc.perform(delete("/api/tour-ratings/{id}", tourRating.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TourRating> tourRatings = tourRatingRepository.findAll();
        assertThat(tourRatings).hasSize(databaseSizeBeforeDelete - 1);
    }
}
