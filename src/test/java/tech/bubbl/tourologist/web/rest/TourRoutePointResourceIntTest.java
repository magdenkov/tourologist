package tech.bubbl.tourologist.web.rest;

import tech.bubbl.tourologist.TourologistApp;

import tech.bubbl.tourologist.domain.TourRoutePoint;
import tech.bubbl.tourologist.repository.TourRoutePointRepository;
import tech.bubbl.tourologist.service.TourRoutePointService;
import tech.bubbl.tourologist.service.dto.TourRoutePointDTO;
import tech.bubbl.tourologist.service.mapper.TourRoutePointMapper;

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
 * Test class for the TourRoutePointResource REST controller.
 *
 * @see TourRoutePointResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TourologistApp.class)
public class TourRoutePointResourceIntTest {

    private static final Double DEFAULT_LAT = 1D;
    private static final Double UPDATED_LAT = 2D;

    private static final Double DEFAULT_LNG = 1D;
    private static final Double UPDATED_LNG = 2D;

    private static final Integer DEFAULT_ORDER_NUMBER = 0;
    private static final Integer UPDATED_ORDER_NUMBER = 1;

    @Inject
    private TourRoutePointRepository tourRoutePointRepository;

    @Inject
    private TourRoutePointMapper tourRoutePointMapper;

    @Inject
    private TourRoutePointService tourRoutePointService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTourRoutePointMockMvc;

    private TourRoutePoint tourRoutePoint;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TourRoutePointResource tourRoutePointResource = new TourRoutePointResource();
        ReflectionTestUtils.setField(tourRoutePointResource, "tourRoutePointService", tourRoutePointService);
        this.restTourRoutePointMockMvc = MockMvcBuilders.standaloneSetup(tourRoutePointResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TourRoutePoint createEntity(EntityManager em) {
        TourRoutePoint tourRoutePoint = new TourRoutePoint()
                .lat(DEFAULT_LAT)
                .lng(DEFAULT_LNG)
                .orderNumber(DEFAULT_ORDER_NUMBER);
        return tourRoutePoint;
    }

    @Before
    public void initTest() {
        tourRoutePoint = createEntity(em);
    }

    @Test
    @Transactional
    public void createTourRoutePoint() throws Exception {
        int databaseSizeBeforeCreate = tourRoutePointRepository.findAll().size();

        // Create the TourRoutePoint
        TourRoutePointDTO tourRoutePointDTO = tourRoutePointMapper.tourRoutePointToTourRoutePointDTO(tourRoutePoint);

        restTourRoutePointMockMvc.perform(post("/api/tour-route-points")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tourRoutePointDTO)))
                .andExpect(status().isCreated());

        // Validate the TourRoutePoint in the database
        List<TourRoutePoint> tourRoutePoints = tourRoutePointRepository.findAll();
        assertThat(tourRoutePoints).hasSize(databaseSizeBeforeCreate + 1);
        TourRoutePoint testTourRoutePoint = tourRoutePoints.get(tourRoutePoints.size() - 1);
        assertThat(testTourRoutePoint.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testTourRoutePoint.getLng()).isEqualTo(DEFAULT_LNG);
        assertThat(testTourRoutePoint.getOrderNumber()).isEqualTo(DEFAULT_ORDER_NUMBER);
    }

    @Test
    @Transactional
    public void getAllTourRoutePoints() throws Exception {
        // Initialize the database
        tourRoutePointRepository.saveAndFlush(tourRoutePoint);

        // Get all the tourRoutePoints
        restTourRoutePointMockMvc.perform(get("/api/tour-route-points?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tourRoutePoint.getId().intValue())))
                .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
                .andExpect(jsonPath("$.[*].lng").value(hasItem(DEFAULT_LNG.doubleValue())))
                .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER)));
    }

    @Test
    @Transactional
    public void getTourRoutePoint() throws Exception {
        // Initialize the database
        tourRoutePointRepository.saveAndFlush(tourRoutePoint);

        // Get the tourRoutePoint
        restTourRoutePointMockMvc.perform(get("/api/tour-route-points/{id}", tourRoutePoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tourRoutePoint.getId().intValue()))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT.doubleValue()))
            .andExpect(jsonPath("$.lng").value(DEFAULT_LNG.doubleValue()))
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER));
    }

    @Test
    @Transactional
    public void getNonExistingTourRoutePoint() throws Exception {
        // Get the tourRoutePoint
        restTourRoutePointMockMvc.perform(get("/api/tour-route-points/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTourRoutePoint() throws Exception {
        // Initialize the database
        tourRoutePointRepository.saveAndFlush(tourRoutePoint);
        int databaseSizeBeforeUpdate = tourRoutePointRepository.findAll().size();

        // Update the tourRoutePoint
        TourRoutePoint updatedTourRoutePoint = tourRoutePointRepository.findOne(tourRoutePoint.getId());
        updatedTourRoutePoint
                .lat(UPDATED_LAT)
                .lng(UPDATED_LNG)
                .orderNumber(UPDATED_ORDER_NUMBER);
        TourRoutePointDTO tourRoutePointDTO = tourRoutePointMapper.tourRoutePointToTourRoutePointDTO(updatedTourRoutePoint);

        restTourRoutePointMockMvc.perform(put("/api/tour-route-points")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tourRoutePointDTO)))
                .andExpect(status().isOk());

        // Validate the TourRoutePoint in the database
        List<TourRoutePoint> tourRoutePoints = tourRoutePointRepository.findAll();
        assertThat(tourRoutePoints).hasSize(databaseSizeBeforeUpdate);
        TourRoutePoint testTourRoutePoint = tourRoutePoints.get(tourRoutePoints.size() - 1);
        assertThat(testTourRoutePoint.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testTourRoutePoint.getLng()).isEqualTo(UPDATED_LNG);
        assertThat(testTourRoutePoint.getOrderNumber()).isEqualTo(UPDATED_ORDER_NUMBER);
    }

    @Test
    @Transactional
    public void deleteTourRoutePoint() throws Exception {
        // Initialize the database
        tourRoutePointRepository.saveAndFlush(tourRoutePoint);
        int databaseSizeBeforeDelete = tourRoutePointRepository.findAll().size();

        // Get the tourRoutePoint
        restTourRoutePointMockMvc.perform(delete("/api/tour-route-points/{id}", tourRoutePoint.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TourRoutePoint> tourRoutePoints = tourRoutePointRepository.findAll();
        assertThat(tourRoutePoints).hasSize(databaseSizeBeforeDelete - 1);
    }
}
