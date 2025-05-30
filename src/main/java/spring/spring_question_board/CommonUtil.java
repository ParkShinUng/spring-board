package spring.spring_question_board;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommonUtil {
    public static String markdown(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    public static Pageable getPageable(int page, int pageSize, String sortProperty, String sortOrder) {
        List<Sort.Order> sortList = new ArrayList<>();
        if (sortOrder.equals("desc")) {
            sortList.add(Sort.Order.desc(sortProperty));
        } else {
            sortList.add(Sort.Order.asc(sortProperty));
        }

        return PageRequest.of(page, pageSize, Sort.by(sortList));
    }
}
