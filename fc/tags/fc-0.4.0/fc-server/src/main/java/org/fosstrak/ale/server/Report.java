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

package org.fosstrak.ale.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.fosstrak.ale.util.ECReportSetEnum;
import org.fosstrak.ale.util.HexUtil;
import org.fosstrak.ale.wsdl.ale.epcglobal.ECSpecValidationException;
import org.fosstrak.ale.wsdl.ale.epcglobal.ECSpecValidationExceptionResponse;
import org.fosstrak.ale.wsdl.ale.epcglobal.ImplementationException;
import org.fosstrak.ale.wsdl.ale.epcglobal.ImplementationExceptionResponse;
import org.fosstrak.ale.xsd.ale.epcglobal.ECFilterSpec;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReport;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReportGroup;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReportGroupCount;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReportGroupList;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReportGroupListMember;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReportSpec;
import org.fosstrak.ale.xsd.epcglobal.EPC;
import org.fosstrak.ale.server.Tag;

/**
 * This class represents a report.
 * It filters and groups tags, add them to the report and build ec reports.
 * 
 * @author regli
 * @author sawielan
 */
public class Report {

	/** logger. */
	private static final Logger LOG = Logger.getLogger(Report.class);
	
	/** name of this report. */
	private final String name;
	/** current event cycle delivers tags. */
	private final EventCycle currentEventCycle;
	
	
	/** patterns of tags which are included in this report. */
	private final Set<Pattern> includePatterns = new HashSet<Pattern>();
	/** patterns of tags which are excluded from this report. */
	private final Set<Pattern> excludePatterns = new HashSet<Pattern>();
	/** patterns to group the tags of this report. */
	private final Set<Pattern> groupPatterns = new HashSet<Pattern>();
	
	/** set of tags of the last event cycle. */
	private final Set<String> lastEventCycleTags = new HashSet<String>();
	
	/** type of this report (current, additions or deletions). */
	private String reportType;

	/** ec report. */
	private ECReport report;
	/** ec report specification. */
	private ECReportSpec reportSpec;
	
	/**
	 * This boolean indicates if the report is ready to create ec reports.
	 * In some cases the last event cycle must be terminated before a new report can be generated.
	 */
	private boolean readyToCreateReport;
	
	/**
	 * Constructor set parameters, read specifiaction and initializes patterns.
	 * 
	 * @param reportSpec defines how the report should be generated
	 * @param currentEventCycle this report belongs to
	 * @throws ImplementationException if an implementation exception occurs
	 */
	public Report(ECReportSpec reportSpec, EventCycle currentEventCycle) throws ImplementationExceptionResponse {
		
		// set name
		name = reportSpec.getReportName();
		
		LOG.debug("Create report '" + name + "'");
		
		// create ECReport
		report = new ECReport();
		
		// set ECReport name
		report.setReportName(name);
		
		// set type
		reportType = reportSpec.getReportSet().getSet();

		// set ECReportSpec
		this.reportSpec = reportSpec;
		
		// set currentEventCycle
		this.currentEventCycle = currentEventCycle;
				
		// init patterns
		initFilterPatterns();
		initGroupPatterns();
		readyToCreateReport = true;

	}

	/**
	 * This method adds a tag to the report.
	 * 
	 * @param tag to add
	 * @throws ECSpecValidationException if the tag is invalid
	 * @throws ImplementationException if an implementation exception occurs
	 */
	public void addTag(Tag tag) throws ECSpecValidationExceptionResponse, ImplementationExceptionResponse {

		// get tag URI
		String tagURI = tag.getTagIDAsPureURI();
	
		// check if the tag is a member of this report (use filter patterns and set spec)
		if (isMember(tagURI)) {
	
				LOG.debug("Event '" + tag + "' is member of report '" + name + "'");
			
				// add tag to report
				addTagToReportGroup(tag);
		}
	}
	
	/**
	 * this method is for compatibility reasons such that eg ReportTest is not broken.
	 * @param tag to add
	 * @throws ECSpecValidationException if the tag is invalid
	 * @throws ImplementationException if an implementation exception occurs
	 */
	public void addTag(org.fosstrak.reader.rprm.core.msg.notification.TagType tag) throws ECSpecValidationExceptionResponse, ImplementationExceptionResponse {
		Tag newtag = new Tag();
		newtag.setTagID(tag.getTagID());
		newtag.setTagIDAsPureURI(tag.getTagIDAsPureURI());
		addTag(newtag);
	}

	/**
	 * helper method to display tags that were added or deleted.
	 * @param reportTags a map holding the tags that were either added or deleted.
	 */
	private void writeDebugInformation(Map<String, Tag> reportTags) {
		String out = '\n' + "+++++++++++++++++++++++++++++++++++++++++++++++++++++" + '\n';
		out +=  '\t' + "eventcycle " + currentEventCycle.getName() + '\n';
		out +=  '\t' + "round " + currentEventCycle.getRounds() + '\n';
		if (reportTags == null) {
			out += '\t' + "no tags" + '\n';
			out +=  "+++++++++++++++++++++++++++++++++++++++++++++++++++++" + '\n';
			LOG.info(out);
			return;
		}
		
		
		for (Tag tag : reportTags.values()) {
			out += '\t' + tag.getTagIDAsPureURI() + '\n';
		}
		out +=  "+++++++++++++++++++++++++++++++++++++++++++++++++++++" + '\n';
		LOG.info(out);
	}
	
