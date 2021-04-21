package org.wallride.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.SortableField;

import javax.persistence.*;

@Entity
@Table(name = "role", schema = "public")
@Getter
@Setter
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Field(name = "sortId", analyze = Analyze.NO, index = org.hibernate.search.annotations.Index.NO)
	@SortableField(forField = "sortId")
	private Long id;

	@Column(length = 100, unique = true)
	@Field(analyze = Analyze.NO)
	private String name;
}
