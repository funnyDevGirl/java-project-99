package hexlet.code.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(of = {"name", "taskStatus"})
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

    @CreatedDate
    private LocalDate createdAt;

    @JoinColumn(name = "status_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private TaskStatus taskStatus;

    @JoinColumn(name = "assignee_id")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)//у меня
    private User assignee;


//    @ManyToMany
//    @JoinTable(name="label_task",
//            joinColumns = @JoinColumn(name="task_id", referencedColumnName="id"),
//            inverseJoinColumns = @JoinColumn(name="label_id", referencedColumnName="id") )
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<Label> labels = new HashSet<>();


//    public void addLabel(Label label) {
//        labels.add(label);
//        label.getTasks().add(this);
//    }
//
//    public void removeLabel(Label label) {
//        labels.remove(label);
//        label.getTasks().remove(this);
//    }
}
