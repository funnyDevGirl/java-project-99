package hexlet.code.repository;

import hexlet.code.model.Task;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;


@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    // TODO: написать JPQL
    // сделать кеш (ehcache), чтобы запрос цеплял из кеша, а не из базы
    // проверить, что все работает!

    @Cacheable(cacheNames = "tasks", key = "#taskIds")
    @Query("SELECT t FROM Task t WHERE t.id IN :taskIds")
    Set<Task> findByIdIn(@Param("taskIds") Set<Long> taskIds);

    @Cacheable(cacheNames = "tasks", key = "#name")
    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.labels LEFT JOIN FETCH t.assignee LEFT JOIN FETCH t.taskStatus WHERE t.name = :name")
    Optional<Task> findByNameWithLazyFields(@Param("name") String name);


    //@EntityGraph(attributePaths = {"labels", "assignee", "taskStatus"})
    Optional<Task> findByName(String name);

    //Set<Task> findByIdIn(Set<Long> taskIds);

    //@Cacheable(cacheNames = "tasks", key = "#name")
    @Query("SELECT t FROM Task AS t LEFT JOIN FETCH t.labels WHERE t.name=:name")
    Optional<Task> findByNameWithLabels(@Param("name") String name);

    //@Cacheable(cacheNames = "tasks", key = "#id")
    @Query("SELECT t FROM Task AS t LEFT JOIN FETCH t.labels WHERE t.id=:id")
    Optional<Task> findByIdWithLabels(@Param("id") Long id);
}
