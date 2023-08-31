package pl.apserwis.ap.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "work")
public class Work implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Size(min = 1)
    private String description;

//    @NotNull
//    @Size(min = 1)
//    private String price;

    @NotNull
    private String date;

    @ManyToOne
    @NotNull
    private Cars cars;

    @ElementCollection
    private List<String> files = new ArrayList<>();

    @Column(name = "accepted", columnDefinition = "boolean default false", nullable = false)
    private Boolean accepted = false;
}
