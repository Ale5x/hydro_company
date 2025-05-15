package org.study.hydrowarehouse.utill;

import org.junit.jupiter.api.Test;
import org.study.hydrowarehouse.exception.AppRequestException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class PaginationTest {

    @Test
    void getPreviousPage_WhenPageIsGreaterThanOne_ReturnsPreviousPage() {
        assertThat(Pagination.getPreviousPage("3")).isEqualTo("2");
    }

    @Test
    void getPreviousPage_WhenPageIsOne_ReturnsOne() {
        assertThat(Pagination.getPreviousPage("1")).isEqualTo("1");
    }

    @Test
    void getPreviousPage_WhenPageIsNotNumber_ThrowsException() {
        assertThatThrownBy(() -> Pagination.getPreviousPage("abc"))
                .isInstanceOf(AppRequestException.class);
    }

    @Test
    void getNumberNextPage_WhenPageIsPositive_ReturnsNextPage() {
        assertThat(Pagination.getNumberNextPage("5")).isEqualTo("6");
    }

    @Test
    void getNumberNextPage_WhenPageIsZero_ReturnsTwo() {
        assertThat(Pagination.getNumberNextPage("0")).isEqualTo("2");
    }

    @Test
    void getNumberNextPage_WhenPageIsNegative_ThrowsException() {
        assertThatThrownBy(() -> Pagination.getNumberNextPage("-1"))
                .isInstanceOf(AppRequestException.class);
    }

    @Test
    void getNumberNextPage_WhenPageIsNotNumber_ThrowsException() {
        assertThatThrownBy(() -> Pagination.getNumberNextPage("abc"))
                .isInstanceOf(AppRequestException.class);
    }

    @Test
    void getOffset_ValidInput_ReturnsCorrectOffset() {
        assertThat(Pagination.getOffset("2", "10")).isEqualTo(10);
        assertThat(Pagination.getOffset("1", "5")).isEqualTo(0);
    }

    @Test
    void getOffset_WhenPageOrSizeInvalid_ThrowsException() {
        assertThatThrownBy(() -> Pagination.getOffset("abc", "10"))
                .isInstanceOf(AppRequestException.class);

        assertThatThrownBy(() -> Pagination.getOffset("2", "abc"))
                .isInstanceOf(AppRequestException.class);
    }
}