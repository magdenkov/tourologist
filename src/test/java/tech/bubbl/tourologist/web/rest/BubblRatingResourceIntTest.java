package tech.bubbl.tourologist.web.rest;

import tech.bubbl.tourologist.TourologistApp;

import tech.bubbl.tourologist.domain.BubblRating;
import tech.bubbl.tourologist.repository.BubblRatingRepository;
import tech.bubbl.tourologist.service.BubblRatingService;
import tech.bubbl.tourologist.service.dto.BubblRatingDTO;
import tech.bubbl.tourologist.service.mapper.BubblRatingMapper;

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
 * Test class for the BubblRatingResource REST controller.
 *
 * @see BubblRatingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TourologistApp.class)
public class BubblRatingResourceIntTest {

    private static final Integer DEFAULT_RATE = 0;
    private static final Integer UPDATED_RATE = 1;

    private static final String DEFAULT_FEEDBACK = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_FEEDBACK = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_TIME_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_TIME);

    @Inject
    private BubblRatingRepository bubblRatingRepository;

    @Inject
    private BubblRatingMapper bubblRatingMapper;

    @Inject
    private BubblRatingService bubblRatingService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBubblRatingMockMvc;

    private BubblRating bubblRating;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BubblRatingResource bubblRatingResource = new BubblRatingResource();
        ReflectionTestUtils.setField(bubblRatingResource, "bubblRatingService", bubblRatingService);
        this.restBubblRatingMockMvc = MockMvcBuilders.standaloneSetup(bubblRatingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BubblRating createEntity(EntityManager em) {
        BubblRating bubblRating = new BubblRating()
                .rate(DEFAULT_RATE)
                .feedback(DEFAULT_FEEDBACK)
                .time(DEFAULT_TIME);
        return bubblRating;
    }

    @Before
    public void initTest() {
        bubblRating = createEntity(em);
    }

    @Test
    @Transactional
    public void createBubblRating() throws Exception {
        int databaseSizeBeforeCreate = bubblRatingRepository.findAll().size();

        // Create the BubblRating
        BubblRatingDTO bubblRatingDTO = bubblRatingMapper.bubblRatingToBubblRatingDTO(bubblRating);

        restBubblRatingMockMvc.perform(post("/api/bubbl-ratings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bubblRatingDTO)))
                .andExpect(status().isCreated());

        // Validate the BubblRating in the database
        List<BubblRating> bubblRatings = bubblRatingRepository.findAll();
        assertThat(bubblRatings).hasSize(databaseSizeBeforeCreate + 1);
        BubblRating testBubblRating = bubblRatings.get(bubblRatings.size() - 1);
        assertThat(testBubblRating.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testBubblRating.getFeedback()).isEqualTo(DEFAULT_FEEDBACK);
        assertThat(testBubblRating.getTime()).isEqualTo(DEFAULT_TIME);
    }

    @Test
    @Transactional
    public void checkRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = bubblRatingRepository.findAll().size();
        // set the field null
        bubblRating.setRate(null);

        // Create the BubblRating, which fails.
        BubblRatingDTO bubblRatingDTO = bubblRatingMapper.bubblRatingToBubblRatingDTO(bubblRating);

        restBubblRatingMockMvc.perform(post("/api/bubbl-ratings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bubblRatingDTO)))
                .andExpect(status().isBadRequest());

        List<BubblRating> bubblRatings = bubblRatingRepository.findAll();
        assertThat(bubblRatings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBubblRatings() throws Exception {
        // Initialize the database
        bubblRatingRepository.saveAndFlush(bubblRating);

        // Get all the bubblRatings
        restBubblRatingMockMvc.perform(get("/api/bubbl-ratings?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bubblRating.getId().intValue())))
                .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE)))
                .andExpect(jsonPath("$.[*].feedback").value(hasItem(DEFAULT_FEEDBACK.toString())))
                .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME_STR)));
    }

    @Test
    @Transactional
    public void getBubblRating() throws Exception {
        // Initialize the database
        bubblRatingRepository.saveAndFlush(bubblRating);

        // Get the bubblRating
        restBubblRatingMockMvc.perform(get("/api/bubbl-ratings/{id}", bubblRating.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bubblRating.getId().intValue()))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE))
            .andExpect(jsonPath("$.feedback").value(DEFAULT_FEEDBACK.toString()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingBubblRating() throws Exception {
        // Get the bubblRating
        restBubblRatingMockMvc.perform(get("/api/bubbl-ratings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBubblRating() throws Exception {
        // Initialize the database
        bubblRatingRepository.saveAndFlush(bubblRating);
        int databaseSizeBeforeUpdate = bubblRatingRepository.findAll().size();

        // Update the bubblRating
        BubblRating updatedBubblRating = bubblRatingRepository.findOne(bubblRating.getId());
        updatedBubblRating
                .rate(UPDATED_RATE)
                .feedback(UPDATED_FEEDBACK)
                .time(UPDATED_TIME);
        BubblRatingDTO bubblRatingDTO = bubblRatingMapper.bubblRatingToBubblRatingDTO(updatedBubblRating);

        restBubblRatingMockMvc.perform(put("/api/bubbl-ratings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bubblRatingDTO)))
                .andExpect(status().isOk());

        // Validate the BubblRating in the database
        List<BubblRating> bubblRatings = bubblRatingRepository.findAll();
        assertThat(bubblRatings).hasSize(databaseSizeBeforeUpdate);
        BubblRating testBubblRating = bubblRatings.get(bubblRatings.size() - 1);
        assertThat(testBubblRating.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testBubblRating.getFeedback()).isEqualTo(UPDATED_FEEDBACK);
        assertThat(testBubblRating.getTime()).isEqualTo(UPDATED_TIME);
    }

    @Test
    @Transactional
    public void deleteBubblRating() throws Exception {
        // Initialize the database
        bubblRatingRepository.saveAndFlush(bubblRating);
        int databaseSizeBeforeDelete = bubblRatingRepository.findAll().size();

        // Get the bubblRating
        restBubblRatingMockMvc.perform(delete("/api/bubbl-ratings/{id}", bubblRating.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<BubblRating> bubblRatings = bubblRatingRepository.findAll();
        assertThat(bubblRatings).hasSize(databaseSizeBeforeDelete - 1);
    }
}
