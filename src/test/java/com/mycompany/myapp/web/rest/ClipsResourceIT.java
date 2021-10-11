package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Clips;
import com.mycompany.myapp.repository.ClipsRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ClipsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClipsResourceIT {

    private static final Integer DEFAULT_USER_ID = 1;
    private static final Integer UPDATED_USER_ID = 2;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_CONTENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONTENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTENT_CONTENT_TYPE = "image/png";

    private static final Integer DEFAULT_POSITIVE_COUNT = 1;
    private static final Integer UPDATED_POSITIVE_COUNT = 2;

    private static final Integer DEFAULT_NEGATIVE_COUNT = 1;
    private static final Integer UPDATED_NEGATIVE_COUNT = 2;

    private static final String ENTITY_API_URL = "/api/clips";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClipsRepository clipsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClipsMockMvc;

    private Clips clips;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Clips createEntity(EntityManager em) {
        Clips clips = new Clips()
            .userId(DEFAULT_USER_ID)
            .name(DEFAULT_NAME)
            .content(DEFAULT_CONTENT)
            .contentContentType(DEFAULT_CONTENT_CONTENT_TYPE)
            .positiveCount(DEFAULT_POSITIVE_COUNT)
            .negativeCount(DEFAULT_NEGATIVE_COUNT);
        return clips;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Clips createUpdatedEntity(EntityManager em) {
        Clips clips = new Clips()
            .userId(UPDATED_USER_ID)
            .name(UPDATED_NAME)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .positiveCount(UPDATED_POSITIVE_COUNT)
            .negativeCount(UPDATED_NEGATIVE_COUNT);
        return clips;
    }

    @BeforeEach
    public void initTest() {
        clips = createEntity(em);
    }

    @Test
    @Transactional
    void createClips() throws Exception {
        int databaseSizeBeforeCreate = clipsRepository.findAll().size();
        // Create the Clips
        restClipsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clips)))
            .andExpect(status().isCreated());

        // Validate the Clips in the database
        List<Clips> clipsList = clipsRepository.findAll();
        assertThat(clipsList).hasSize(databaseSizeBeforeCreate + 1);
        Clips testClips = clipsList.get(clipsList.size() - 1);
        assertThat(testClips.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testClips.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testClips.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testClips.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);
        assertThat(testClips.getPositiveCount()).isEqualTo(DEFAULT_POSITIVE_COUNT);
        assertThat(testClips.getNegativeCount()).isEqualTo(DEFAULT_NEGATIVE_COUNT);
    }

    @Test
    @Transactional
    void createClipsWithExistingId() throws Exception {
        // Create the Clips with an existing ID
        clips.setId(1L);

        int databaseSizeBeforeCreate = clipsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClipsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clips)))
            .andExpect(status().isBadRequest());

        // Validate the Clips in the database
        List<Clips> clipsList = clipsRepository.findAll();
        assertThat(clipsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = clipsRepository.findAll().size();
        // set the field null
        clips.setUserId(null);

        // Create the Clips, which fails.

        restClipsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clips)))
            .andExpect(status().isBadRequest());

        List<Clips> clipsList = clipsRepository.findAll();
        assertThat(clipsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = clipsRepository.findAll().size();
        // set the field null
        clips.setName(null);

        // Create the Clips, which fails.

        restClipsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clips)))
            .andExpect(status().isBadRequest());

        List<Clips> clipsList = clipsRepository.findAll();
        assertThat(clipsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClips() throws Exception {
        // Initialize the database
        clipsRepository.saveAndFlush(clips);

        // Get all the clipsList
        restClipsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clips.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTENT))))
            .andExpect(jsonPath("$.[*].positiveCount").value(hasItem(DEFAULT_POSITIVE_COUNT)))
            .andExpect(jsonPath("$.[*].negativeCount").value(hasItem(DEFAULT_NEGATIVE_COUNT)));
    }

    @Test
    @Transactional
    void getClips() throws Exception {
        // Initialize the database
        clipsRepository.saveAndFlush(clips);

        // Get the clips
        restClipsMockMvc
            .perform(get(ENTITY_API_URL_ID, clips.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(clips.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.contentContentType").value(DEFAULT_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.content").value(Base64Utils.encodeToString(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.positiveCount").value(DEFAULT_POSITIVE_COUNT))
            .andExpect(jsonPath("$.negativeCount").value(DEFAULT_NEGATIVE_COUNT));
    }

    @Test
    @Transactional
    void getNonExistingClips() throws Exception {
        // Get the clips
        restClipsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewClips() throws Exception {
        // Initialize the database
        clipsRepository.saveAndFlush(clips);

        int databaseSizeBeforeUpdate = clipsRepository.findAll().size();

        // Update the clips
        Clips updatedClips = clipsRepository.findById(clips.getId()).get();
        // Disconnect from session so that the updates on updatedClips are not directly saved in db
        em.detach(updatedClips);
        updatedClips
            .userId(UPDATED_USER_ID)
            .name(UPDATED_NAME)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .positiveCount(UPDATED_POSITIVE_COUNT)
            .negativeCount(UPDATED_NEGATIVE_COUNT);

        restClipsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedClips.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedClips))
            )
            .andExpect(status().isOk());

        // Validate the Clips in the database
        List<Clips> clipsList = clipsRepository.findAll();
        assertThat(clipsList).hasSize(databaseSizeBeforeUpdate);
        Clips testClips = clipsList.get(clipsList.size() - 1);
        assertThat(testClips.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testClips.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClips.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testClips.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testClips.getPositiveCount()).isEqualTo(UPDATED_POSITIVE_COUNT);
        assertThat(testClips.getNegativeCount()).isEqualTo(UPDATED_NEGATIVE_COUNT);
    }

    @Test
    @Transactional
    void putNonExistingClips() throws Exception {
        int databaseSizeBeforeUpdate = clipsRepository.findAll().size();
        clips.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClipsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clips.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clips))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clips in the database
        List<Clips> clipsList = clipsRepository.findAll();
        assertThat(clipsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClips() throws Exception {
        int databaseSizeBeforeUpdate = clipsRepository.findAll().size();
        clips.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClipsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clips))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clips in the database
        List<Clips> clipsList = clipsRepository.findAll();
        assertThat(clipsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClips() throws Exception {
        int databaseSizeBeforeUpdate = clipsRepository.findAll().size();
        clips.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClipsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clips)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Clips in the database
        List<Clips> clipsList = clipsRepository.findAll();
        assertThat(clipsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClipsWithPatch() throws Exception {
        // Initialize the database
        clipsRepository.saveAndFlush(clips);

        int databaseSizeBeforeUpdate = clipsRepository.findAll().size();

        // Update the clips using partial update
        Clips partialUpdatedClips = new Clips();
        partialUpdatedClips.setId(clips.getId());

        partialUpdatedClips.userId(UPDATED_USER_ID).content(UPDATED_CONTENT).contentContentType(UPDATED_CONTENT_CONTENT_TYPE);

        restClipsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClips.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClips))
            )
            .andExpect(status().isOk());

        // Validate the Clips in the database
        List<Clips> clipsList = clipsRepository.findAll();
        assertThat(clipsList).hasSize(databaseSizeBeforeUpdate);
        Clips testClips = clipsList.get(clipsList.size() - 1);
        assertThat(testClips.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testClips.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testClips.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testClips.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testClips.getPositiveCount()).isEqualTo(DEFAULT_POSITIVE_COUNT);
        assertThat(testClips.getNegativeCount()).isEqualTo(DEFAULT_NEGATIVE_COUNT);
    }

    @Test
    @Transactional
    void fullUpdateClipsWithPatch() throws Exception {
        // Initialize the database
        clipsRepository.saveAndFlush(clips);

        int databaseSizeBeforeUpdate = clipsRepository.findAll().size();

        // Update the clips using partial update
        Clips partialUpdatedClips = new Clips();
        partialUpdatedClips.setId(clips.getId());

        partialUpdatedClips
            .userId(UPDATED_USER_ID)
            .name(UPDATED_NAME)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .positiveCount(UPDATED_POSITIVE_COUNT)
            .negativeCount(UPDATED_NEGATIVE_COUNT);

        restClipsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClips.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClips))
            )
            .andExpect(status().isOk());

        // Validate the Clips in the database
        List<Clips> clipsList = clipsRepository.findAll();
        assertThat(clipsList).hasSize(databaseSizeBeforeUpdate);
        Clips testClips = clipsList.get(clipsList.size() - 1);
        assertThat(testClips.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testClips.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClips.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testClips.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testClips.getPositiveCount()).isEqualTo(UPDATED_POSITIVE_COUNT);
        assertThat(testClips.getNegativeCount()).isEqualTo(UPDATED_NEGATIVE_COUNT);
    }

    @Test
    @Transactional
    void patchNonExistingClips() throws Exception {
        int databaseSizeBeforeUpdate = clipsRepository.findAll().size();
        clips.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClipsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clips.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clips))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clips in the database
        List<Clips> clipsList = clipsRepository.findAll();
        assertThat(clipsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClips() throws Exception {
        int databaseSizeBeforeUpdate = clipsRepository.findAll().size();
        clips.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClipsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clips))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clips in the database
        List<Clips> clipsList = clipsRepository.findAll();
        assertThat(clipsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClips() throws Exception {
        int databaseSizeBeforeUpdate = clipsRepository.findAll().size();
        clips.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClipsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(clips)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Clips in the database
        List<Clips> clipsList = clipsRepository.findAll();
        assertThat(clipsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClips() throws Exception {
        // Initialize the database
        clipsRepository.saveAndFlush(clips);

        int databaseSizeBeforeDelete = clipsRepository.findAll().size();

        // Delete the clips
        restClipsMockMvc
            .perform(delete(ENTITY_API_URL_ID, clips.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Clips> clipsList = clipsRepository.findAll();
        assertThat(clipsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
