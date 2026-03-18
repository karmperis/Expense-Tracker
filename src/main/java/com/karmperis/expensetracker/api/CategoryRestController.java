package com.karmperis.expensetracker.api;

import com.karmperis.expensetracker.core.exceptions.EntityAlreadyExistsException;
import com.karmperis.expensetracker.core.exceptions.EntityNotFoundException;
import com.karmperis.expensetracker.dto.CategoryEditDTO;
import com.karmperis.expensetracker.dto.CategoryInsertDTO;
import com.karmperis.expensetracker.dto.CategoryReadOnlyDTO;
import com.karmperis.expensetracker.model.User;
import com.karmperis.expensetracker.service.ICategoryService;
import com.karmperis.expensetracker.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Rest Controller for managing expense and income categories.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryRestController {

    private final ICategoryService categoryService;
    private final IUserService userService;

    /**
     * Creates a new category.
     * @param dto The category data to be validated and saved.
     * @return The created category details.
     * @throws EntityAlreadyExistsException If the category name already exists for this user.
     * @throws EntityNotFoundException If the associated CategoryType or User is not found.
     */
    @PostMapping
    public ResponseEntity<CategoryReadOnlyDTO> createCategory(@Valid @RequestBody CategoryInsertDTO dto)
            throws EntityAlreadyExistsException, EntityNotFoundException {

        User user = userService.getRawUserByUsername("admin");

        CategoryReadOnlyDTO createdCategory = categoryService.saveCategory(dto, user);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    /**
     * Retrieves all active categories for the user with pagination.
     */
    @GetMapping
    public ResponseEntity<Page<CategoryReadOnlyDTO>> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        User user = userService.getRawUserByUsername("admin");
        Pageable pageable = PageRequest.of(page,size);

        return ResponseEntity.ok(categoryService.getAllCategories(pageable, user));
    }

    /**
     * Updates an existing category.
     */
    @PutMapping("/{uuid}")
    public ResponseEntity<CategoryReadOnlyDTO> updateCategory(
            @PathVariable UUID uuid,
            @Valid @RequestBody CategoryEditDTO dto)
            throws EntityNotFoundException, EntityAlreadyExistsException {

        User user = userService.getRawUserByUsername("admin");
        CategoryReadOnlyDTO updated = categoryService.updateCategory(uuid, dto, user);
        return ResponseEntity.ok(updated);
    }

    /**
     * Performs a soft delete by deactivating the category.
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID uuid) throws EntityNotFoundException {
        User user = userService.getRawUserByUsername("admin");
        categoryService.deleteCategory(uuid, user);
        return ResponseEntity.noContent().build();
    }
}