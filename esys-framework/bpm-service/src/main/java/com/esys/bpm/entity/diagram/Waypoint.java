package com.esys.bpm.entity.diagram;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class Waypoint {

	@Id
	@SequenceGenerator(name = "waypoint_id_generator", sequenceName = "waypoint_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "waypoint_id_generator")
	private Long waypointId;

	private String x;
	private String y;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, optional = false)
	@JoinColumn(name = "edgeId", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Edge edge;
}
