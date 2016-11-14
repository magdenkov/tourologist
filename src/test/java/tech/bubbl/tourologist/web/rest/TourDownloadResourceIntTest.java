package tech.bubbl.tourologist.web.rest;

import tech.bubbl.tourologist.TourologistApp;

import tech.bubbl.tourologist.domain.TourDownload;
import tech.bubbl.tourologist.repository.TourDownloadRepository;
import tech.bubbl.tourologist.service.TourDownloadService;
import tech.bubbl.tourologist.service.dto.TourDownloadDTO;
import tech.bubbl.tourologist.service.mapper.TourDownloadMapper;

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
 * Test class for the TourDownloadResource REST controller.
 *
 * @see TourDownloadResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TourologistApp.class)
public class TourDownloadResourceIntTest {

    private static final ZonedDateTime DEFAULT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_TIME_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_TIME);

    @Inject
    private TourDownloadRepository tourDownloadRepository;

    @Inject
    private TourDownloadMapper tourDownloadMapper;

    @Inject
    private TourDownloadService tourDownloadService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTourDownloadMockMvc;

    private TourDownload tourDownload;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TourDownloadResource tourDownloadResource = new TourDownloadResource();
        ReflectionTestUtils.setField(tourDownloadResource, "tourDownloadService", tourDownloadService);
        this.restTourDownloadMockMvc = MockMvcBuilders.standaloneSetup(tourDownloadResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TourDownload createEntity(EntityManager em) {
        TourDownload tourDownload = new TourDownload()
                .time(DEFAULT_TIME);
        return tourDownload;
    }

    @Before
    public void initTest() {
        tourDownload = createEntity(em);
    }

    @Test
    @Transactional
    public void createTourDownload() throws Exception {
        int databaseSizeBeforeCreate = tourDownloadRepository.findAll().size();

        // Create the TourDownload
        TourDownloadDTO tourDownloadDTO = tourDownloadMapper.tourDownloadToTourDownloadDTO(tourDownload);

        restTourDownloadMockMvc.perform(post("/api/tour-downloads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tourDownloadDTO)))
                .andExpect(status().isCreated());

        // Validate the TourDownload in the database
        List<TourDownload> tourDownloads = tourDownloadRepository.findAll();
        assertThat(tourDownloads).hasSize(databaseSizeBeforeCreate + 1);
        TourDownload testTourDownload = tourDownloads.get(tourDownloads.size() - 1);
        assertThat(testTourDownload.getTime()).isEqualTo(DEFAULT_TIME);
    }

    @Test
    @Transactional
    public void checkTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = tourDownloadRepository.findAll().size();
        // set the field null
        tourDownload.setTime(null);

        // Create the TourDownload, which fails.
        TourDownloadDTO tourDownloadDTO = tourDownloadMapper.tourDownloadToTourDownloadDTO(tourDownload);

        restTourDownloadMockMvc.perform(post("/api/tour-downloads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tourDownloadDTO)))
                .andExpect(status().isBadRequest());

        List<TourDownload> tourDownloads = tourDownloadRepository.findAll();
        assertThat(tourDownloads).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTourDownloads() throws Exception {
        // Initialize the database
        tourDownloadRepository.saveAndFlush(tourDownload);

        // Get all the tourDownloads
        restTourDownloadMockMvc.perform(get("/api/tour-downloads?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tourDownload.getId().intValue())))
                .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME_STR)));
    }

    @Test
    @Transactional
    public void getTourDownload() throws Exception {
        // Initialize the database
        tourDownloadRepository.saveAndFlush(tourDownload);

        // Get the tourDownload
        restTourDownloadMockMvc.perform(get("/api/tour-downloads/{id}", tourDownload.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tourDownload.getId().intValue()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingTourDownload() throws Exception {
        // Get the tourDownload
        restTourDownloadMockMvc.perform(get("/api/tour-downloads/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTourDownload() throws Exception {
        // Initialize the database
        tourDownloadRepository.saveAndFlush(tourDownload);
        int databaseSizeBeforeUpdate = tourDownloadRepository.findAll().size();

        // Update the tourDownload
        TourDownload updatedTourDownload = tourDownloadRepository.findOne(tourDownload.getId());
        updatedTourDownload
                .time(UPDATED_TIME);
        TourDownloadDTO tourDownloadDTO = tourDownloadMapper.tourDownloadToTourDownloadDTO(updatedTourDownload);

        restTourDownloadMockMvc.perform(put("/api/tour-downloads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tourDownloadDTO)))
                .andExpect(status().isOk());

        // Validate the TourDownload in the database
        List<TourDownload> tourDownloads = tourDownloadRepository.findAll();
        assertThat(tourDownloads).hasSize(databaseSizeBeforeUpdate);
        TourDownload testTourDownload = tourDownloads.get(tourDownloads.size() - 1);
        assertThat(testTourDownload.getTime()).isEqualTo(UPDATED_TIME);
    }

    @Test
    @Transactional
    public void deleteTourDownload() throws Exception {
        // Initialize the database
        tourDownloadRepository.saveAndFlush(tourDownload);
        int databaseSizeBeforeDelete = tourDownloadRepository.findAll().size();

        // Get the tourDownload
        restTourDownloadMockMvc.perform(delete("/api/tour-downloads/{id}", tourDownload.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TourDownload> tourDownloads = tourDownloadRepository.findAll();
        assertThat(tourDownloads).hasSize(databaseSizeBeforeDelete - 1);
    }
}
