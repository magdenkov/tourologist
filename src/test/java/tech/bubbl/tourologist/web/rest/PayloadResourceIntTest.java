package tech.bubbl.tourologist.web.rest;

import tech.bubbl.tourologist.TourologistApp;

import tech.bubbl.tourologist.domain.Payload;
import tech.bubbl.tourologist.repository.PayloadRepository;
import tech.bubbl.tourologist.service.PayloadService;
import tech.bubbl.tourologist.service.dto.PayloadDTO;
import tech.bubbl.tourologist.service.mapper.PayloadMapper;

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

import tech.bubbl.tourologist.domain.enumeration.PayloadType;
/**
 * Test class for the PayloadResource REST controller.
 *
 * @see PayloadResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TourologistApp.class)
public class PayloadResourceIntTest {

    private static final PayloadType DEFAULT_PAYLOAD_TYPE = PayloadType.OTHER;
    private static final PayloadType UPDATED_PAYLOAD_TYPE = PayloadType.AUDIO;

    private static final String DEFAULT_URL = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

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
    private PayloadRepository payloadRepository;

    @Inject
    private PayloadMapper payloadMapper;

    @Inject
    private PayloadService payloadService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPayloadMockMvc;

    private Payload payload;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PayloadResource payloadResource = new PayloadResource();
        ReflectionTestUtils.setField(payloadResource, "payloadService", payloadService);
        this.restPayloadMockMvc = MockMvcBuilders.standaloneSetup(payloadResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payload createEntity(EntityManager em) {
        Payload payload = new Payload()
                .payloadType(DEFAULT_PAYLOAD_TYPE)
                .url(DEFAULT_URL)
                .createdDate(DEFAULT_CREATED_DATE)
                .lastModified(DEFAULT_LAST_MODIFIED)
                .deleted(DEFAULT_DELETED);
        return payload;
    }

    @Before
    public void initTest() {
        payload = createEntity(em);
    }

    @Test
    @Transactional
    public void createPayload() throws Exception {
        int databaseSizeBeforeCreate = payloadRepository.findAll().size();

        // Create the Payload
        PayloadDTO payloadDTO = payloadMapper.payloadToPayloadDTO(payload);

        restPayloadMockMvc.perform(post("/api/payloads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(payloadDTO)))
                .andExpect(status().isCreated());

        // Validate the Payload in the database
        List<Payload> payloads = payloadRepository.findAll();
        assertThat(payloads).hasSize(databaseSizeBeforeCreate + 1);
        Payload testPayload = payloads.get(payloads.size() - 1);
        assertThat(testPayload.getPayloadType()).isEqualTo(DEFAULT_PAYLOAD_TYPE);
        assertThat(testPayload.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testPayload.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPayload.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testPayload.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void getAllPayloads() throws Exception {
        // Initialize the database
        payloadRepository.saveAndFlush(payload);

        // Get all the payloads
        restPayloadMockMvc.perform(get("/api/payloads?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(payload.getId().intValue())))
                .andExpect(jsonPath("$.[*].payloadType").value(hasItem(DEFAULT_PAYLOAD_TYPE.toString())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED_STR)))
                .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED_STR)));
    }

    @Test
    @Transactional
    public void getPayload() throws Exception {
        // Initialize the database
        payloadRepository.saveAndFlush(payload);

        // Get the payload
        restPayloadMockMvc.perform(get("/api/payloads/{id}", payload.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(payload.getId().intValue()))
            .andExpect(jsonPath("$.payloadType").value(DEFAULT_PAYLOAD_TYPE.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE_STR))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED_STR))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED_STR));
    }

    @Test
    @Transactional
    public void getNonExistingPayload() throws Exception {
        // Get the payload
        restPayloadMockMvc.perform(get("/api/payloads/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePayload() throws Exception {
        // Initialize the database
        payloadRepository.saveAndFlush(payload);
        int databaseSizeBeforeUpdate = payloadRepository.findAll().size();

        // Update the payload
        Payload updatedPayload = payloadRepository.findOne(payload.getId());
        updatedPayload
                .payloadType(UPDATED_PAYLOAD_TYPE)
                .url(UPDATED_URL)
                .createdDate(UPDATED_CREATED_DATE)
                .lastModified(UPDATED_LAST_MODIFIED)
                .deleted(UPDATED_DELETED);
        PayloadDTO payloadDTO = payloadMapper.payloadToPayloadDTO(updatedPayload);

        restPayloadMockMvc.perform(put("/api/payloads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(payloadDTO)))
                .andExpect(status().isOk());

        // Validate the Payload in the database
        List<Payload> payloads = payloadRepository.findAll();
        assertThat(payloads).hasSize(databaseSizeBeforeUpdate);
        Payload testPayload = payloads.get(payloads.size() - 1);
        assertThat(testPayload.getPayloadType()).isEqualTo(UPDATED_PAYLOAD_TYPE);
        assertThat(testPayload.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testPayload.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPayload.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testPayload.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void deletePayload() throws Exception {
        // Initialize the database
        payloadRepository.saveAndFlush(payload);
        int databaseSizeBeforeDelete = payloadRepository.findAll().size();

        // Get the payload
        restPayloadMockMvc.perform(delete("/api/payloads/{id}", payload.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Payload> payloads = payloadRepository.findAll();
        assertThat(payloads).hasSize(databaseSizeBeforeDelete - 1);
    }
}
