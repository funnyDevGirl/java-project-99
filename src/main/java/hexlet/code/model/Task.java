package hexlet.code.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Id;
import jakarta.persistence.CascadeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "tasks")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Task implements BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @NotBlank
    @Size(min = 1)
    private String name;

    private Integer index;

    @NotBlank
    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private TaskStatus taskStatus;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private User assignee;

    @CreatedDate
    private LocalDate createdAt;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<Label> labels = new HashSet<>();
}
