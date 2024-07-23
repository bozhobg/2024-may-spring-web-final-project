package bg.softuni.recipe.explorer.repository;

import bg.softuni.recipe.explorer.model.entity.Diet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DietRepository extends JpaRepository<Diet, Long> {

}
