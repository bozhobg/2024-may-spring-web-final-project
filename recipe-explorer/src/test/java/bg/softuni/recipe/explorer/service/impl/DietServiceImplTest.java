package bg.softuni.recipe.explorer.service.impl;

import bg.softuni.recipe.explorer.repository.DietRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DietServiceImplTest {

    private DietServiceImpl toTest;
    @Mock
    private DietRepository mockDietRepository;

    @BeforeEach
    void setUp() {
        toTest = new DietServiceImpl(mockDietRepository);
    }

    @Test
    void testGetBasicDTOs_ReturnsMapped() {

    }

    void testGetAllByIds() {}

    void testAreIdsValid() {}
}
