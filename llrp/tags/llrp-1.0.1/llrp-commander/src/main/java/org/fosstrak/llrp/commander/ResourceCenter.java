/*
 *  
 *  Fosstrak LLRP Commander (www.fosstrak.org)
 * 
 *  Copyright (C) 2008 ETH Zurich
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/> 
 *
 */

package org.fosstrak.llrp.commander;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.fosstrak.llrp.adaptor.AdaptorManagement;
import org.fosstrak.llrp.adaptor.exception.LLRPRuntimeException;
import org.fosstrak.llrp.client.LLRPExceptionHandlerTypeMap;
import org.fosstrak.llrp.client.LLRPMessageItem;
import org.fosstrak.llrp.client.MessageHandler;
import org.fosstrak.llrp.client.ROAccessReportsRepository;
import org.fosstrak.llrp.client.Repository;
import org.fosstrak.llrp.client.RepositoryFactory;
import org.fosstrak.llrp.client.repository.sql.DerbyRepository;
import org.fosstrak.llrp.commander.preferences.PreferenceConstants;
import org.fosstrak.llrp.commander.repository.MessageModel;
import org.fosstrak.llrp.commander.util.LLRP;
import org.fosstrak.llrp.commander.util.MessageBoxRefresh;
import org.fosstrak.llrp.commander.util.Utility;
import org.fosstrak.llrp.commander.views.MessageboxView;
import org.fosstrak.llrp.commander.views.ReaderExplorerView;
import org.jdom.Document;
import org.llrp.ltk.exceptions.InvalidLLRPMessageException;
import org.llrp.ltk.generated.LLRPMessageFactory;
import org.llrp.ltk.generated.messages.RO_ACCESS_REPORT;
import org.llrp.ltk.generated.parameters.LLRPStatus;
import org.llrp.ltk.types.LLRPMessage;

/**
 * This single access point for lower level resources, like Reader and Messages, from
 * the GUI side. The class apply the <strong>Singleton</strong> pattern.
 *
 * @author Haoning Zhang
 * @author sawielan
 * @version 1.0
 */
public class ResourceCenter {

	/**
	 * Maximal message retrieval number
	 */
	public final static int GET_MAX_MESSAGES = 25;
	
	/**
	 * Default Eclipse Project for storing editable messages
	 */
	public final static String DEFAULT_ECLIPSE_PROJECT = "LLRP_CMDR";
	
	/**
	 * Default reader configuration file name
	 */
	public final static String DEFAULT_READER_DEF_FILENAME = "readers.xml";
	
	/**
	 * Pre-built folder, for opened incoming messages
	 */
	public final static String REPO_SUBFOLDER = "Temporary";
	
	/**
	 * Pre-built folder, for editable outgoing messages
	 */
	public final static String DRAFT_SUBFOLDER = "Draft";
	
	/**
	 * Pre-built folder, for messages template (samples)
	 */
	public final static String SAMPLE_SUBFOLDER = "Sample";
	
	/** folder storing the configuration files. */
	public final static String CONFIG_SUBFOLDER = "cfg";
	
	/** the name of the configuration file for the reader configuration. */
	public final static String RDR_CFG_FILE = "rdrCfg.properties";
	
	public final static String DB_SUBFOLDER = "db"; 
	
	private static ResourceCenter instance;
	
	private Repository repo;
	
	private static Logger log = Logger.getLogger(ResourceCenter.class);
	
	private MessageModel messageModel;
	
	private String eclipseProjectName;
	
	private String readerDefinitionFilename;
	
	private ExceptionHandler exceptionHandler;
	
	private HashMap<String, String> readerConfigMap;
	
	private HashMap<String, String> readerROSpecMap;
	
	private ReaderExplorerView readerExplorerView;
	
	/**
	 * Only store meta data, without XML content, to save the memory
	 */
	private ArrayList<LLRPMessageItem> messageList;
	
	/** the worker thread that refreshes the message box periodically. */
	private MessageBoxRefresh messageBoxRefresh = null;
	
	/** flags whether the adaptor management has been initialized or not. */
	private boolean adapterMgmtInitialized = false;
	
	// flags, whether the RO_ACCESS_REPORTS logging facility has been initialized.
	private boolean roAccessReportsLogginInitialized = false;
	
	/** use an image cache in order not to recreate images over and over again.*/
	private Map<String, Image> imageCache = new HashMap<String, Image> ();
	
