@GenericGenerator(
		name = "optimized-sequence",
		strategy = "enhanced-sequence",
		parameters = {
				@Parameter(name="prefer_sequence_per_entity", value="true"),
				@Parameter(name="optimizer", value="hilo")})

package com.example.ol.model.hibernate.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
