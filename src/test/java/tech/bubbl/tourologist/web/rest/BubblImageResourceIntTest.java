package tech.bubbl.tourologist.web.rest;

import tech.bubbl.tourologist.TourologistApp;

import tech.bubbl.tourologist.domain.BubblImage;
import tech.bubbl.tourologist.repository.BubblImageRepository;
import tech.bubbl.tourologist.service.BubblImageService;
import tech.bubbl.tourologist.service.dto.BubblImageDTO;
import tech.bubbl.tourologist.service.mapper.BubblImageMapper;

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
 * Test class for the BubblImageResource REST controller.
 *
 * @see BubblImageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TourologistApp.class)
public class BubblImageResourceIntTest {

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
    private BubblImageRepository bubblImageRepository;

    @Inject
    private BubblImageMapper bubblImageMapper;

    @Inject
    private BubblImageService bubblImageService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBubblImageMockMvc;

    private BubblImage bubblImage;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BubblImageResource bubblImageResource = new BubblImageResource();
        ReflectionTestUtils.setField(bubblImageResource, "bubblImageService", bubblImageService);
        this.restBubblImageMockMvc = MockMvcBuilders.standaloneSetup(bubblImageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BubblImage createEntity(EntityManager em) {
        BubblImage bubblImage = new BubblImage()
                .name(DEFAULT_NAME)
                .uploaded(DEFAULT_UPLOADED)
                .s3Key(DEFAULT_S_3_KEY)
                .s3Bucket(DEFAULT_S_3_BUCKET)
                .s3ThumbKey(DEFAULT_S_3_THUMB_KEY)
                .s3Region(DEFAULT_S_3_REGION)
                .type(DEFAULT_TYPE)
                .master(DEFAULT_MASTER);
        return bubblImage;
    }

    @Before
    public void initTest() {
        bubblImage = createEntity(em);
    }

    @Test
    @Transactional
    public void createBubblImage() throws Exception {
        int databaseSizeBeforeCreate = bubblImageRepository.findAll().size();

        // Create the BubblImage
        BubblImageDTO bubblImageDTO = bubblImageMapper.bubblImageToBubblImageDTO(bubblImage);

        restBubblImageMockMvc.perform(post("/api/bubbl-images")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bubblImageDTO)))
                .andExpect(status().isCreated());

        // Validate the BubblImage in the database
        List<BubblImage> bubblImages = bubblImageRepository.findAll();
        assertThat(bubblImages).hasSize(databaseSizeBeforeCreate + 1);
        BubblImage testBubblImage = bubblImages.get(bubblImages.size() - 1);
        assertThat(testBubblImage.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBubblImage.getUploaded()).isEqualTo(DEFAULT_UPLOADED);
        assertThat(testBubblImage.gets3Key()).isEqualTo(DEFAULT_S_3_KEY);
        assertThat(testBubblImage.gets3Bucket()).isEqualTo(DEFAULT_S_3_BUCKET);
        assertThat(testBubblImage.gets3ThumbKey()).isEqualTo(DEFAULT_S_3_THUMB_KEY);
        assertThat(testBubblImage.gets3Region()).isEqualTo(DEFAULT_S_3_REGION);
        assertThat(testBubblImage.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testBubblImage.isMaster()).isEqualTo(DEFAULT_MASTER);
    }

    @Test
    @Transactional
    public void getAllBubblImages() throws Exception {
        // Initialize the database
        bubblImageRepository.saveAndFlush(bubblImage);

        // Get all the bubblImages
        restBubblImageMockMvc.perform(get("/api/bubbl-images?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bubblImage.getId().intValue())))
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
    public void getBubblImage() throws Exception {
        // Initialize the database
        bubblImageRepository.saveAndFlush(bubblImage);

        // Get the bubblImage
        restBubblImageMockMvc.perform(get("/api/bubbl-images/{id}", bubblImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bubblImage.getId().intValue()))
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
    public void getNonExistingBubblImage() throws Exception {
        // Get the bubblImage
        restBubblImageMockMvc.perform(get("/api/bubbl-images/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBubblImage() throws Exception {
        // Initialize the database
        bubblImageRepository.saveAndFlush(bubblImage);
        int databaseSizeBeforeUpdate = bubblImageRepository.findAll().size();

        // Update the bubblImage
        BubblImage updatedBubblImage = bubblImageRepository.findOne(bubblImage.getId());
        updatedBubblImage
                .name(UPDATED_NAME)
                .uploaded(UPDATED_UPLOADED)
                .s3Key(UPDATED_S_3_KEY)
                .s3Bucket(UPDATED_S_3_BUCKET)
                .s3ThumbKey(UPDATED_S_3_THUMB_KEY)
                .s3Region(UPDATED_S_3_REGION)
                .type(UPDATED_TYPE)
                .master(UPDATED_MASTER);
        BubblImageDTO bubblImageDTO = bubblImageMapper.bubblImageToBubblImageDTO(updatedBubblImage);

        restBubblImageMockMvc.perform(put("/api/bubbl-images")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bubblImageDTO)))
                .andExpect(status().isOk());

        // Validate the BubblImage in the database
        List<BubblImage> bubblImages = bubblImageRepository.findAll();
        assertThat(bubblImages).hasSize(databaseSizeBeforeUpdate);
        BubblImage testBubblImage = bubblImages.get(bubblImages.size() - 1);
        assertThat(testBubblImage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBubblImage.getUploaded()).isEqualTo(UPDATED_UPLOADED);
        assertThat(testBubblImage.gets3Key()).isEqualTo(UPDATED_S_3_KEY);
        assertThat(testBubblImage.gets3Bucket()).isEqualTo(UPDATED_S_3_BUCKET);
        assertThat(testBubblImage.gets3ThumbKey()).isEqualTo(UPDATED_S_3_THUMB_KEY);
        assertThat(testBubblImage.gets3Region()).isEqualTo(UPDATED_S_3_REGION);
        assertThat(testBubblImage.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testBubblImage.isMaster()).isEqualTo(UPDATED_MASTER);
    }

    @Test
    @Transactional
    public void deleteBubblImage() throws Exception {
        // Initialize the database
        bubblImageRepository.saveAndFlush(bubblImage);
        int databaseSizeBeforeDelete = bubblImageRepository.findAll().size();

        // Get the bubblImage
        restBubblImageMockMvc.perform(delete("/api/bubbl-images/{id}", bubblImage.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<BubblImage> bubblImages = bubblImageRepository.findAll();
        assertThat(bubblImages).hasSize(databaseSizeBeforeDelete - 1);
    }
}
