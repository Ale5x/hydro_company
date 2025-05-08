package org.study.hydro.hateoas;

import org.junit.jupiter.api.Test;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.study.hydro.controller.ControllerConstants;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.hateoas.Link.of;

public class HypermediaListAssemblerTest {

    private final HypermediaListAssembler<String> assembler = new HypermediaListAssembler<>() {};

    @Test
    void createPaginatedModel_WithNextData_ShouldIncludeNextLink() {
        List<String> dataList = List.of("item1", "item2");
        List<String> nextDataList = List.of("nextItem");
        Link previousLink = of("http://localhost/prev", ControllerConstants.PREVIOUS);
        Link nextLink = of("http://localhost/next", ControllerConstants.NEXT);

        CollectionModel<String> model = assembler.createPaginatedModel(dataList, nextDataList, previousLink, nextLink);

        assertThat(model.getContent()).containsExactly("item1", "item2");
        assertThat(model.getLinks()).hasSize(2);
        assertThat(model.getLinks(ControllerConstants.PREVIOUS)).isNotEmpty();
        assertThat(model.getLinks(ControllerConstants.NEXT)).isNotEmpty();
    }

    @Test
    void createPaginatedModel_WithoutNextData_ShouldExcludeNextLink() {
        List<String> dataList = List.of("item1", "item2");
        List<String> nextDataList = List.of();
        Link previousLink = of("http://localhost/prev", ControllerConstants.PREVIOUS);
        Link nextLink = of("http://localhost/next", ControllerConstants.NEXT);

        CollectionModel<String> model = assembler.createPaginatedModel(dataList, nextDataList, previousLink, nextLink);

        assertThat(model.getContent()).containsExactly("item1", "item2");
        assertThat(model.getLinks()).hasSize(1);
        assertThat(model.getLinks(ControllerConstants.PREVIOUS)).isNotEmpty();
        assertThat(model.getLinks(ControllerConstants.NEXT)).isEmpty();
    }
}
