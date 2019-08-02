package core.article;

import java.io.Serializable;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Tags")
@NamedQuery(name = "Tag.getAllTags", query = "SELECT t FROM Tag t")
@RequestScoped
public class Tag implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private String id;

	@Column(name = "name")
    private String name;
	
    public Tag() {
    }

    public Tag(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }
}
