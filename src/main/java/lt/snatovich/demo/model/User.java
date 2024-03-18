package lt.snatovich.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`user`")
public class User {

    public User(String uuid) {
        this.id = UUID.fromString(uuid);
    }

    public User(UUID uuid) {
        this.id = uuid;
    }

    @Id
    private UUID id;


    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<Tweet> tweets;
}
