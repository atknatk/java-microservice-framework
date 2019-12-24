package com.esys.bpm.entity.diagram;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import com.esys.bpm.entity.Process;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
/* @Table(schema = "bpm") */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Plane {

	@Id
	@SequenceGenerator(name = "plane_id_generator", sequenceName = "plane_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "plane_id_generator")
	private Long planeId;

	@OneToOne(cascade = CascadeType.ALL)
	private Process process;

	@OneToMany(mappedBy = "plane", cascade = CascadeType.ALL)
	private List<Shape> shapes;

	@OneToMany(mappedBy = "plane", cascade = CascadeType.ALL)
	private List<Edge> edges;
}
