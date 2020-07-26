package com.artarkatesoft.bootstrap;

import com.artarkatesoft.domain.Category;
import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.domain.UnitOfMeasure;
import com.artarkatesoft.repositories.CategoryRepository;
import com.artarkatesoft.repositories.RecipeRepository;
import com.artarkatesoft.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Mock
    RecipeRepository recipeRepository;
    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    ContextRefreshedEvent event;

    @InjectMocks
    DataInitializer initializer;

    @Test
    @DisplayName("when there is no categories then should bootstrap categories data")
    void bootstrapCategoriesData() {
        //given
        given(recipeRepository.count()).willReturn(1L);
        //when
        initializer.onApplicationEvent(event);
        //then
        then(categoryRepository).should().count();
        then(categoryRepository)
                .should(times(4))
                .save(any(Category.class));
    }

    @Test
    @DisplayName("when there is no Unit Of Measure then should bootstrap UOM data")
    void bootstrapUnitOfMeasureData() {
        //given
        given(recipeRepository.count()).willReturn(1L);
        //when
        initializer.onApplicationEvent(event);
        //then
        then(unitOfMeasureRepository).should().count();
        then(unitOfMeasureRepository)
                .should(atLeast(6))
                .save(any(UnitOfMeasure.class));
    }

    @Test
    @DisplayName("when recipes present then should not bootstrap Recipes data")
    void whenRecipesPresent_shouldNotSave() {
        //given
        given(recipeRepository.count()).willReturn(1L);
        //when
        initializer.onApplicationEvent(event);
        //then
        then(recipeRepository).should().count();
        then(recipeRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("when no recipes present and UOM absent then should throw exception")
    void whenNoRecipes_shouldThrowException() {
        //when
        Executable initializerExecutable = () -> initializer.onApplicationEvent(event);
        //then
        assertThrows(RuntimeException.class, initializerExecutable);
        then(recipeRepository).should().count();
        then(recipeRepository).shouldHaveNoMoreInteractions();
        then(unitOfMeasureRepository).should().findByDescription(anyString());
    }

    @Test
    @DisplayName("when no recipes present then should bootstrap data")
    void whenNoRecipes_shouldBootstrap() {
        //given
        given(unitOfMeasureRepository.findByDescription(anyString())).willReturn(Optional.of(new UnitOfMeasure()));
        given(categoryRepository.findByDescription(anyString())).willReturn(Optional.of(new Category()));
        //when
        initializer.onApplicationEvent(event);
        //then
        then(recipeRepository).should().count();
        then(unitOfMeasureRepository).should(atLeastOnce()).findByDescription(anyString());
        then(categoryRepository).should(atLeastOnce()).findByDescription(anyString());
        then(recipeRepository).should(times(2)).save(any(Recipe.class));
    }
}
