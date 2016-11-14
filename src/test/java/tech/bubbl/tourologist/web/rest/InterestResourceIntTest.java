package tech.bubbl.tourologist.web.rest;

import tech.bubbl.tourologist.TourologistApp;

import tech.bubbl.tourologist.domain.Interest;
import tech.bubbl.tourologist.repository.InterestRepository;
import tech.bubbl.tourologist.service.InterestService;
import tech.bubbl.tourologist.service.dto.InterestDTO;
import tech.bubbl.tourologist.service.mapper.InterestMapper;

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
 * Test class for the InterestResource REST controller.
 *
 * @see InterestResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TourologistApp.class)
public class InterestResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final String DEFAULT_ICON = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private InterestRepository interestRepository;

    @Inject
    private InterestMapper interestMapper;

    @Inject
    private InterestService interestService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restInterestMockMvc;

    private Interest interest;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InterestResource interestResource = new InterestResource();
        ReflectionTestUtils.setField(interestResource, "interestService", interestService);
        this.restInterestMockMvc = MockMvcBuilders.standaloneSetup(interestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Interest createEntity(EntityManager em) {
        Interest interest = new Interest()
                .name(DEFAULT_NAME)
                .icon(DEFAULT_ICON);
        return interest;
    }

    @Before
    public void initTest() {
        interest = createEntity(em);
    }

    @Test
    @Transactional
    public void createInterest() throws Exception {
        int databaseSizeBeforeCreate = interestRepository.findAll().size();

        // Create the Interest
        InterestDTO interestDTO = interestMapper.interestToInterestDTO(interest);

        restInterestMockMvc.perform(post("/api/interests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(interestDTO)))
                .andExpect(status().isCreated());

        // Validate the Interest in the database
        List<Interest> interests = interestRepository.findAll();
        assertThat(interests).hasSize(databaseSizeBeforeCreate + 1);
        Interest testInterest = interests.get(interests.size() - 1);
        assertThat(testInterest.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInterest.getIcon()).isEqualTo(DEFAULT_ICON);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = interestRepository.findAll().size();
        // set the field null
        interest.setName(null);

        // Create the Interest, which fails.
        InterestDTO interestDTO = interestMapper.interestToInterestDTO(interest);

        restInterestMockMvc.perform(post("/api/interests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(interestDTO)))
                .andExpect(status().isBadRequest());

        List<Interest> interests = interestRepository.findAll();
        assertThat(interests).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInterests() throws Exception {
        // Initialize the database
        interestRepository.saveAndFlush(interest);

        // Get all the interests
        restInterestMockMvc.perform(get("/api/interests?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(interest.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON.toString())));
    }

    @Test
    @Transactional
    public void getInterest() throws Exception {
        // Initialize the database
        interestRepository.saveAndFlush(interest);

        // Get the interest
        restInterestMockMvc.perform(get("/api/interests/{id}", interest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(interest.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInterest() throws Exception {
        // Get the interest
        restInterestMockMvc.perform(get("/api/interests/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInterest() throws Exception {
        // Initialize the database
        interestRepository.saveAndFlush(interest);
        int databaseSizeBeforeUpdate = interestRepository.findAll().size();

        // Update the interest
        Interest updatedInterest = interestRepository.findOne(interest.getId());
        updatedInterest
                .name(UPDATED_NAME)
                .icon(UPDATED_ICON);
        InterestDTO interestDTO = interestMapper.interestToInterestDTO(updatedInterest);

        restInterestMockMvc.perform(put("/api/interests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(interestDTO)))
                .andExpect(status().isOk());

        // Validate the Interest in the database
        List<Interest> interests = interestRepository.findAll();
        assertThat(interests).hasSize(databaseSizeBeforeUpdate);
        Interest testInterest = interests.get(interests.size() - 1);
        assertThat(testInterest.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInterest.getIcon()).isEqualTo(UPDATED_ICON);
    }

    @Test
    @Transactional
    public void deleteInterest() throws Exception {
        // Initialize the database
        interestRepository.saveAndFlush(interest);
        int databaseSizeBeforeDelete = interestRepository.findAll().size();

        // Get the interest
        restInterestMockMvc.perform(delete("/api/interests/{id}", interest.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Interest> interests = interestRepository.findAll();
        assertThat(interests).hasSize(databaseSizeBeforeDelete - 1);
    }
}