	/**
	 * This method returns the new ec report.
	 * 
	 * @return ec report
	 * @throws ECSpecValidationException if a tag is invalid
	 * @throws ImplementationException if an implementation exception occurs
	 */
	public ECReport getECReport() throws ECSpecValidationExceptionResponse, ImplementationExceptionResponse {

		//generate new ECReport
		if (reportType.equalsIgnoreCase(ECReportSetEnum.ADDITIONS)) {
			
			// get additional tags
			Map<String, Tag> reportTags = new HashMap<String, Tag>();

			// add tags from current EventCycle 
			for (Tag tag : currentEventCycle.getTags()) {
				reportTags.put(tag.getTagIDAsPureURI(), tag);
			}
				
			// remove tags from last EventCycle
			if (currentEventCycle.getLastEventCycleTags() != null) {
				for (Tag tag : currentEventCycle.getLastEventCycleTags()) {
					reportTags.remove(tag.getTagIDAsPureURI());
				}
			}
				
			// add tags to report with filtering
			readyToCreateReport = true;

			for (Tag tag : reportTags.values()) {
				addTag(tag);
			}
			//writeDebugInformation(reportTags);
	
		} else if (reportType.equalsIgnoreCase(ECReportSetEnum.CURRENT)) {

			// get tags from current EventCycle 
			for (Tag tag : currentEventCycle.getTags()) {
				addTag(tag);
			}

		//} else if (reportType == ECReportSetEnum.DELETIONS) {
		} else if (reportType.equalsIgnoreCase(ECReportSetEnum.DELETIONS)) {
			
			// get removed tags
			Map<String, Tag> reportTags = new HashMap<String, Tag>();
				
			// add tags from last EventCycle
			if (currentEventCycle.getLastEventCycleTags() != null) {
				for (Tag tag : currentEventCycle.getLastEventCycleTags()) {
					reportTags.put(tag.getTagIDAsPureURI(), tag);
				}
			}
				
			// remove tags from current EventCycle
			for (Tag tag : currentEventCycle.getTags()) {
				reportTags.remove(tag.getTagIDAsPureURI());
			}
				
			// add tags to report with filtering
			readyToCreateReport = true;
			for (Tag tag : reportTags.values()) {
				addTag(tag);
			}
			//writeDebugInformation(reportTags);
		}

		if (reportSpec.isReportIfEmpty() || !isEmpty()) {
			ECReport temp = report;	
			report = new ECReport();
			report.setReportName(name);	
			return temp;
		} else {
			report = new ECReport();
			report.setReportName(name);
			return null;
		}
	}

	//
	// private methods
	//
	
