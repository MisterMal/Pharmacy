package pl.lodz.p.it.ssbd2023.ssbd01.moa.managers;

import jakarta.ejb.Local;
import pl.lodz.p.it.ssbd2023.ssbd01.common.CommonManagerLocalInterface;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Category;

import java.util.List;

@Local
public interface CategoryManagerLocal extends CommonManagerLocalInterface {


    List<Category> getAllCategories();

    Category createCategory(Category category);

    Category getCategory(Long id);

    Category editCategory(Long id, Category category, Long version);

    Category findByName(String name);
}
