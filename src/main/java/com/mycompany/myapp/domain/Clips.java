package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Clips.
 */
@Entity
@Table(name = "clips")
public class Clips implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "content", nullable = false)
    private byte[] content;

    @Column(name = "content_content_type", nullable = false)
    private String contentContentType;

    @Column(name = "positive_count")
    private Integer positiveCount;

    @Column(name = "negative_count")
    private Integer negativeCount;

    @OneToMany(mappedBy = "clips")
    @JsonIgnoreProperties(value = { "internalUser", "clips" }, allowSetters = true)
    private Set<ClipUser> clipUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Clips id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public Clips userId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return this.name;
    }

    public Clips name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getContent() {
        return this.content;
    }

    public Clips content(byte[] content) {
        this.content = content;
        return this;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentContentType() {
        return this.contentContentType;
    }

    public Clips contentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
        return this;
    }

    public void setContentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
    }

    public Integer getPositiveCount() {
        return this.positiveCount;
    }

    public Clips positiveCount(Integer positiveCount) {
        this.positiveCount = positiveCount;
        return this;
    }

    public void setPositiveCount(Integer positiveCount) {
        this.positiveCount = positiveCount;
    }

    public Integer getNegativeCount() {
        return this.negativeCount;
    }

    public Clips negativeCount(Integer negativeCount) {
        this.negativeCount = negativeCount;
        return this;
    }

    public void setNegativeCount(Integer negativeCount) {
        this.negativeCount = negativeCount;
    }

    public Set<ClipUser> getClipUsers() {
        return this.clipUsers;
    }

    public Clips clipUsers(Set<ClipUser> clipUsers) {
        this.setClipUsers(clipUsers);
        return this;
    }

    public Clips addClipUser(ClipUser clipUser) {
        this.clipUsers.add(clipUser);
        clipUser.setClips(this);
        return this;
    }

    public Clips removeClipUser(ClipUser clipUser) {
        this.clipUsers.remove(clipUser);
        clipUser.setClips(null);
        return this;
    }

    public void setClipUsers(Set<ClipUser> clipUsers) {
        if (this.clipUsers != null) {
            this.clipUsers.forEach(i -> i.setClips(null));
        }
        if (clipUsers != null) {
            clipUsers.forEach(i -> i.setClips(this));
        }
        this.clipUsers = clipUsers;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Clips)) {
            return false;
        }
        return id != null && id.equals(((Clips) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Clips{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", name='" + getName() + "'" +
            ", content='" + getContent() + "'" +
            ", contentContentType='" + getContentContentType() + "'" +
            ", positiveCount=" + getPositiveCount() +
            ", negativeCount=" + getNegativeCount() +
            "}";
    }
}
