package com.esys.bpm.entity.diagram;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class Bounds {

	@Id
	@SequenceGenerator(name = "bounds_id_generator", sequenceName = "bounds_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bounds_id_generator")
	private Long boundsId;

	private String x;
	private String y;
	private String width;
	private String height;
}
