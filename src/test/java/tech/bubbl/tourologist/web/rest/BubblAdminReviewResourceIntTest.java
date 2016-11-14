package tech.bubbl.tourologist.web.rest;

import tech.bubbl.tourologist.TourologistApp;

import tech.bubbl.tourologist.domain.BubblAdminReview;
import tech.bubbl.tourologist.repository.BubblAdminReviewRepository;
import tech.bubbl.tourologist.service.BubblAdminReviewService;
import tech.bubbl.tourologist.service.dto.BubblAdminReviewDTO;
import tech.bubbl.tourologist.service.mapper.BubblAdminReviewMapper;

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
 * Test class for the BubblAdminReviewResource REST controller.
 *
 * @see BubblAdminReviewResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TourologistApp.class)
public class BubblAdminReviewResourceIntTest {

    private static final String DEFAULT_REASON = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Boolean DEFAULT_APPROVED = false;
    private static final Boolean UPDATED_APPROVED = true;

    private static final ZonedDateTime DEFAULT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_TIME_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_TIME);

    @Inject
    private BubblAdminReviewRepository bubblAdminReviewRepository;

    @Inject
    private BubblAdminReviewMapper bubblAdminReviewMapper;

    @Inject
    private BubblAdminReviewService bubblAdminReviewService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBubblAdminReviewMockMvc;

    private BubblAdminReview bubblAdminReview;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BubblAdminReviewResource bubblAdminReviewResource = new BubblAdminReviewResource();
        ReflectionTestUtils.setField(bubblAdminReviewResource, "bubblAdminReviewService", bubblAdminReviewService);
        this.restBubblAdminReviewMockMvc = MockMvcBuilders.standaloneSetup(bubblAdminReviewResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BubblAdminReview createEntity(EntityManager em) {
        BubblAdminReview bubblAdminReview = new BubblAdminReview()
                .reason(DEFAULT_REASON)
                .approved(DEFAULT_APPROVED)
                .time(DEFAULT_TIME);
        return bubblAdminReview;
    }

    @Before
    public void initTest() {
        bubblAdminReview = createEntity(em);
    }

    @Test
    @Transactional
    public void createBubblAdminReview() throws Exception {
        int databaseSizeBeforeCreate = bubblAdminReviewRepository.findAll().size();

        // Create the BubblAdminReview
        BubblAdminReviewDTO bubblAdminReviewDTO = bubblAdminReviewMapper.bubblAdminReviewToBubblAdminReviewDTO(bubblAdminReview);

        restBubblAdminReviewMockMvc.perform(post("/api/bubbl-admin-reviews")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bubblAdminReviewDTO)))
                .andExpect(status().isCreated());

        // Validate the BubblAdminReview in the database
        List<BubblAdminReview> bubblAdminReviews = bubblAdminReviewRepository.findAll();
        assertThat(bubblAdminReviews).hasSize(databaseSizeBeforeCreate + 1);
        BubblAdminReview testBubblAdminReview = bubblAdminReviews.get(bubblAdminReviews.size() - 1);
        assertThat(testBubblAdminReview.getReason()).isEqualTo(DEFAULT_REASON);
        assertThat(testBubblAdminReview.isApproved()).isEqualTo(DEFAULT_APPROVED);
        assertThat(testBubblAdminReview.getTime()).isEqualTo(DEFAULT_TIME);
    }

    @Test
    @Transactional
    public void getAllBubblAdminReviews() throws Exception {
        // Initialize the database
        bubblAdminReviewRepository.saveAndFlush(bubblAdminReview);

        // Get all the bubblAdminReviews
        restBubblAdminReviewMockMvc.perform(get("/api/bubbl-admin-reviews?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bubblAdminReview.getId().intValue())))
                .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON.toString())))
                .andExpect(jsonPath("$.[*].approved").value(hasItem(DEFAULT_APPROVED.booleanValue())))
                .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME_STR)));
    }

    @Test
    @Transactional
    public void getBubblAdminReview() throws Exception {
        // Initialize the database
        bubblAdminReviewRepository.saveAndFlush(bubblAdminReview);

        // Get the bubblAdminReview
        restBubblAdminReviewMockMvc.perform(get("/api/bubbl-admin-reviews/{id}", bubblAdminReview.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bubblAdminReview.getId().intValue()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON.toString()))
            .andExpect(jsonPath("$.approved").value(DEFAULT_APPROVED.booleanValue()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingBubblAdminReview() throws Exception {
        // Get the bubblAdminReview
        restBubblAdminReviewMockMvc.perform(get("/api/bubbl-admin-reviews/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBubblAdminReview() throws Exception {
        // Initialize the database
        bubblAdminReviewRepository.saveAndFlush(bubblAdminReview);
        int databaseSizeBeforeUpdate = bubblAdminReviewRepository.findAll().size();

        // Update the bubblAdminReview
        BubblAdminReview updatedBubblAdminReview = bubblAdminReviewRepository.findOne(bubblAdminReview.getId());
        updatedBubblAdminReview
                .reason(UPDATED_REASON)
                .approved(UPDATED_APPROVED)
                .time(UPDATED_TIME);
        BubblAdminReviewDTO bubblAdminReviewDTO = bubblAdminReviewMapper.bubblAdminReviewToBubblAdminReviewDTO(updatedBubblAdminReview);

        restBubblAdminReviewMockMvc.perform(put("/api/bubbl-admin-reviews")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bubblAdminReviewDTO)))
                .andExpect(status().isOk());

        // Validate the BubblAdminReview in the database
        List<BubblAdminReview> bubblAdminReviews = bubblAdminReviewRepository.findAll();
        assertThat(bubblAdminReviews).hasSize(databaseSizeBeforeUpdate);
        BubblAdminReview testBubblAdminReview = bubblAdminReviews.get(bubblAdminReviews.size() - 1);
        assertThat(testBubblAdminReview.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testBubblAdminReview.isApproved()).isEqualTo(UPDATED_APPROVED);
        assertThat(testBubblAdminReview.getTime()).isEqualTo(UPDATED_TIME);
    }

    @Test
    @Transactional
    public void deleteBubblAdminReview() throws Exception {
        // Initialize the database
        bubblAdminReviewRepository.saveAndFlush(bubblAdminReview);
        int databaseSizeBeforeDelete = bubblAdminReviewRepository.findAll().size();

        // Get the bubblAdminReview
        restBubblAdminReviewMockMvc.perform(delete("/api/bubbl-admin-reviews/{id}", bubblAdminReview.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<BubblAdminReview> bubblAdminReviews = bubblAdminReviewRepository.findAll();
        assertThat(bubblAdminReviews).hasSize(databaseSizeBeforeDelete - 1);
    }
}
