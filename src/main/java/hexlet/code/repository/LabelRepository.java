package hexlet.code.repository;

import hexlet.code.model.Label;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {

    //@Cacheable(cacheNames = "labels", key = "#name")
    @Query("SELECT l FROM Label l LEFT JOIN FETCH l.tasks WHERE l.name = :name")
    Optional<Label> findByNameWithTasks(@Param("name") String name);

    //@Cacheable(cacheNames = "labels", key = "#labelsIds")
    @Query("SELECT l FROM Label l WHERE l.id IN :labelsIds")
    Set<Label> findByIdIn(@Param("labelsIds") Set<Long> labelsIds);

    //@Cacheable(cacheNames = "labels", key = "#id")
    @Query("SELECT l FROM Label l JOIN l.tasks WHERE l.id = :id")
    Optional<Label> findByIdWithTasks(@Param("id") Long id);

// select l1_0.id,l1_0.created_at,l1_0.name from labels l1_0 where l1_0.id=?

// select l1_0.id,l1_0.created_at,l1_0.name,t1_0.labels_id,t1_1.id,t1_1.assignee_id,t1_1.created_at,t1_1.description,t1_1.index,t1_1.name,t1_1.task_status_id from labels l1_0 join tasks_labels t1_0 on l1_0.id=t1_0.labels_id join tasks t1_1 on t1_1.id=t1_0.tasks_id where l1_0.id=?

    //@EntityGraph(attributePaths = {"tasks"})
    Optional<Label> findByName(String name);

    //Set<Label> findByIdIn(Set<Long> labelsIds);
}
