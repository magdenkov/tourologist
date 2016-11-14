package tech.bubbl.tourologist.web.rest;

import tech.bubbl.tourologist.TourologistApp;

import tech.bubbl.tourologist.domain.TourImage;
import tech.bubbl.tourologist.repository.TourImageRepository;
import tech.bubbl.tourologist.service.TourImageService;
import tech.bubbl.tourologist.service.dto.TourImageDTO;
import tech.bubbl.tourologist.service.mapper.TourImageMapper;

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
 * Test class for the TourImageResource REST controller.
 *
 * @see TourImageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TourologistApp.class)
public class TourImageResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final ZonedDateTime DEFAULT_UPLOADED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_UPLOADED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_UPLOADED_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_UPLOADED);

    private static final String DEFAULT_S_3_KEY = "AAAAA";
    private static final String UPDATED_S_3_KEY = "BBBBB";

    private static final String DEFAULT_S_3_BUCKET = "AAAAA";
    private static final String UPDATED_S_3_BUCKET = "BBBBB";

    private static final String DEFAULT_S_3_THUMB_KEY = "AAAAA";
    private static final String UPDATED_S_3_THUMB_KEY = "BBBBB";

    private static final String DEFAULT_S_3_REGION = "AAAAA";
    private static final String UPDATED_S_3_REGION = "BBBBB";

    private static final String DEFAULT_TYPE = "AAAAA";
    private static final String UPDATED_TYPE = "BBBBB";

    private static final Boolean DEFAULT_MASTER = false;
    private static final Boolean UPDATED_MASTER = true;

    @Inject
    private TourImageRepository tourImageRepository;

    @Inject
    private TourImageMapper tourImageMapper;

    @Inject
    private TourImageService tourImageService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTourImageMockMvc;

    private TourImage tourImage;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TourImageResource tourImageResource = new TourImageResource();
        ReflectionTestUtils.setField(tourImageResource, "tourImageService", tourImageService);
        this.restTourImageMockMvc = MockMvcBuilders.standaloneSetup(tourImageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TourImage createEntity(EntityManager em) {
        TourImage tourImage = new TourImage()
                .name(DEFAULT_NAME)
                .uploaded(DEFAULT_UPLOADED)
                .s3Key(DEFAULT_S_3_KEY)
                .s3Bucket(DEFAULT_S_3_BUCKET)
                .s3ThumbKey(DEFAULT_S_3_THUMB_KEY)
                .s3Region(DEFAULT_S_3_REGION)
                .type(DEFAULT_TYPE)
                .master(DEFAULT_MASTER);
        return tourImage;
    }

    @Before
    public void initTest() {
        tourImage = createEntity(em);
    }

    @Test
    @Transactional
    public void createTourImage() throws Exception {
        int databaseSizeBeforeCreate = tourImageRepository.findAll().size();

        // Create the TourImage
        TourImageDTO tourImageDTO = tourImageMapper.tourImageToTourImageDTO(tourImage);

        restTourImageMockMvc.perform(post("/api/tour-images")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tourImageDTO)))
                .andExpect(status().isCreated());

        // Validate the TourImage in the database
        List<TourImage> tourImages = tourImageRepository.findAll();
        assertThat(tourImages).hasSize(databaseSizeBeforeCreate + 1);
        TourImage testTourImage = tourImages.get(tourImages.size() - 1);
        assertThat(testTourImage.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTourImage.getUploaded()).isEqualTo(DEFAULT_UPLOADED);
        assertThat(testTourImage.gets3Key()).isEqualTo(DEFAULT_S_3_KEY);
        assertThat(testTourImage.gets3Bucket()).isEqualTo(DEFAULT_S_3_BUCKET);
        assertThat(testTourImage.gets3ThumbKey()).isEqualTo(DEFAULT_S_3_THUMB_KEY);
        assertThat(testTourImage.gets3Region()).isEqualTo(DEFAULT_S_3_REGION);
        assertThat(testTourImage.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTourImage.isMaster()).isEqualTo(DEFAULT_MASTER);
    }

    @Test
    @Transactional
    public void getAllTourImages() throws Exception {
        // Initialize the database
        tourImageRepository.saveAndFlush(tourImage);

        // Get all the tourImages
        restTourImageMockMvc.perform(get("/api/tour-images?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tourImage.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].uploaded").value(hasItem(DEFAULT_UPLOADED_STR)))
                .andExpect(jsonPath("$.[*].s3Key").value(hasItem(DEFAULT_S_3_KEY.toString())))
                .andExpect(jsonPath("$.[*].s3Bucket").value(hasItem(DEFAULT_S_3_BUCKET.toString())))
                .andExpect(jsonPath("$.[*].s3ThumbKey").value(hasItem(DEFAULT_S_3_THUMB_KEY.toString())))
                .andExpect(jsonPath("$.[*].s3Region").value(hasItem(DEFAULT_S_3_REGION.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].master").value(hasItem(DEFAULT_MASTER.booleanValue())));
    }

    @Test
    @Transactional
    public void getTourImage() throws Exception {
        // Initialize the database
        tourImageRepository.saveAndFlush(tourImage);

        // Get the tourImage
        restTourImageMockMvc.perform(get("/api/tour-images/{id}", tourImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tourImage.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.uploaded").value(DEFAULT_UPLOADED_STR))
            .andExpect(jsonPath("$.s3Key").value(DEFAULT_S_3_KEY.toString()))
            .andExpect(jsonPath("$.s3Bucket").value(DEFAULT_S_3_BUCKET.toString()))
            .andExpect(jsonPath("$.s3ThumbKey").value(DEFAULT_S_3_THUMB_KEY.toString()))
            .andExpect(jsonPath("$.s3Region").value(DEFAULT_S_3_REGION.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.master").value(DEFAULT_MASTER.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTourImage() throws Exception {
        // Get the tourImage
        restTourImageMockMvc.perform(get("/api/tour-images/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTourImage() throws Exception {
        // Initialize the database
        tourImageRepository.saveAndFlush(tourImage);
        int databaseSizeBeforeUpdate = tourImageRepository.findAll().size();

        // Update the tourImage
        TourImage updatedTourImage = tourImageRepository.findOne(tourImage.getId());
        updatedTourImage
                .name(UPDATED_NAME)
                .uploaded(UPDATED_UPLOADED)
                .s3Key(UPDATED_S_3_KEY)
                .s3Bucket(UPDATED_S_3_BUCKET)
                .s3ThumbKey(UPDATED_S_3_THUMB_KEY)
                .s3Region(UPDATED_S_3_REGION)
                .type(UPDATED_TYPE)
                .master(UPDATED_MASTER);
        TourImageDTO tourImageDTO = tourImageMapper.tourImageToTourImageDTO(updatedTourImage);

        restTourImageMockMvc.perform(put("/api/tour-images")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tourImageDTO)))
                .andExpect(status().isOk());

        // Validate the TourImage in the database
        List<TourImage> tourImages = tourImageRepository.findAll();
        assertThat(tourImages).hasSize(databaseSizeBeforeUpdate);
        TourImage testTourImage = tourImages.get(tourImages.size() - 1);
        assertThat(testTourImage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTourImage.getUploaded()).isEqualTo(UPDATED_UPLOADED);
        assertThat(testTourImage.gets3Key()).isEqualTo(UPDATED_S_3_KEY);
        assertThat(testTourImage.gets3Bucket()).isEqualTo(UPDATED_S_3_BUCKET);
        assertThat(testTourImage.gets3ThumbKey()).isEqualTo(UPDATED_S_3_THUMB_KEY);
        assertThat(testTourImage.gets3Region()).isEqualTo(UPDATED_S_3_REGION);
        assertThat(testTourImage.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTourImage.isMaster()).isEqualTo(UPDATED_MASTER);
    }

    @Test
    @Transactional
    public void deleteTourImage() throws Exception {
        // Initialize the database
        tourImageRepository.saveAndFlush(tourImage);
        int databaseSizeBeforeDelete = tourImageRepository.findAll().size();

        // Get the tourImage
        restTourImageMockMvc.perform(delete("/api/tour-images/{id}", tourImage.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TourImage> tourImages = tourImageRepository.findAll();
        assertThat(tourImages).hasSize(databaseSizeBeforeDelete - 1);
    }
}
