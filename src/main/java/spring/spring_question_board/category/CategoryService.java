package spring.spring_question_board.category;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.spring_question_board.DataNotFoundException;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public void create(String name) {
        Category category = new Category();
        category.setName(name);
        this.categoryRepository.save(category);
    }

    public List<Category> getList() {
        return this.categoryRepository.findAll();
    }

    public Category getCategory(String name) {
        Optional<Category> category = this.categoryRepository.findByName(name);
        if (category.isPresent()) {
            return category.get();
        } else {
            throw new DataNotFoundException("Category not found");
        }
    }
}