    /**
     * Private Constructor, internally called.
     */
	private ResourceCenter() {

		// load class LLRP
		LLRP.getLlrpDefintion();
			
		setEclipseProjectName(DEFAULT_ECLIPSE_PROJECT);
		setReaderDefinitionFilename(DEFAULT_READER_DEF_FILENAME);
		
		messageList = new ArrayList<LLRPMessageItem>();
		
		messageModel = new MessageModel();
		
		readerConfigMap = new HashMap<String, String>();
		readerROSpecMap = new HashMap<String, String>();
	}
	
	/**
	 * helper to initialize the adaptor management at the right moment.
	 */
	public void initializeAdaptorMgmt() {
		if (adapterMgmtInitialized) {
			log.info("adaptor management already initialized");
			return;
		}
		
		IProject project = getEclipseProject();
		// refresh the workspace...
		try {
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e1) {
			e1.printStackTrace();
		}
		
		// check if the configuration folder exists.
		IFolder configFolder = project.getFolder(
				ResourceCenter.CONFIG_SUBFOLDER);
		if (!configFolder.exists()) {
			try {
				log.info("create new config folder...");
				configFolder.create(true, true, null);
				log.info("created config folder.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// check if the reader configuration exists.
		IFile cfg = configFolder.getFile(
				ResourceCenter.RDR_CFG_FILE
				);
		
		if (cfg.exists()) {
			log.info("found configuration file - good.");
		} else {
			log.info("reader configuration file missing. create new...");
			String defaultCFG = Utility.findWithFullPath(
					"/readerDefaultConfig.properties");
			
			try {
				// copy the file
				InputStream in = new FileInputStream(new File(defaultCFG));					
				cfg.create(in, false, null);
				in.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// create our message handler
		MessageHandler handler = new MessageHandler() {

			public void handle(String adapter, String reader, LLRPMessage msg) {
				LLRPMessageItem item = new LLRPMessageItem();
				item.setAdapter(adapter);
				item.setReader(reader);
				
				String msgName = msg.getName();
				item.setMessageType(msgName);
				
				// if the message contains a "LLRPStatus" parameter, set the status code (otherwise use empty string)
				String statusCode = "";
				try {
					Method getLLRPStatusMethod = msg.getClass().getMethod("getLLRPStatus", new Class[0]);
					LLRPStatus status = (LLRPStatus) getLLRPStatusMethod.invoke(msg, new Object[0]);
					statusCode = status.getStatusCode().toString();
				} catch (Exception e) {
					// do nothing
				} 
				item.setStatusCode(statusCode);
				
				// store the xml string to the repository
				try {
					item.setContent(msg.toXMLString());
				} catch (InvalidLLRPMessageException e) {
					e.printStackTrace();
				}
				
				try {
					getRepository().put(item);
				} catch (Exception e) {
					// repository might be null
					e.printStackTrace();
				}
				
				// add the message to the meta data list.
				addToMessageMetadataList(item);
				
				if (item.getMessageType().equals("GET_READER_CONFIG_RESPONSE")) {
					ResourceCenter.getInstance().addReaderConfig(
							item.getAdapter(), 
							item.getReader(), 
							item.getId());
				}
				
				if (item.getMessageType().equals("GET_ROSPECS_RESPONSE")) {
					ResourceCenter.getInstance().addReaderROSpec(
							item.getAdapter(), 
							item.getReader(), 
							item.getId());
				}
			}
		};
		
		AdaptorManagement.getInstance().registerFullHandler(handler);
		IWorkspaceRoot myWorkspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		String readConfig = myWorkspaceRoot.getLocation().toString() + 
				cfg.getFullPath().toString();
		
		String storeConfig = readConfig;
		boolean commitChanges = true;
		try {
			AdaptorManagement.getInstance().initialize(
					readConfig, storeConfig, commitChanges, null, null);
		} catch (LLRPRuntimeException e) {
			e.printStackTrace();
		}
		
		adapterMgmtInitialized = true;
	}	
	
	/**
	 * initialize the RO_ACCESS_REPORTS logging facility. This initializer 
	 * should be called once. Basically it registers the repository on the 
	 * adapter management to be notified about new RO_ACCESS_REPORTS.
	 */
	public void initializeROAccessReportsLogging() {
		if (roAccessReportsLogginInitialized) return;
		
		// get a handle of the repository.
		ROAccessReportsRepository r = getRepository().getROAccessRepository();
		if ((null != r) && (r instanceof MessageHandler)) {
			log.debug("initializing RO_ACCESS_REPORTS logging facility.");			
			AdaptorManagement.getInstance().registerPartialHandler(
					(MessageHandler)r, RO_ACCESS_REPORT.class);
		}
		roAccessReportsLogginInitialized = true;
	}
	
	/**
	 * Get the Message model
	 * 
	 * @return Message Model
	 */
	public MessageModel getMessageModel() {
		return messageModel;
	}
	
    /**
     * Return the only instance of this class, call the Constructor in its first call.
     *
     */
	public static ResourceCenter getInstance() {
		if (null == instance) {
			instance = new ResourceCenter();
		}
		return instance;
	}
	
	/**
	 * Add LLRP Message Item to Repository and Content Provider.
	 * 
	 * @param aNewMessage Incoming LLRP Message
	 */
	public void addMessage(LLRPMessageItem aNewMessage) {
		if (null == aNewMessage) {
			return;
		}
		
		//Add the Repository
		repo.put(aNewMessage);
	}
	
	/**
	 * Get the message meta data list.
	 * 
	 * @return Message meta data list 
	 */
	public ArrayList<LLRPMessageItem> getMessageMetadataList() {
		return messageList;
	}
	
	/**
	 * Add new message meta data item into list
	 * @param aNewMessage New message meta data item
	 */
	public void addToMessageMetadataList(LLRPMessageItem aNewMessage) {
		//Remove XML Content to save the memory, then put into the 1st place of the list
		aNewMessage.setContent("");
		messageList.add(aNewMessage);
		
		// flag the refresher to refresh the messagebox 
		if (messageBoxRefresh != null) {
			messageBoxRefresh.setDirty();
		}
	}
	
	/**
	 * Clear all data in meta data list
	 */
	public void clearMessageMetadataList() {
		messageList.clear();
	}
	
	/**
	 * Get LLRP XML content by Message ID
	 * @param aMsgId Message ID
	 * @return LLRP XML Content
	 */
	public String getMessageContent(String aMsgId) {
		
		if (null == aMsgId) {
			return null;
		}
		
		LLRPMessageItem msg = repo.get(aMsgId);
		
		return msg.getContent().equals("") ? null : msg.getContent();
	}
	
	/**
	 * allows to set a new repository.
	 * @param repo the new repository.
	 * @return the old repository.
	 */
	public Repository setRepository(Repository repo) {
		Repository old = this.repo;
		this.repo = repo;
		return old;
	}
	
	/**
	 * Get the Repository interface.
	 * 
	 * @return Repository interface
	 */
	public Repository getRepository() {
		if (repo == null) {
			
			log.debug("open/create new repository");
			IProject project = getEclipseProject();
			// refresh the workspace...
			try {
				project.refreshLocal(IResource.DEPTH_INFINITE, null);
			} catch (CoreException e1) {
				e1.printStackTrace();
			}
			
			IWorkspaceRoot myWorkspaceRoot = 
				ResourcesPlugin.getWorkspace().getRoot();
			IFolder dbFolder = project.getFolder(
					ResourceCenter.DB_SUBFOLDER);
			
			String dbLocation = myWorkspaceRoot.getLocation().toString() + 
					dbFolder.getFullPath().toString() + "/";
			
			log.info("using db location: " + dbLocation);
			IPreferenceStore store = LLRPPlugin.getDefault().getPreferenceStore();
			boolean internalDB = store.getBoolean(
					PreferenceConstants.P_USE_INTERNAL_DB);
			
			repo = null;
			boolean wipeRO = store.getBoolean(
					PreferenceConstants.P_WIPE_RO_ACCESS_REPORTS_ON_STARTUP);
			boolean wipe = store.getBoolean(
					PreferenceConstants.P_WIPE_DB_ON_STARTUP
					);
			boolean logRO = store.getBoolean(
					PreferenceConstants.P_LOG_RO_ACCESS_REPORTS
					);
			Map<String, String> args = new HashMap<String, String> ();
			args.put(RepositoryFactory.ARG_WIPE_DB, 
					String.format("%b", wipe));
			args.put(RepositoryFactory.ARG_WIPE_RO_ACCESS_REPORTS_DB, 
					String.format("%b", wipeRO));
			args.put(RepositoryFactory.ARG_LOG_RO_ACCESS_REPORT, 
					String.format("%b", logRO));
			if (!internalDB) {
				// obtain the user name, password and JDBC connector URL from the 
				// eclipse preference store.
				args.put(RepositoryFactory.ARG_USERNAME,
						store.getString(PreferenceConstants.P_EXT_DB_USERNAME));
				args.put(RepositoryFactory.ARG_PASSWRD,
						store.getString(PreferenceConstants.P_EXT_DB_PWD));
				args.put(RepositoryFactory.ARG_JDBC_STRING,
						store.getString(PreferenceConstants.P_EXT_DB_JDBC));
				args.put(RepositoryFactory.ARG_DB_CLASSNAME,
						store.getString(PreferenceConstants.P_EXT_DB_IMPLEMENTOR));
				
				try {
					repo = RepositoryFactory.create(args);
					
				} catch (Exception e) {
					log.error("Could not invoke the repository, using fallback");
					e.printStackTrace();
					IStatus status = new Status(
							IStatus.WARNING, LLRPPlugin.PLUGIN_ID, 
							"LLRP Repository Warning.", e);
					ErrorDialog.openError(
							LLRPPlugin.getDefault().getWorkbench()
								.getDisplay().getActiveShell(),
							"Could not open Repository - Using fallback.", 
							e.getMessage(), status);
				}
			}
			if (internalDB || (null == repo)) {
				log.debug("Starting internal Derby database.");
				args.put(DerbyRepository.ARG_REPO_LOCATION, dbLocation);
				args.put(RepositoryFactory.ARG_USERNAME, "");
				args.put(RepositoryFactory.ARG_PASSWRD, "");
				args.put(RepositoryFactory.ARG_JDBC_STRING, "");
				repo = new DerbyRepository();
				try {
					repo.initialize(args);
				} catch (LLRPRuntimeException e) {
					e.printStackTrace();
					IStatus status = new Status(
							IStatus.WARNING, LLRPPlugin.PLUGIN_ID,
							"LLRP Repository Warning.", e);
					ErrorDialog.openError(
							LLRPPlugin.getDefault().getWorkbench()
								.getDisplay().getActiveShell(),
							"Could not open Default/Fallback repository " + 
							"LLRP Commander cannot continue properly!", 
							e.getMessage(), status);
				}
			}
		}
		return repo;
	}
	
	/**
	 * @return true if RO_ACCESS_REPORTS shall be logged, false otherwise.
	 */
	public boolean isLogROAccessReports() {
		IPreferenceStore store = LLRPPlugin.getDefault().getPreferenceStore();
		return store.getBoolean(PreferenceConstants.P_LOG_RO_ACCESS_REPORTS);
	}
	
	/**
	 * Get Eclipse Project Name
	 * @return Eclipse Project Name
	 */
	public String getEclipseProjectName() {
		return eclipseProjectName;
	}
	
	/**
	 * Set Eclipse Project Name
	 * @param aName Eclipse Project Name
	 */
	public void setEclipseProjectName(String aName) {
		eclipseProjectName = aName;
	}
	
	/**
	 * Get Eclipse <code>IProject</code> instance
	 * @return Eclipse IProject instance
	 */
	public IProject getEclipseProject() {
		IWorkspaceRoot myWorkspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IProject myWebProject = myWorkspaceRoot.getProject(getEclipseProjectName());
		
		if (!myWebProject.exists()) {
			log.info("Project " + getEclipseProjectName() + " doesn't exists!");
			return null;
		}
		
		try {
			if (myWebProject.exists() && !myWebProject.isOpen()) {
				myWebProject.open(null);
			}
		} catch (CoreException ce) {
			log.debug("could not open project " + ce.getMessage());
		}
		
		return myWebProject;
	}
	
	/**
	 * Get current editing file name
	 * @return Current editing file name
	 */
	public String getCurrentFileName() {
		
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
		String fileName = page.getActiveEditor().getEditorInput().getName();
		
		return fileName;
	}
	
	/**
	 * Get current editing XML content
	 * @return Current editing XML content
	 */
	public String getCurrentFile() {
		
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
		IFileEditorInput input = (IFileEditorInput) page.getActiveEditor().getEditorInput();
		
		StringBuffer aXMLContent = new StringBuffer();
		
		try {
			InputStream is = input.getFile().getContents();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));

			String line;
			while ((line = reader.readLine()) != null) {
				aXMLContent.append(line);
			}

			reader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return aXMLContent.toString();
	}
	
	/**
	 * Helper function. Generate <code>LLRPMessage</code> instance
	 * by XML content.
	 * If the exchange by LTKJava hold errors, return null.
	 * 
	 * @param aXMLFileContent XML file content
	 * @return LLRPMessage instance.
	 */
	public LLRPMessage generateLLRPMessage(String aXMLFileContent) {
		
		LLRPMessage message = null;
		
		log.info("Start generating LLRPMessage...");
		
		try {
			Document doc = new org.jdom.input.SAXBuilder()
					.build(new StringReader(aXMLFileContent));
			
			message = LLRPMessageFactory.createLLRPMessage(doc);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		log.info("LLRPMessage successfully generated.");
		
		return message;
	}
	
	/**
	 * Send LLRP Message.
	 * 
	 * @param aAdapterName Adapter Logical Name
	 * @param aReaderName Reader Logical Name
	 * @param aMessage LLRPMessage instance
	 * @param aComment User Input Comments
	 */
	public void sendMessage(String aAdapterName, String aReaderName, LLRPMessage aMessage, String aComment) {
		try {
			String msgName = aMessage.getName();
			
			LLRPMessageItem  item = new LLRPMessageItem();
			item.setMark(LLRPMessageItem.MARK_OUTGOING);
			item.setAdapter(aAdapterName);
			item.setReader(aReaderName);
			item.setContent(aMessage.toXMLString());
			item.setMessageType(msgName);
			item.setComment(aComment);
			
			this.addMessage(item);
			
			AdaptorManagement.getInstance().enqueueLLRPMessage(aAdapterName, aReaderName, aMessage);
						
		} catch (LLRPRuntimeException e) {
			e.printStackTrace();
		} catch (InvalidLLRPMessageException ive) {
			ive.printStackTrace();
		}
	}
	
	/**
	 * Disconnect all readers.
	 */
	public void disconnectAllReaders() {
		log.info("Disconnecting all readers...");
		AdaptorManagement.getInstance().disconnectReaders();
		
		AdaptorManagement.getInstance().shutdown();
	}

	/**
	 * Get Reader definition filename
	 * @return Reader definition filename
	 */
	public String getReaderDefinitionFilename() {
		return readerDefinitionFilename;
	}

	/**
	 * Set Reader definition filename
	 * @param aReaderDefinitionFilename Reader definition filename
	 */
	public void setReaderDefinitionFilename(String aReaderDefinitionFilename) {
		readerDefinitionFilename = aReaderDefinitionFilename;
	}
		
	/**
	 * Get <code>Image</code> from icon folder
	 * @param aFilename Image filename
	 * @return Image instance
	 */
	public Image getImage(String aFilename) {
		synchronized (imageCache) {
			if (imageCache.containsKey(aFilename)) {
				return imageCache.get(aFilename);
			}
			log.debug("Generate Image:" + "icons/" + aFilename);
			Image img = LLRPPlugin.getImageDescriptor("icons/" + aFilename).createImage();
			imageCache.put(aFilename, img);
			return img;
		}
	}
	
	/**
	 * Get <code>ImageDescriptor</code> from icon folder
	 * @param aFilename Image filename
	 * @return ImageDescriptor instance
	 */
	public ImageDescriptor getImageDescriptor(String aFilename) {
		log.debug("Generate ImageDescriptor:" + "icons/" + aFilename);
		return LLRPPlugin.getImageDescriptor("icons/" + aFilename);
	}
	
	public void setExceptionHandler(ExceptionHandler aHandler) {
		exceptionHandler = aHandler;
		AdaptorManagement.getInstance().setExceptionHandler(aHandler);
	}
	
	public void postExceptionToGUI(LLRPExceptionHandlerTypeMap aExceptionType, String aAdapter, String aReader) {
		if (null == exceptionHandler) {
			return;
		}
		
		exceptionHandler.postExceptionToGUI(aExceptionType, null, aAdapter, aReader);
		
	}
	
	/**
	 * set the message box view in the message box refresh thread. If the 
	 * refresh thread is not started yet, a new instance is generated and the 
	 * thread is started, otherwise the new message box view is registered on 
	 * the running thread.
	 * @param aMessagebox the message box to be set.
	 */
	public void setMessageboxView(MessageboxView aMessagebox) {
		if (messageBoxRefresh == null) {
			messageBoxRefresh = new MessageBoxRefresh(aMessagebox);
			new Thread(messageBoxRefresh).start();
		} else {
			messageBoxRefresh.setMessageBox(aMessagebox);
		}
	}

	/**
	 * @param readerExplorerView the readerExplorerView to set
	 */
	public void setReaderExplorerView(ReaderExplorerView readerExplorerView) {
		this.readerExplorerView = readerExplorerView;
	}


	/**
	 * @return the readerExplorerView
	 */
	public ReaderExplorerView getReaderExplorerView() {
		return readerExplorerView;
	}
	
	public void addReaderConfig(String aAdapterName, String aReaderName, String aMessageID) {
		readerConfigMap.put(aAdapterName + aReaderName, aMessageID);
	}
	
	public void removeReaderConfig(String aAdapterName, String aReaderName) {
		readerConfigMap.remove(aAdapterName + aReaderName);
	}
	
	public String getReaderConfigMsgId(String aAdapterName, String aReaderName) {
		String result = readerConfigMap.get(aAdapterName + aReaderName);
		return result;
	}
	
	public void addReaderROSpec(String aAdapterName, String aReaderName, String aMessageID) {
		readerROSpecMap.put(aAdapterName + aReaderName, aMessageID);
	}
	
	public void removeReaderROSpec(String aAdapterName, String aReaderName) {
		readerROSpecMap.remove(aAdapterName + aReaderName);
	}
	
	public String getReaderROSpecMsgId(String aAdapterName, String aReaderName) {
		String result = readerROSpecMap.get(aAdapterName + aReaderName);
		return result;
	}
	
	public boolean existReaderConfig(String aAdapterName, String aReaderName) {
		String result = getReaderConfigMsgId(aAdapterName, aReaderName);
		return (null == result) ? false : true;
	}
	
	public boolean existReaderROSpec(String aAdapterName, String aReaderName) {
		String result = getReaderROSpecMsgId(aAdapterName, aReaderName);
		return (null == result) ? false : true;
	}
	
	/**
	 * writes a chunk of data to a folder within a requested file.
	 * @param folder the folder where to write to. if null uses "temporary".
	 * @param fileName the file name where to write to. if null, abort.
	 * @param msg the data chunk to be written.
	 * @return the file handle interface of the written file.
	 * @throws Exception when there is a problem.
	 */
	public IFile writeMessageToFile(String folder, 
			String fileName, String msg) throws Exception {
		
		if (null == folder) folder = ResourceCenter.REPO_SUBFOLDER;
		if (null == msg) return null;
		
		if (null == fileName) fileName = String.format(
				"%d.csv", System.currentTimeMillis());
		
		IProject project = getEclipseProject();
		
		// open if necessary
		if (project.exists() && !project.isOpen())
			project.open(null);
		
		IFolder repoFolder = project.getFolder(folder);
		if (!repoFolder.exists()) {
			repoFolder.create(true, true, null);
		}
		IFile msgFile = repoFolder.getFile(fileName);
		
		if (!msgFile.exists()) {
			InputStream is = 
				new ByteArrayInputStream(msg.getBytes());
			msgFile.create(is, false, null);
		}
		return msgFile;
	}
	
	/**
	 * writes the content of the given id into a temporary file.
	 * @param aMsgId the message id to write to file.
	 */
	public void writeMessageToFile(String aMsgId) {
		
		if (null == aMsgId) {
			log.warn("Message is null!");
			return;
		}
		
		String content = getMessageContent(aMsgId);
		
		try {IFile msgFile = writeMessageToFile(
					ResourceCenter.REPO_SUBFOLDER, aMsgId + ".llrp", content);
			// Open new file in editor
			IWorkbench workbench = PlatformUI.getWorkbench();
			IWorkbenchPage page = 
				workbench.getActiveWorkbenchWindow().getActivePage();
			
			IDE.openEditor(page, msgFile, 
					"org.fosstrak.llrp.commander.editors.LLRPEditor", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * tear down the resource center.
	 */
	public void close() {
		log.info("Closing Database...");
		ResourceCenter.getInstance().getRepository().close();
		log.info("Undefine all readers...");
		ResourceCenter.getInstance().disconnectAllReaders();
		log.info("stopping message box refresher...");
		messageBoxRefresh.stop();
	}

	/**
	 * @return the messageBoxRefresh
	 */
	public MessageBoxRefresh getMessageBoxRefresh() {
		return messageBoxRefresh;
	}
}
