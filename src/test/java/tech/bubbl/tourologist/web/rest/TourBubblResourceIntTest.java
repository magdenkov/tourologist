package tech.bubbl.tourologist.web.rest;

import tech.bubbl.tourologist.TourologistApp;

import tech.bubbl.tourologist.domain.TourBubbl;
import tech.bubbl.tourologist.repository.TourBubblRepository;
import tech.bubbl.tourologist.service.TourBubblService;
import tech.bubbl.tourologist.service.dto.TourBubblDTO;
import tech.bubbl.tourologist.service.mapper.TourBubblMapper;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TourBubblResource REST controller.
 *
 * @see TourBubblResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TourologistApp.class)
public class TourBubblResourceIntTest {

    private static final Integer DEFAULT_ORDER_NUMBER = 0;
    private static final Integer UPDATED_ORDER_NUMBER = 1;

    @Inject
    private TourBubblRepository tourBubblRepository;

    @Inject
    private TourBubblMapper tourBubblMapper;

    @Inject
    private TourBubblService tourBubblService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTourBubblMockMvc;

    private TourBubbl tourBubbl;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TourBubblResource tourBubblResource = new TourBubblResource();
        ReflectionTestUtils.setField(tourBubblResource, "tourBubblService", tourBubblService);
        this.restTourBubblMockMvc = MockMvcBuilders.standaloneSetup(tourBubblResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TourBubbl createEntity(EntityManager em) {
        TourBubbl tourBubbl = new TourBubbl()
                .orderNumber(DEFAULT_ORDER_NUMBER);
        return tourBubbl;
    }

    @Before
    public void initTest() {
        tourBubbl = createEntity(em);
    }

    @Test
    @Transactional
    public void createTourBubbl() throws Exception {
        int databaseSizeBeforeCreate = tourBubblRepository.findAll().size();

        // Create the TourBubbl
        TourBubblDTO tourBubblDTO = tourBubblMapper.tourBubblToTourBubblDTO(tourBubbl);

        restTourBubblMockMvc.perform(post("/api/tour-bubbls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tourBubblDTO)))
                .andExpect(status().isCreated());

        // Validate the TourBubbl in the database
        List<TourBubbl> tourBubbls = tourBubblRepository.findAll();
        assertThat(tourBubbls).hasSize(databaseSizeBeforeCreate + 1);
        TourBubbl testTourBubbl = tourBubbls.get(tourBubbls.size() - 1);
        assertThat(testTourBubbl.getOrderNumber()).isEqualTo(DEFAULT_ORDER_NUMBER);
    }

    @Test
    @Transactional
    public void getAllTourBubbls() throws Exception {
        // Initialize the database
        tourBubblRepository.saveAndFlush(tourBubbl);

        // Get all the tourBubbls
        restTourBubblMockMvc.perform(get("/api/tour-bubbls?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tourBubbl.getId().intValue())))
                .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER)));
    }

    @Test
    @Transactional
    public void getTourBubbl() throws Exception {
        // Initialize the database
        tourBubblRepository.saveAndFlush(tourBubbl);

        // Get the tourBubbl
        restTourBubblMockMvc.perform(get("/api/tour-bubbls/{id}", tourBubbl.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tourBubbl.getId().intValue()))
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER));
    }

    @Test
    @Transactional
    public void getNonExistingTourBubbl() throws Exception {
        // Get the tourBubbl
        restTourBubblMockMvc.perform(get("/api/tour-bubbls/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTourBubbl() throws Exception {
        // Initialize the database
        tourBubblRepository.saveAndFlush(tourBubbl);
        int databaseSizeBeforeUpdate = tourBubblRepository.findAll().size();

        // Update the tourBubbl
        TourBubbl updatedTourBubbl = tourBubblRepository.findOne(tourBubbl.getId());
        updatedTourBubbl
                .orderNumber(UPDATED_ORDER_NUMBER);
        TourBubblDTO tourBubblDTO = tourBubblMapper.tourBubblToTourBubblDTO(updatedTourBubbl);

        restTourBubblMockMvc.perform(put("/api/tour-bubbls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tourBubblDTO)))
                .andExpect(status().isOk());

        // Validate the TourBubbl in the database
        List<TourBubbl> tourBubbls = tourBubblRepository.findAll();
        assertThat(tourBubbls).hasSize(databaseSizeBeforeUpdate);
        TourBubbl testTourBubbl = tourBubbls.get(tourBubbls.size() - 1);
        assertThat(testTourBubbl.getOrderNumber()).isEqualTo(UPDATED_ORDER_NUMBER);
    }

    @Test
    @Transactional
    public void deleteTourBubbl() throws Exception {
        // Initialize the database
        tourBubblRepository.saveAndFlush(tourBubbl);
        int databaseSizeBeforeDelete = tourBubblRepository.findAll().size();

        // Get the tourBubbl
        restTourBubblMockMvc.perform(delete("/api/tour-bubbls/{id}", tourBubbl.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TourBubbl> tourBubbls = tourBubblRepository.findAll();
        assertThat(tourBubbls).hasSize(databaseSizeBeforeDelete - 1);
    }
}
