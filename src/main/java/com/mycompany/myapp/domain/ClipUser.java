package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A ClipUser.
 */
@Entity
@Table(name = "clip_user")
public class ClipUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private User internalUser;

    @ManyToOne
    @JsonIgnoreProperties(value = { "clipUsers" }, allowSetters = true)
    private Clips clips;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClipUser id(Long id) {
        this.id = id;
        return this;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public ClipUser internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public Clips getClips() {
        return this.clips;
    }

    public ClipUser clips(Clips clips) {
        this.setClips(clips);
        return this;
    }

    public void setClips(Clips clips) {
        this.clips = clips;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClipUser)) {
            return false;
        }
        return id != null && id.equals(((ClipUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClipUser{" +
            "id=" + getId() +
            "}";
    }
}
