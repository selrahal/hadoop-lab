package com.rhc.hadoop.drools;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsHelper {
	private static KieContainer kieContainer = KieServices.Factory.get().newKieClasspathContainer();

	public static KieSession createKieSession() {
		return kieContainer.newKieSession();
	}
	
}
