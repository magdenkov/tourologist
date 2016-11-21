package tech.bubbl.tourologist.web.rest;

import tech.bubbl.tourologist.TourologistApp;

import tech.bubbl.tourologist.domain.Bubbl;
import tech.bubbl.tourologist.repository.BubblRepository;
import tech.bubbl.tourologist.service.BubblService;
import tech.bubbl.tourologist.service.dto.BubblDTO;
import tech.bubbl.tourologist.service.mapper.BubblMapper;

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

import tech.bubbl.tourologist.domain.enumeration.Status;
/**
 * Test class for the BubblResource REST controller.
 *
 * @see BubblResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TourologistApp.class)
public class BubblResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.DRAFT;
    private static final Status UPDATED_STATUS = Status.SUBMITTED;

    private static final Double DEFAULT_LAT = 1D;
    private static final Double UPDATED_LAT = 2D;

    private static final Double DEFAULT_LNG = 1D;
    private static final Double UPDATED_LNG = 2D;

    private static final Integer DEFAULT_RADIUS_METERS = 0;
    private static final Integer UPDATED_RADIUS_METERS = 1;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATED_DATE_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_CREATED_DATE);

    private static final ZonedDateTime DEFAULT_LAST_MODIFIED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_LAST_MODIFIED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_LAST_MODIFIED_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_LAST_MODIFIED);

    private static final ZonedDateTime DEFAULT_DELETED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DELETED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DELETED_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_DELETED);

    @Inject
    private BubblRepository bubblRepository;

    @Inject
    private BubblMapper bubblMapper;

    @Inject
    private BubblService bubblService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBubblMockMvc;

    private Bubbl bubbl;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BubblResource bubblResource = new BubblResource();
        ReflectionTestUtils.setField(bubblResource, "bubblService", bubblService);
        this.restBubblMockMvc = MockMvcBuilders.standaloneSetup(bubblResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bubbl createEntity(EntityManager em) {
        Bubbl bubbl = new Bubbl()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .status(DEFAULT_STATUS)
                .lat(DEFAULT_LAT)
                .lng(DEFAULT_LNG)
                .radiusMeters(DEFAULT_RADIUS_METERS)
                .createdDate(DEFAULT_CREATED_DATE)
                .lastModified(DEFAULT_LAST_MODIFIED)
                .deleted(DEFAULT_DELETED);
        return bubbl;
    }

    @Before
    public void initTest() {
        bubbl = createEntity(em);
    }

    @Test
    @Transactional
    public void createBubbl() throws Exception {
        int databaseSizeBeforeCreate = bubblRepository.findAll().size();

        // Create the Bubbl
        BubblDTO bubblDTO = bubblMapper.bubblToBubblDTO(bubbl);

        restBubblMockMvc.perform(post("/api/bubbls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bubblDTO)))
                .andExpect(status().isCreated());

        // Validate the Bubbl in the database
        List<Bubbl> bubbls = bubblRepository.findAll();
        assertThat(bubbls).hasSize(databaseSizeBeforeCreate + 1);
        Bubbl testBubbl = bubbls.get(bubbls.size() - 1);
        assertThat(testBubbl.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBubbl.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBubbl.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testBubbl.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testBubbl.getLng()).isEqualTo(DEFAULT_LNG);
        assertThat(testBubbl.getRadiusMeters()).isEqualTo(DEFAULT_RADIUS_METERS);
        assertThat(testBubbl.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testBubbl.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testBubbl.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void getAllBubbls() throws Exception {
        // Initialize the database
        bubblRepository.saveAndFlush(bubbl);

        // Get all the bubbls
        restBubblMockMvc.perform(get("/api/bubbls?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bubbl.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
                .andExpect(jsonPath("$.[*].lng").value(hasItem(DEFAULT_LNG.doubleValue())))
                .andExpect(jsonPath("$.[*].radiusMeters").value(hasItem(DEFAULT_RADIUS_METERS)))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED_STR)))
                .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED_STR)));
    }

    @Test
    @Transactional
    public void getBubbl() throws Exception {
        // Initialize the database
        bubblRepository.saveAndFlush(bubbl);

        // Get the bubbl
        restBubblMockMvc.perform(get("/api/bubbls/{id}", bubbl.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bubbl.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT.doubleValue()))
            .andExpect(jsonPath("$.lng").value(DEFAULT_LNG.doubleValue()))
            .andExpect(jsonPath("$.radiusMeters").value(DEFAULT_RADIUS_METERS))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE_STR))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED_STR))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED_STR));
    }

    @Test
    @Transactional
    public void getNonExistingBubbl() throws Exception {
        // Get the bubbl
        restBubblMockMvc.perform(get("/api/bubbls/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBubbl() throws Exception {
        // Initialize the database
        bubblRepository.saveAndFlush(bubbl);
        int databaseSizeBeforeUpdate = bubblRepository.findAll().size();

        // Update the bubbl
        Bubbl updatedBubbl = bubblRepository.findOne(bubbl.getId());
        updatedBubbl
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .status(UPDATED_STATUS)
                .lat(UPDATED_LAT)
                .lng(UPDATED_LNG)
                .radiusMeters(UPDATED_RADIUS_METERS)
                .createdDate(UPDATED_CREATED_DATE)
                .lastModified(UPDATED_LAST_MODIFIED)
                .deleted(UPDATED_DELETED);
        BubblDTO bubblDTO = bubblMapper.bubblToBubblDTO(updatedBubbl);

        restBubblMockMvc.perform(put("/api/bubbls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bubblDTO)))
                .andExpect(status().isOk());

        // Validate the Bubbl in the database
        List<Bubbl> bubbls = bubblRepository.findAll();
        assertThat(bubbls).hasSize(databaseSizeBeforeUpdate);
        Bubbl testBubbl = bubbls.get(bubbls.size() - 1);
        assertThat(testBubbl.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBubbl.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBubbl.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBubbl.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testBubbl.getLng()).isEqualTo(UPDATED_LNG);
        assertThat(testBubbl.getRadiusMeters()).isEqualTo(UPDATED_RADIUS_METERS);
        assertThat(testBubbl.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testBubbl.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testBubbl.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void deleteBubbl() throws Exception {
        // Initialize the database
        bubblRepository.saveAndFlush(bubbl);
        int databaseSizeBeforeDelete = bubblRepository.findAll().size();

        // Get the bubbl
        restBubblMockMvc.perform(delete("/api/bubbls/{id}", bubbl.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Bubbl> bubbls = bubblRepository.findAll();
        assertThat(bubbls).hasSize(databaseSizeBeforeDelete - 1);
    }
}
