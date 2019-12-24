package com.esys.bpm.entity.diagram;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
/* @Table(schema = "bpm") */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shape {

	@Id
	@SequenceGenerator(name = "shape_id_generator", sequenceName = "shape_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shape_id_generator")
	private Long shapeId;

	@Column(nullable = false)
	private String xmlId;

	@Column(nullable = false)
	private Long elementId;
	@Column(nullable = false)
	private String elementXmlId;

	@OneToOne(cascade = CascadeType.ALL)
	private Bounds bounds;

	@OneToOne(cascade = CascadeType.ALL)
	private Label label;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, optional = false)
	@JoinColumn(name = "planeId", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Plane plane;
}
