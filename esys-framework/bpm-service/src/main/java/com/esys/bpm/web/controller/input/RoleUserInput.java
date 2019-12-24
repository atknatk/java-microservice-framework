package com.esys.bpm.web.controller.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleUserInput {
	private Long roleId;
	private Long userId;
}
