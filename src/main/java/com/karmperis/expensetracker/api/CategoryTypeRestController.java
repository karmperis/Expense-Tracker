package com.karmperis.expensetracker.api;

import com.karmperis.expensetracker.core.exceptions.EntityAlreadyExistsException;
import com.karmperis.expensetracker.core.exceptions.EntityInvalidArgumentException;
import com.karmperis.expensetracker.core.exceptions.EntityNotFoundException;
import com.karmperis.expensetracker.dto.CategoryTypeEditDTO;
import com.karmperis.expensetracker.dto.CategoryTypeInsertDTO;
import com.karmperis.expensetracker.dto.CategoryTypeReadOnlyDTO;
import com.karmperis.expensetracker.model.User;
import com.karmperis.expensetracker.service.ICategoryTypeService;
import com.karmperis.expensetracker.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Rest controller for managing category types.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category-type")
public class CategoryTypeRestController {

    private final ICategoryTypeService categoryTypeService;
    private final IUserService userService;

    /**
     * Retrieves a paginated list of category types.
     * @param page The page number to retrieve (defaults to 0).
     * @param size The number of items per page (defaults to 10).
     * @return A ResponseEntity containing a page of CategoryTypeReadOnlyDTO.
     */
    @GetMapping
    public ResponseEntity<Page<CategoryTypeReadOnlyDTO>> getAllCategoryTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        User user = userService.getRawUserByUsername("admin");

        Page<CategoryTypeReadOnlyDTO> categoryTypes = categoryTypeService.getAllCategoryTypes(pageable, user);
        return ResponseEntity.ok(categoryTypes);
    }

    /**
     * Creates a new category type with Bean Validation.
     */
    @PostMapping
    public ResponseEntity<CategoryTypeReadOnlyDTO> createCategoryType(
            @Valid @RequestBody CategoryTypeInsertDTO dto)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException {

        User user = userService.getRawUserByUsername("admin");
        CategoryTypeReadOnlyDTO created = categoryTypeService.saveCategoryType(dto, user);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Updates an existing category type.
     */
    @PutMapping("/{uuid}")
    public ResponseEntity<CategoryTypeReadOnlyDTO> updateCategoryType(
            @PathVariable UUID uuid,
            @Valid @RequestBody CategoryTypeEditDTO dto)
            throws EntityNotFoundException, EntityAlreadyExistsException, EntityInvalidArgumentException {

        User user = userService.getRawUserByUsername("admin");
        CategoryTypeReadOnlyDTO updated = categoryTypeService.updateCategoryType(uuid, dto, user);
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletes a category type.
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteCategoryType(@PathVariable UUID uuid) throws EntityNotFoundException {
        User user = userService.getRawUserByUsername("admin");
        categoryTypeService.deleteCategoryType(uuid, user);
        return ResponseEntity.noContent().build();
    }
}