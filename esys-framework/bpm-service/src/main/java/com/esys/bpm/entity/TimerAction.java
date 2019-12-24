package com.esys.bpm.entity;

import javax.persistence.Column;
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
public class TimerAction {
	@Id
	@SequenceGenerator(name = "timer_action_id_generator", sequenceName = "timer_action_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timer_action_id_generator")
	private Long timerActionId;
	@Column(nullable = false)
	private int timerTaskId;

}
