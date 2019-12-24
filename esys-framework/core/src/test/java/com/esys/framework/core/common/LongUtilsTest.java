package com.esys.framework.core.common;

import com.esys.framework.core.model.ModelResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
@RunWith(SpringRunner.class)
public class LongUtilsTest {

    Long val;
    Long val2;
    @Before
    public void setUp() {
        val = 1L;
        val2 = null;
    }

    @Test
    public void isNullOrZero() {
        assertThat(LongUtils.isNullOrZero(val)).isFalse();
        assertThat(LongUtils.isNullOrZero(val2)).isTrue();
    }

    @Test
    public void isNullOrZeroWithMessage() {
        ModelResult.ModelResultBuilder modelResultBuilder = new ModelResult.ModelResultBuilder();
        ModelResult nullOrZeroWithMessage = LongUtils.isNullOrZeroWithMessage(val, modelResultBuilder);
        assertThat(nullOrZeroWithMessage.isSuccess()).isTrue();

        nullOrZeroWithMessage = LongUtils.isNullOrZeroWithMessage(val2, modelResultBuilder);
        assertThat(nullOrZeroWithMessage.isSuccess()).isFalse();
    }

    @Test
    public void isNullOrZeroWithMessage1() {
        ModelResult.ModelResultBuilders modelResultBuilders = new ModelResult.ModelResultBuilders();
        ModelResult nullOrZeroWithMessage = LongUtils.isNullOrZeroWithMessage(val, modelResultBuilders);
        assertThat(nullOrZeroWithMessage.isSuccess()).isTrue();

        nullOrZeroWithMessage = LongUtils.isNullOrZeroWithMessage(val2, modelResultBuilders);
        assertThat(nullOrZeroWithMessage.isSuccess()).isFalse();
    }
}