	/**
	 * This method initializes the filter patterns on the basis of the ec report specification.
	 */
	private void initFilterPatterns() {
	
		LOG.debug("Init filter patterns");
		
		// get filter spec
		ECFilterSpec filterSpec = reportSpec.getFilterSpec();
		if (filterSpec != null) {
			
			// add ECIncludePatterns from spec to includePatterns set
			List<String> ecIncludePatterns = filterSpec.getIncludePatterns().getIncludePattern();
			if (ecIncludePatterns != null) {
				for (String pattern : ecIncludePatterns) {
					try {
						includePatterns.add(new Pattern(pattern, PatternUsage.FILTER));
					} catch (ECSpecValidationExceptionResponse e) {
						e.printStackTrace();
					}
				}
			}
			
			// add ECExcludePatterns from spec to excludePatterns set
			List<String> ecExcludePatterns = filterSpec.getExcludePatterns().getExcludePattern();
			if (ecExcludePatterns != null) {
				for (String pattern : ecExcludePatterns) {
					try {
						excludePatterns.add(new Pattern(pattern, PatternUsage.FILTER));
					} catch (ECSpecValidationExceptionResponse e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}
	
	/**
	 * This method initializes the group patterns on the basis of the ec report specification.
	 */
	private void initGroupPatterns() {
		
		LOG.debug("Init group patterns");
		if (reportSpec.getGroupSpec() != null) {
			// get group spec
			List<String> groupSpec = reportSpec.getGroupSpec().getPattern();
			// add ECGroupPatterns from spec to groupPatterns set
			for (String pattern : groupSpec) {
				try {
					groupPatterns.add(new Pattern(pattern, PatternUsage.GROUP));
				} catch (ECSpecValidationExceptionResponse e) {
					e.printStackTrace();
				}	
			}
		}		
	}
	
	/**
	 * This method checks on the basis of the filter patterns if the specified tag could be a member of this report.
	 * 
	 * @param tagURI to check for possible membership
	 * @return true if the tag could be a member of this report and false otherwise
	 * @throws ECSpecValidationException if the tag is invalid
	 * @throws ImplementationException if an implementation exception occurs
	 */
	private boolean isMember(String tagURI) throws ECSpecValidationExceptionResponse, ImplementationExceptionResponse {
				
		if (reportType.equalsIgnoreCase(ECReportSetEnum.ADDITIONS)) {
		
			// if report type is additions the tag is only a member if it wasn't a member of the last event cycle	
			Set<Tag> tags = currentEventCycle.getLastEventCycleTags();
			if (tags != null) {
				for (Tag tag : tags) {
					if (tag.getTagIDAsPureURI().equals(tagURI)) {
						return false;
					}
				}
			}
		}			

		// check if tagURI is member of an exclude pattern
		for (Pattern pattern : excludePatterns) {
			if (pattern.isMember(tagURI)) {
				return false;
			}
		}
		
		// check if there are include patterns specified
		if (includePatterns.size() == 0) {
			return true;
		} else {
			
			// check if tagURI is a member of an include pattern
			for (Pattern pattern : includePatterns) {
				if (pattern.isMember(tagURI)) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * This method adds a tag to the matching group of the report.
	 * 
	 * @param tag to add
	 * @throws ECSpecValidationException if the tag is invalid
	 * @throws ImplementationException if an implementation exception occurs
	 */
	private void addTagToReportGroup(Tag tag) throws ImplementationExceptionResponse, ECSpecValidationExceptionResponse {
		
		// get tag URI
		String tagURI = tag.getTagIDAsPureURI();
		
		// get group name (use group patterns)
		String groupName = getGroupName(tagURI);
		
		LOG.debug("The group name for tag '" + tagURI + "' is '" + groupName + "'");
		
		// get matching group
		ECReportGroup matchingGroup = null;
		List<ECReportGroup> groups = report.getGroup();
		//if (groups == null) {
		if (groups.isEmpty()) {
			matchingGroup = null;
		} else {
			for (ECReportGroup group : groups) {
				if (groupName == null) {
					if (group.getGroupName() == null) {
						matchingGroup = group;
					}
				} else {
					if (groupName.equals(group.getGroupName())) {
						matchingGroup = group;
					}
				}
			}
		}
	
		// create group if group does not already exist
		if (matchingGroup == null) {
			
			LOG.debug("Group '" + groupName + "' does not already exist, create it");
						
			// create group
			matchingGroup = new ECReportGroup();
			
			// set name
			matchingGroup.setGroupName(groupName);
			
			// set count
			if (reportSpec.getOutput().isIncludeCount()) {
				ECReportGroupCount groupCount = new ECReportGroupCount();
				groupCount.setCount(0);
				matchingGroup.setGroupCount(groupCount);
			}
			
			// create and set group list
			matchingGroup.setGroupList(new ECReportGroupList());
			
			// add to groups
			report.getGroup().add(matchingGroup);
			
		}
		
		// create group list member
		ECReportGroupListMember groupMember = new ECReportGroupListMember();
		if (reportSpec.getOutput().isIncludeRawDecimal()) {
			EPC epc = new EPC();
			epc.setValue(tag.getTagIDAsPureURI());
			groupMember.setRawDecimal(epc);
		}
		if (reportSpec.getOutput().isIncludeTag()) {
			EPC epc = new EPC();
			epc.setValue(tag.getTagIDAsPureURI());
			groupMember.setTag(epc);
		}
		if (reportSpec.getOutput().isIncludeRawHex()) {
			if (tag.getTagID() != null) {
				EPC epc = new EPC();
				epc.setValue(HexUtil.byteArrayToHexString(tag.getTagID()));
				groupMember.setRawHex(epc);
			}
		}
		
		// add list member to group list
		List<ECReportGroupListMember> members = matchingGroup.getGroupList().getMember();		members.add(groupMember);
		
		// increment group counter
		if (reportSpec.getOutput().isIncludeCount()) {
			matchingGroup.getGroupCount().setCount(matchingGroup.getGroupCount().getCount() + 1);
		}
		
		LOG.debug("Tag '" + tagURI + "' successfully added to group '" + groupName + "' of report '" + name + "'");
		
	}

	/**
	 * This method get the matching group of this report for the specified tag.
	 * 
	 * @param tagURI to search group for
	 * @return group name
	 * @throws ECSpecValidationException if the tag is invalid
	 * @throws ImplementationException if an implementation exception occurs
	 */
	private String getGroupName(String tagURI) throws ImplementationExceptionResponse, ECSpecValidationExceptionResponse {
		
		for (Pattern pattern : groupPatterns) {
			if (pattern.isMember(tagURI)) {
				return pattern.getGroupName(tagURI);
			}
		}
			
		return null;
		
	}
	
	/**
	 * This method indicates if the report contains any tags.
	 * 
	 * @return true if the report is empty and false otherwise
	 */
	private boolean isEmpty() {
		
		List<ECReportGroup> groups = report.getGroup();
		if (groups != null) {
			for (ECReportGroup group : groups) {
				ECReportGroupList groupList = group.getGroupList();
				if (groupList.getMember().size() > 0) {
					return false;
				}
			}
		}
		return true;

	}
}