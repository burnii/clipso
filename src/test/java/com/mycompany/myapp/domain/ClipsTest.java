package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClipsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Clips.class);
        Clips clips1 = new Clips();
        clips1.setId(1L);
        Clips clips2 = new Clips();
        clips2.setId(clips1.getId());
        assertThat(clips1).isEqualTo(clips2);
        clips2.setId(2L);
        assertThat(clips1).isNotEqualTo(clips2);
        clips1.setId(null);
        assertThat(clips1).isNotEqualTo(clips2);
    }
}
