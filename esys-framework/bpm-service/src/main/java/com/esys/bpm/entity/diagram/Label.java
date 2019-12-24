package com.esys.bpm.entity.diagram;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
/*@Table(schema = "bpm")*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Label {

	@Id
	@SequenceGenerator(name = "label_id_generator", sequenceName = "label_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "label_id_generator")
	private Long labelId;

	@OneToOne(cascade = CascadeType.ALL)
	private Bounds bounds;
}
