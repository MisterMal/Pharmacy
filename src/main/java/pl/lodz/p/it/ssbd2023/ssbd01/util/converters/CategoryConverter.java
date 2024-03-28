package pl.lodz.p.it.ssbd2023.ssbd01.util.converters;

import pl.lodz.p.it.ssbd2023.ssbd01.dto.category.CategoryDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.category.EditCategoryDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.category.GetCategoryDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Category;

public class CategoryConverter {

    private CategoryConverter() {}

    public static CategoryDTO mapCategoryToCategoryDTO(Category category) {
        return CategoryDTO.builder()
                .isOnPrescription(category.getIsOnPrescription())
                .name(category.getName())
                .build();
    }

    public static Category mapCategoryDTOToCategory(CategoryDTO categoryDTO) {
        return Category.builder()
                .isOnPrescription(categoryDTO.getIsOnPrescription())
                .name(categoryDTO.getName())
                .build();
    }

    public static Category mapEditCategoryDTOToCategory(EditCategoryDTO editCategoryDTO) {
        return Category.builder()
                .isOnPrescription(editCategoryDTO.getIsOnPrescription())
                .name(editCategoryDTO.getName())
                .build();
    }

    public static GetCategoryDTO mapCategoryToGetCategoryDTO(Category category) {
        return GetCategoryDTO.builder()
                .id(category.getId())
                .version(category.getVersion())
                .isOnPrescription(category.getIsOnPrescription())
                .name(category.getName())
                .build();
    }
}
