package tech.bubbl.tourologist.web.rest;

import tech.bubbl.tourologist.TourologistApp;

import tech.bubbl.tourologist.domain.BubblDownload;
import tech.bubbl.tourologist.repository.BubblDownloadRepository;
import tech.bubbl.tourologist.service.BubblDownloadService;
import tech.bubbl.tourologist.service.dto.BubblDownloadDTO;
import tech.bubbl.tourologist.service.mapper.BubblDownloadMapper;

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
 * Test class for the BubblDownloadResource REST controller.
 *
 * @see BubblDownloadResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TourologistApp.class)
public class BubblDownloadResourceIntTest {

    private static final ZonedDateTime DEFAULT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_TIME_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_TIME);

    @Inject
    private BubblDownloadRepository bubblDownloadRepository;

    @Inject
    private BubblDownloadMapper bubblDownloadMapper;

    @Inject
    private BubblDownloadService bubblDownloadService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBubblDownloadMockMvc;

    private BubblDownload bubblDownload;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BubblDownloadResource bubblDownloadResource = new BubblDownloadResource();
        ReflectionTestUtils.setField(bubblDownloadResource, "bubblDownloadService", bubblDownloadService);
        this.restBubblDownloadMockMvc = MockMvcBuilders.standaloneSetup(bubblDownloadResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BubblDownload createEntity(EntityManager em) {
        BubblDownload bubblDownload = new BubblDownload()
                .time(DEFAULT_TIME);
        return bubblDownload;
    }

    @Before
    public void initTest() {
        bubblDownload = createEntity(em);
    }

    @Test
    @Transactional
    public void createBubblDownload() throws Exception {
        int databaseSizeBeforeCreate = bubblDownloadRepository.findAll().size();

        // Create the BubblDownload
        BubblDownloadDTO bubblDownloadDTO = bubblDownloadMapper.bubblDownloadToBubblDownloadDTO(bubblDownload);

        restBubblDownloadMockMvc.perform(post("/api/bubbl-downloads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bubblDownloadDTO)))
                .andExpect(status().isCreated());

        // Validate the BubblDownload in the database
        List<BubblDownload> bubblDownloads = bubblDownloadRepository.findAll();
        assertThat(bubblDownloads).hasSize(databaseSizeBeforeCreate + 1);
        BubblDownload testBubblDownload = bubblDownloads.get(bubblDownloads.size() - 1);
        assertThat(testBubblDownload.getTime()).isEqualTo(DEFAULT_TIME);
    }

    @Test
    @Transactional
    public void getAllBubblDownloads() throws Exception {
        // Initialize the database
        bubblDownloadRepository.saveAndFlush(bubblDownload);

        // Get all the bubblDownloads
        restBubblDownloadMockMvc.perform(get("/api/bubbl-downloads?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bubblDownload.getId().intValue())))
                .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME_STR)));
    }

    @Test
    @Transactional
    public void getBubblDownload() throws Exception {
        // Initialize the database
        bubblDownloadRepository.saveAndFlush(bubblDownload);

        // Get the bubblDownload
        restBubblDownloadMockMvc.perform(get("/api/bubbl-downloads/{id}", bubblDownload.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bubblDownload.getId().intValue()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingBubblDownload() throws Exception {
        // Get the bubblDownload
        restBubblDownloadMockMvc.perform(get("/api/bubbl-downloads/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBubblDownload() throws Exception {
        // Initialize the database
        bubblDownloadRepository.saveAndFlush(bubblDownload);
        int databaseSizeBeforeUpdate = bubblDownloadRepository.findAll().size();

        // Update the bubblDownload
        BubblDownload updatedBubblDownload = bubblDownloadRepository.findOne(bubblDownload.getId());
        updatedBubblDownload
                .time(UPDATED_TIME);
        BubblDownloadDTO bubblDownloadDTO = bubblDownloadMapper.bubblDownloadToBubblDownloadDTO(updatedBubblDownload);

        restBubblDownloadMockMvc.perform(put("/api/bubbl-downloads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bubblDownloadDTO)))
                .andExpect(status().isOk());

        // Validate the BubblDownload in the database
        List<BubblDownload> bubblDownloads = bubblDownloadRepository.findAll();
        assertThat(bubblDownloads).hasSize(databaseSizeBeforeUpdate);
        BubblDownload testBubblDownload = bubblDownloads.get(bubblDownloads.size() - 1);
        assertThat(testBubblDownload.getTime()).isEqualTo(UPDATED_TIME);
    }

    @Test
    @Transactional
    public void deleteBubblDownload() throws Exception {
        // Initialize the database
        bubblDownloadRepository.saveAndFlush(bubblDownload);
        int databaseSizeBeforeDelete = bubblDownloadRepository.findAll().size();

        // Get the bubblDownload
        restBubblDownloadMockMvc.perform(delete("/api/bubbl-downloads/{id}", bubblDownload.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<BubblDownload> bubblDownloads = bubblDownloadRepository.findAll();
        assertThat(bubblDownloads).hasSize(databaseSizeBeforeDelete - 1);
    }
}
