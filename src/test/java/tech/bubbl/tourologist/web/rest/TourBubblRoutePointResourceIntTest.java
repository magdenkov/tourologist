package tech.bubbl.tourologist.web.rest;

import tech.bubbl.tourologist.TourologistApp;

import tech.bubbl.tourologist.domain.TourBubblRoutePoint;
import tech.bubbl.tourologist.repository.TourBubblRoutePointRepository;
import tech.bubbl.tourologist.service.TourBubblRoutePointService;
import tech.bubbl.tourologist.service.dto.TourBubblRoutePointDTO;
import tech.bubbl.tourologist.service.mapper.TourBubblRoutePointMapper;

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
 * Test class for the TourBubblRoutePointResource REST controller.
 *
 * @see TourBubblRoutePointResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TourologistApp.class)
public class TourBubblRoutePointResourceIntTest {

    private static final Double DEFAULT_LAT = 1D;
    private static final Double UPDATED_LAT = 2D;

    private static final Double DEFAULT_LNG = 1D;
    private static final Double UPDATED_LNG = 2D;

    private static final Integer DEFAULT_ORDER_NUMBER = 0;
    private static final Integer UPDATED_ORDER_NUMBER = 1;

    @Inject
    private TourBubblRoutePointRepository tourBubblRoutePointRepository;

    @Inject
    private TourBubblRoutePointMapper tourBubblRoutePointMapper;

    @Inject
    private TourBubblRoutePointService tourBubblRoutePointService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTourBubblRoutePointMockMvc;

    private TourBubblRoutePoint tourBubblRoutePoint;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TourBubblRoutePointResource tourBubblRoutePointResource = new TourBubblRoutePointResource();
        ReflectionTestUtils.setField(tourBubblRoutePointResource, "tourBubblRoutePointService", tourBubblRoutePointService);
        this.restTourBubblRoutePointMockMvc = MockMvcBuilders.standaloneSetup(tourBubblRoutePointResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TourBubblRoutePoint createEntity(EntityManager em) {
        TourBubblRoutePoint tourBubblRoutePoint = new TourBubblRoutePoint()
                .lat(DEFAULT_LAT)
                .lng(DEFAULT_LNG)
                .orderNumber(DEFAULT_ORDER_NUMBER);
        return tourBubblRoutePoint;
    }

    @Before
    public void initTest() {
        tourBubblRoutePoint = createEntity(em);
    }

    @Test
    @Transactional
    public void createTourBubblRoutePoint() throws Exception {
        int databaseSizeBeforeCreate = tourBubblRoutePointRepository.findAll().size();

        // Create the TourBubblRoutePoint
        TourBubblRoutePointDTO tourBubblRoutePointDTO = tourBubblRoutePointMapper.tourBubblRoutePointToTourBubblRoutePointDTO(tourBubblRoutePoint);

        restTourBubblRoutePointMockMvc.perform(post("/api/tour-bubbl-route-points")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tourBubblRoutePointDTO)))
                .andExpect(status().isCreated());

        // Validate the TourBubblRoutePoint in the database
        List<TourBubblRoutePoint> tourBubblRoutePoints = tourBubblRoutePointRepository.findAll();
        assertThat(tourBubblRoutePoints).hasSize(databaseSizeBeforeCreate + 1);
        TourBubblRoutePoint testTourBubblRoutePoint = tourBubblRoutePoints.get(tourBubblRoutePoints.size() - 1);
        assertThat(testTourBubblRoutePoint.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testTourBubblRoutePoint.getLng()).isEqualTo(DEFAULT_LNG);
        assertThat(testTourBubblRoutePoint.getOrderNumber()).isEqualTo(DEFAULT_ORDER_NUMBER);
    }

    @Test
    @Transactional
    public void getAllTourBubblRoutePoints() throws Exception {
        // Initialize the database
        tourBubblRoutePointRepository.saveAndFlush(tourBubblRoutePoint);

        // Get all the tourBubblRoutePoints
        restTourBubblRoutePointMockMvc.perform(get("/api/tour-bubbl-route-points?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tourBubblRoutePoint.getId().intValue())))
                .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
                .andExpect(jsonPath("$.[*].lng").value(hasItem(DEFAULT_LNG.doubleValue())))
                .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER)));
    }

    @Test
    @Transactional
    public void getTourBubblRoutePoint() throws Exception {
        // Initialize the database
        tourBubblRoutePointRepository.saveAndFlush(tourBubblRoutePoint);

        // Get the tourBubblRoutePoint
        restTourBubblRoutePointMockMvc.perform(get("/api/tour-bubbl-route-points/{id}", tourBubblRoutePoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tourBubblRoutePoint.getId().intValue()))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT.doubleValue()))
            .andExpect(jsonPath("$.lng").value(DEFAULT_LNG.doubleValue()))
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER));
    }

    @Test
    @Transactional
    public void getNonExistingTourBubblRoutePoint() throws Exception {
        // Get the tourBubblRoutePoint
        restTourBubblRoutePointMockMvc.perform(get("/api/tour-bubbl-route-points/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTourBubblRoutePoint() throws Exception {
        // Initialize the database
        tourBubblRoutePointRepository.saveAndFlush(tourBubblRoutePoint);
        int databaseSizeBeforeUpdate = tourBubblRoutePointRepository.findAll().size();

        // Update the tourBubblRoutePoint
        TourBubblRoutePoint updatedTourBubblRoutePoint = tourBubblRoutePointRepository.findOne(tourBubblRoutePoint.getId());
        updatedTourBubblRoutePoint
                .lat(UPDATED_LAT)
                .lng(UPDATED_LNG)
                .orderNumber(UPDATED_ORDER_NUMBER);
        TourBubblRoutePointDTO tourBubblRoutePointDTO = tourBubblRoutePointMapper.tourBubblRoutePointToTourBubblRoutePointDTO(updatedTourBubblRoutePoint);

        restTourBubblRoutePointMockMvc.perform(put("/api/tour-bubbl-route-points")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tourBubblRoutePointDTO)))
                .andExpect(status().isOk());

        // Validate the TourBubblRoutePoint in the database
        List<TourBubblRoutePoint> tourBubblRoutePoints = tourBubblRoutePointRepository.findAll();
        assertThat(tourBubblRoutePoints).hasSize(databaseSizeBeforeUpdate);
        TourBubblRoutePoint testTourBubblRoutePoint = tourBubblRoutePoints.get(tourBubblRoutePoints.size() - 1);
        assertThat(testTourBubblRoutePoint.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testTourBubblRoutePoint.getLng()).isEqualTo(UPDATED_LNG);
        assertThat(testTourBubblRoutePoint.getOrderNumber()).isEqualTo(UPDATED_ORDER_NUMBER);
    }

    @Test
    @Transactional
    public void deleteTourBubblRoutePoint() throws Exception {
        // Initialize the database
        tourBubblRoutePointRepository.saveAndFlush(tourBubblRoutePoint);
        int databaseSizeBeforeDelete = tourBubblRoutePointRepository.findAll().size();

        // Get the tourBubblRoutePoint
        restTourBubblRoutePointMockMvc.perform(delete("/api/tour-bubbl-route-points/{id}", tourBubblRoutePoint.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TourBubblRoutePoint> tourBubblRoutePoints = tourBubblRoutePointRepository.findAll();
        assertThat(tourBubblRoutePoints).hasSize(databaseSizeBeforeDelete - 1);
    }
}
