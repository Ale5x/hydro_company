package org.study.hydro.hateoas;

import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpMethod;
import org.study.hydro.controller.ControllerConstants;

import static org.assertj.core.api.Assertions.assertThat;

class HateoasLinkHelperTest {
    private static final String BASE_PATH = "api/test";
    private static final String PAGE = "2";
    private static final String SIZE = "10";

    static class DummyController {}

    @Test
    void createPreviousLink_ShouldReturnCorrectLink() {
        Link link = HateoasLinkHelper.createPreviousLink(DummyController.class, BASE_PATH, PAGE, SIZE);

        String expectedHref = "/api/test?size=10&page=1";
        assertThat(link.getRel().value()).isEqualTo(ControllerConstants.PREVIOUS);
        assertThat(link.getType()).isEqualTo(HttpMethod.GET.name());
        assertThat(link.getHref()).endsWith(expectedHref);
    }

    @Test
    void createNextLink_ShouldReturnCorrectLink() {
        Link link = HateoasLinkHelper.createNextLink(DummyController.class, BASE_PATH, PAGE, SIZE);

        String expectedHref = "/api/test?size=10&page=3";
        assertThat(link.getRel().value()).isEqualTo(ControllerConstants.NEXT);
        assertThat(link.getType()).isEqualTo(HttpMethod.GET.name());
        assertThat(link.getHref()).endsWith(expectedHref);
    }

    @Test
    void createPreviousLink_WhenPageIsOne_ShouldReturnPageOne() {
        String currentPage = "1";
        Link link = HateoasLinkHelper.createPreviousLink(DummyController.class, BASE_PATH, currentPage, SIZE);

        assertThat(link.getRel().value()).isEqualTo(ControllerConstants.PREVIOUS);
        assertThat(link.getType()).isEqualTo(HttpMethod.GET.name());
        assertThat(link.getHref()).endsWith("/api/test?size=10&page=1");
    }

    @Test
    void createNextLink_WhenPageIsZero_ShouldReturnPageTwo() {
        String currentPage = "0";
        Link link = HateoasLinkHelper.createNextLink(DummyController.class, BASE_PATH, currentPage, SIZE);

        assertThat(link.getRel().value()).isEqualTo(ControllerConstants.NEXT);
        assertThat(link.getType()).isEqualTo(HttpMethod.GET.name());
        assertThat(link.getHref()).endsWith("/api/test?size=10&page=2");
    }
}