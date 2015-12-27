package com.momega.spacesimulator.simulation.json;

import org.junit.Assert;

import org.junit.Test;

import com.google.gson.Gson;
import com.momega.spacesimulator.simulation.test.TestResult;

public class GsonTest {

	@Test
	public void basicTest() {
		Gson gson = new Gson();
		TestResult r = new TestResult();
		r.setFinished(true);
		r.setNumber(1);
		String s = gson.toJson(r);
		Assert.assertEquals("{\"number\":1,\"finished\":true}", s);
	}

}
