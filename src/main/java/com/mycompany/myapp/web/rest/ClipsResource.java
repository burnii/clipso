package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Clips;
import com.mycompany.myapp.repository.ClipsRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Clips}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ClipsResource {

    private final Logger log = LoggerFactory.getLogger(ClipsResource.class);

    private static final String ENTITY_NAME = "clips";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClipsRepository clipsRepository;

    public ClipsResource(ClipsRepository clipsRepository) {
        this.clipsRepository = clipsRepository;
    }

    /**
     * {@code POST  /clips} : Create a new clips.
     *
     * @param clips the clips to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clips, or with status {@code 400 (Bad Request)} if the clips has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/clips")
    public ResponseEntity<Clips> createClips(@Valid @RequestBody Clips clips) throws URISyntaxException {
        log.debug("REST request to save Clips : {}", clips);
        if (clips.getId() != null) {
            throw new BadRequestAlertException("A new clips cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Clips result = clipsRepository.save(clips);
        return ResponseEntity
            .created(new URI("/api/clips/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /clips/:id} : Updates an existing clips.
     *
     * @param id the id of the clips to save.
     * @param clips the clips to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clips,
     * or with status {@code 400 (Bad Request)} if the clips is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clips couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/clips/{id}")
    public ResponseEntity<Clips> updateClips(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Clips clips)
        throws URISyntaxException {
        log.debug("REST request to update Clips : {}, {}", id, clips);
        if (clips.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clips.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clipsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Clips result = clipsRepository.save(clips);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, clips.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /clips/:id} : Partial updates given fields of an existing clips, field will ignore if it is null
     *
     * @param id the id of the clips to save.
     * @param clips the clips to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clips,
     * or with status {@code 400 (Bad Request)} if the clips is not valid,
     * or with status {@code 404 (Not Found)} if the clips is not found,
     * or with status {@code 500 (Internal Server Error)} if the clips couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/clips/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Clips> partialUpdateClips(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Clips clips
    ) throws URISyntaxException {
        log.debug("REST request to partial update Clips partially : {}, {}", id, clips);
        if (clips.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clips.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clipsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Clips> result = clipsRepository
            .findById(clips.getId())
            .map(
                existingClips -> {
                    if (clips.getUserId() != null) {
                        existingClips.setUserId(clips.getUserId());
                    }
                    if (clips.getName() != null) {
                        existingClips.setName(clips.getName());
                    }
                    if (clips.getContent() != null) {
                        existingClips.setContent(clips.getContent());
                    }
                    if (clips.getContentContentType() != null) {
                        existingClips.setContentContentType(clips.getContentContentType());
                    }
                    if (clips.getPositiveCount() != null) {
                        existingClips.setPositiveCount(clips.getPositiveCount());
                    }
                    if (clips.getNegativeCount() != null) {
                        existingClips.setNegativeCount(clips.getNegativeCount());
                    }

                    return existingClips;
                }
            )
            .map(clipsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, clips.getId().toString())
        );
    }

    /**
     * {@code GET  /clips} : get all the clips.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clips in body.
     */
    @GetMapping("/clips")
    public List<Clips> getAllClips() {
        log.debug("REST request to get all Clips");
        return clipsRepository.findAll();
    }

    /**
     * {@code GET  /clips/:id} : get the "id" clips.
     *
     * @param id the id of the clips to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clips, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/clips/{id}")
    public ResponseEntity<Clips> getClips(@PathVariable Long id) {
        log.debug("REST request to get Clips : {}", id);
        Optional<Clips> clips = clipsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(clips);
    }

    /**
     * {@code DELETE  /clips/:id} : delete the "id" clips.
     *
     * @param id the id of the clips to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/clips/{id}")
    public ResponseEntity<Void> deleteClips(@PathVariable Long id) {
        log.debug("REST request to delete Clips : {}", id);
        clipsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
