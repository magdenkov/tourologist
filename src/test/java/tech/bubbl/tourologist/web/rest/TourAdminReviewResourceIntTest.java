package tech.bubbl.tourologist.web.rest;

import tech.bubbl.tourologist.TourologistApp;

import tech.bubbl.tourologist.domain.TourAdminReview;
import tech.bubbl.tourologist.repository.TourAdminReviewRepository;
import tech.bubbl.tourologist.service.TourAdminReviewService;
import tech.bubbl.tourologist.service.dto.TourAdminReviewDTO;
import tech.bubbl.tourologist.service.mapper.TourAdminReviewMapper;

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
 * Test class for the TourAdminReviewResource REST controller.
 *
 * @see TourAdminReviewResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TourologistApp.class)
public class TourAdminReviewResourceIntTest {

    private static final String DEFAULT_REASON = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Boolean DEFAULT_APPROVED = false;
    private static final Boolean UPDATED_APPROVED = true;

    private static final ZonedDateTime DEFAULT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_TIME_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_TIME);

    @Inject
    private TourAdminReviewRepository tourAdminReviewRepository;

    @Inject
    private TourAdminReviewMapper tourAdminReviewMapper;

    @Inject
    private TourAdminReviewService tourAdminReviewService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTourAdminReviewMockMvc;

    private TourAdminReview tourAdminReview;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TourAdminReviewResource tourAdminReviewResource = new TourAdminReviewResource();
        ReflectionTestUtils.setField(tourAdminReviewResource, "tourAdminReviewService", tourAdminReviewService);
        this.restTourAdminReviewMockMvc = MockMvcBuilders.standaloneSetup(tourAdminReviewResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TourAdminReview createEntity(EntityManager em) {
        TourAdminReview tourAdminReview = new TourAdminReview()
                .reason(DEFAULT_REASON)
                .approved(DEFAULT_APPROVED)
                .time(DEFAULT_TIME);
        return tourAdminReview;
    }

    @Before
    public void initTest() {
        tourAdminReview = createEntity(em);
    }

    @Test
    @Transactional
    public void createTourAdminReview() throws Exception {
        int databaseSizeBeforeCreate = tourAdminReviewRepository.findAll().size();

        // Create the TourAdminReview
        TourAdminReviewDTO tourAdminReviewDTO = tourAdminReviewMapper.tourAdminReviewToTourAdminReviewDTO(tourAdminReview);

        restTourAdminReviewMockMvc.perform(post("/api/tour-admin-reviews")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tourAdminReviewDTO)))
                .andExpect(status().isCreated());

        // Validate the TourAdminReview in the database
        List<TourAdminReview> tourAdminReviews = tourAdminReviewRepository.findAll();
        assertThat(tourAdminReviews).hasSize(databaseSizeBeforeCreate + 1);
        TourAdminReview testTourAdminReview = tourAdminReviews.get(tourAdminReviews.size() - 1);
        assertThat(testTourAdminReview.getReason()).isEqualTo(DEFAULT_REASON);
        assertThat(testTourAdminReview.isApproved()).isEqualTo(DEFAULT_APPROVED);
        assertThat(testTourAdminReview.getTime()).isEqualTo(DEFAULT_TIME);
    }

    @Test
    @Transactional
    public void getAllTourAdminReviews() throws Exception {
        // Initialize the database
        tourAdminReviewRepository.saveAndFlush(tourAdminReview);

        // Get all the tourAdminReviews
        restTourAdminReviewMockMvc.perform(get("/api/tour-admin-reviews?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tourAdminReview.getId().intValue())))
                .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON.toString())))
                .andExpect(jsonPath("$.[*].approved").value(hasItem(DEFAULT_APPROVED.booleanValue())))
                .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME_STR)));
    }

    @Test
    @Transactional
    public void getTourAdminReview() throws Exception {
        // Initialize the database
        tourAdminReviewRepository.saveAndFlush(tourAdminReview);

        // Get the tourAdminReview
        restTourAdminReviewMockMvc.perform(get("/api/tour-admin-reviews/{id}", tourAdminReview.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tourAdminReview.getId().intValue()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON.toString()))
            .andExpect(jsonPath("$.approved").value(DEFAULT_APPROVED.booleanValue()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingTourAdminReview() throws Exception {
        // Get the tourAdminReview
        restTourAdminReviewMockMvc.perform(get("/api/tour-admin-reviews/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTourAdminReview() throws Exception {
        // Initialize the database
        tourAdminReviewRepository.saveAndFlush(tourAdminReview);
        int databaseSizeBeforeUpdate = tourAdminReviewRepository.findAll().size();

        // Update the tourAdminReview
        TourAdminReview updatedTourAdminReview = tourAdminReviewRepository.findOne(tourAdminReview.getId());
        updatedTourAdminReview
                .reason(UPDATED_REASON)
                .approved(UPDATED_APPROVED)
                .time(UPDATED_TIME);
        TourAdminReviewDTO tourAdminReviewDTO = tourAdminReviewMapper.tourAdminReviewToTourAdminReviewDTO(updatedTourAdminReview);

        restTourAdminReviewMockMvc.perform(put("/api/tour-admin-reviews")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tourAdminReviewDTO)))
                .andExpect(status().isOk());

        // Validate the TourAdminReview in the database
        List<TourAdminReview> tourAdminReviews = tourAdminReviewRepository.findAll();
        assertThat(tourAdminReviews).hasSize(databaseSizeBeforeUpdate);
        TourAdminReview testTourAdminReview = tourAdminReviews.get(tourAdminReviews.size() - 1);
        assertThat(testTourAdminReview.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testTourAdminReview.isApproved()).isEqualTo(UPDATED_APPROVED);
        assertThat(testTourAdminReview.getTime()).isEqualTo(UPDATED_TIME);
    }

    @Test
    @Transactional
    public void deleteTourAdminReview() throws Exception {
        // Initialize the database
        tourAdminReviewRepository.saveAndFlush(tourAdminReview);
        int databaseSizeBeforeDelete = tourAdminReviewRepository.findAll().size();

        // Get the tourAdminReview
        restTourAdminReviewMockMvc.perform(delete("/api/tour-admin-reviews/{id}", tourAdminReview.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TourAdminReview> tourAdminReviews = tourAdminReviewRepository.findAll();
        assertThat(tourAdminReviews).hasSize(databaseSizeBeforeDelete - 1);
    }
}
