/*
 * Copyright (C) 2007 ETH Zurich
 *
 * This file is part of Fosstrak (www.fosstrak.org).
 *
 * Fosstrak is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1, as published by the Free Software Foundation.
 *
 * Fosstrak is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Fosstrak; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA  02110-1301  USA
 */

package org.fosstrak.capturingapp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.definition.KnowledgePackage;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.StatelessKnowledgeSession;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReports;
import org.fosstrak.epcis.model.EPCISDocumentType;


/**
 * An ECReportHandler receives a ECReport and generates a EPCIS document that 
 * can be sent to an EPCIS repository. If you would like to implement your own 
 * handler, you will find some guide-lines below. In addition you can get inspiration 
 * by the default  reports handler {@link DefaultECReportHandler}.
 * <h3>Default Execution Sequence:</h3>
 * If you do not override the method <code>LinkedList&lt;EPCISDocumentType&gt;
 * handle(ECReports reports)</code>, the execution sequence of the handler is 
 * as following:
 * <ol>
 * <li><code>abstract loadRules()</code>: load drools rules from disc.</li>
 * <li><code>checkErrors()</code>: checks if errors in the rule-set. If there are 
 * errors, an exception is thrown.</li>
 * <li><code>registerKnowledgeBase()</code>: Create a new knowledge-base 
 * <code>kbase</code> from the <code>kbuilder</code>.</li>
 * <li><code>createSession()</code>: Creates a drools session. By default, a 
 * <strong>StatefulKnowledgeSession</strong> is created. Notice, that all 
 * subsequent generic methods are programmed in such a way, that they can 
 * handle both <strong>StatefulKnowledgeSession</strong> and 
 * <strong>StatelessKnowledgeSession</strong>.</li>
 * <li><code>prepareGlobalCollector(ksession)</code>: Registers a collector for 
 * the EPCIS documents. If you override the method <code>handle</code> make 
 * sure, to call this method <strong>before</strong> you execute the drools 
 * rules as otherwise third-party drools rules might crash.</li>
 * <li><code>executeSession(reports):</code>: Execute the drools rules. In 
 * <strong>StatefulKnowledgeSession</strong> the ECReports are injected via 
 * <code>insert</code>, in <strong>StatelessKnowledgeSession</strong> the 
 * rules are executed via <code>execute</code>.</li>
 * <li><code>collectResults()</code>: Retrieves the results from the global 
 * collector and submits them to the capture application for further delivery.</li>
 * </ol>
 * <h3>Methods to be implemented:</h3>
 * <ul>
 * <li><code>loadRules()</code>.  It is required, that 
 * this method loads the drools rules from the file-system into 
 * <code>kbuilder</code>.<br/>
 * Example:<br/>
 * 	<code>kbuilder.add(</code><br/>
 * <code>&nbsp;&nbsp;ResourceFactory.newClassPathResource(</code><br/>
 * <code>&nbsp;&nbsp;&nbsp;&nbsp;changeSet,</code><br/>
 * <code>&nbsp;&nbsp;&nbsp;&nbsp;DefaultECReportHandler.class),</code><br/>
 * <code>&nbsp;&nbsp;ResourceType.CHANGE_SET);</code>
 * </li>
 * @author sawielan
 *
 */
public abstract class ECReportsHandler {
	
	/** the name of the global variable for the drools results. */
	public static final String RESULTS = "epcisResults";
	
	/** array that will collect the results from the drools executions. */
	protected ArrayList<Object> epcis = null;
	
	/** the knowledge builder. */ 
	protected final KnowledgeBuilder kbuilder = 
		KnowledgeBuilderFactory.newKnowledgeBuilder();
	
	/** the knowledge base. */
	protected KnowledgeBase kbase = null;
	
	/** 
	 * the knowledge session. this is object to allow either state-full or 
	 * state-less sessions.
	 */
	protected Object ksession;
	
	// logger
	private static final Logger log = Logger.getLogger(ECReportsHandler.class);
	

	/** the default change-set to load with the drools rules. */
	public static final String DEFAULT_RULE_SET = "changeset.xml";
	
	// the change-set to load with the drools rules.
	protected final String changeSet;
	
	/**
	 * default constructor. sets the rule-set to 'changeset.xml'.
	 */
	public ECReportsHandler() {
		changeSet = DEFAULT_RULE_SET;
	}
	
	/**
	 * create a new handler with a non default change set.
	 * @param changeSet
	 */
	public ECReportsHandler(String changeSet) {
		this.changeSet = changeSet;
	}
	
	/**
	 * @return the change set that is used with this handler.
	 */
	public final String getChangeSet() {
		return changeSet;
	}
	
	/**
	 * this method is invoked from the capture application whenever a new 
	 * ECReports is received. The resulting EPCIS document will be delivered 
	 * automatically to the EPCIS repository. If you set the return value 
	 * to <code>null</code>, then no report is generated. <br/>
	 * <strong>NOTICE:</strong> If you override this method, make sure that you 
	 * invoke the method <code>prepareGlobalCollector(ksession)</code> 
	 * <strong>before</strong> you invoke the rule-set.
	 * @param reports the ECReports with the EPC data.
	 * @return an EPCIS document when this handler consumed the report, null 
	 * otherwise.
	 * @throws RuntimeException when the rule(s) could not be compiled.
	 */
	public LinkedList<EPCISDocumentType> handle(ECReports reports) throws RuntimeException {
		log.debug("handling report.");
		loadRules();
		checkErrors();
		registerKnowledgeBase();
		createSession();

		// THIS ONE IS IMPORTANT FOR THE DEFAULT COLLECT RESULTS!!!
		prepareGlobalCollector(ksession);
		
		executeSession(reports);
		
		return collectResults();
	}
	
	/**
	 * executes the drools session. The method is aware of the two different 
	 * knowledge-sessions <code>StatelessKnowledgeSession</code> and 
	 * <code>StatefulKnowledgeSession</code> and invokes them respectively.
	 * @param reports the ECReports.
	 */
	public void executeSession(ECReports reports) {
		log.debug("executing session.");
		if (ksession instanceof StatelessKnowledgeSession) {
			((StatelessKnowledgeSession)ksession).execute(reports);
		} else if (ksession instanceof StatefulKnowledgeSession) {
			StatefulKnowledgeSession sks = (StatefulKnowledgeSession) ksession;
			sks.insert(reports);
			sks.fireAllRules();
		}
	}
	
	/**
	 * create a new knowledge session. By default a 
	 * <code>StatefulKnowledgeSession</code> is generated.
	 */
	protected void createSession() {
		log.debug("create new stateless knowledge session.");
		ksession = kbase.newStatefulKnowledgeSession();
	}
	
	/**
	 * register the knowledge base from the rule builder in the knowledge base.
	 */
	protected void registerKnowledgeBase() {	
		if (null != kbase) {
			// we don't need to register the knowledge-base again. 
			return;
		}
		log.debug("register knowledge base.");
		// get the compiled packages
		final Collection<KnowledgePackage> pkgs = kbuilder
			.getKnowledgePackages();

		// add the packages to a knowledge-base (deploy the knowledge packages).
		kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(pkgs);
	}

	/**
	 * check the knowledge-builder for errors in the rules.
	 * @throws RuntimeException when there are errors in the rules.
	 */
	protected void checkErrors() throws RuntimeException {
		// Check the builder for errors
		if (kbuilder.hasErrors()) {
			log.debug(kbuilder.getErrors().toString());
			throw new RuntimeException(kbuilder.getErrors().toString());
		}
	}

	/**
	 * prepare a global collector where the rules can store the results to.
	 * @param ksession the session holding the collector.
	 */
	protected void prepareGlobalCollector(Object ksession) {
		log.debug("preparing global collector.");
		epcis = new ArrayList<Object> ();
		if (ksession instanceof StatefulKnowledgeSession) {
			((StatefulKnowledgeSession) ksession).setGlobal(RESULTS, epcis);
		} else if (ksession instanceof StatelessKnowledgeSession) {
			((StatelessKnowledgeSession)ksession).setGlobal(RESULTS, epcis);
		}
	}
	
	/**
	 * collect the results from the rules evaluation and put them into a nice 
	 * typed linked list.
	 * @return a linked list holding the result set.
	 */
	protected LinkedList<EPCISDocumentType> collectResults() {
		log.debug("collecting results.");
		LinkedList<EPCISDocumentType> results 
			= new LinkedList<EPCISDocumentType> ();
		for (Object o : epcis) {
			if ((null != o) && (o instanceof EPCISDocumentType)) {
				results.add((EPCISDocumentType)o);
			} else if (null != o) {
				log.debug(o);
			}
		}
		return results;
	}
	
	/**
	 * <code>// parse and compile the rule from file</code><br/>
	 * <code> kbuilder.add(ResourceFactory.newClassPathResource(</code><br/>
	 * <code>      "HelloWorld.drl", HelloWorld.class), ResourceType.DRL);</code><br/>
	 */
	public abstract void loadRules();
	
	/**
	 * in case of a <code>StatefullKnowledgeSession</code> we have to call 
	 * dispose at the end of execution.
	 */
	public void dispose() {
		if (ksession instanceof StatefulKnowledgeSession) {
			((StatefulKnowledgeSession) ksession).dispose();
		}
	}
}
