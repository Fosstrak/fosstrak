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

package org.fosstrak.llrp.commander.persistence.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.fosstrak.llrp.adaptor.exception.LLRPRuntimeException;
import org.fosstrak.llrp.client.LLRPMessageItem;
import org.fosstrak.llrp.client.ROAccessReportsRepository;
import org.fosstrak.llrp.client.Repository;
import org.fosstrak.llrp.client.RepositoryFactory;
import org.fosstrak.llrp.client.repository.sql.DerbyRepository;
import org.fosstrak.llrp.client.repository.sql.roaccess.ROAccessItem;
import org.fosstrak.llrp.commander.llrpaccess.LLRPAccess;
import org.fosstrak.llrp.commander.persistence.Persistence;
import org.fosstrak.llrp.commander.persistence.exception.PersistenceException;
import org.fosstrak.llrp.commander.persistence.type.PersistenceDescriptor;
import org.fosstrak.llrp.commander.persistence.type.RepositoryFactoryDelegate;
import org.llrp.ltk.generated.messages.RO_ACCESS_REPORT;

/**
 * default implementation of the persistence layer.
 * @author swieland
 *
 */
public class PersistenceImpl implements Persistence {

	public static final String DB_STORE_LOCATION = "org.fosstrak.llrp.commander.persistence.defaultDbLocation";
	
	private static final Logger LOG = Logger.getLogger(PersistenceImpl.class);
	
	private Repository repository;
	private LLRPAccess llrpAccess;
	private RepositoryFactoryDelegate repositoryFactory;
	
	/**
	 * construct this persistence layer.
	 * @param llrpAccess the handle onto the LLRP access layer.
	 */
	public PersistenceImpl(LLRPAccess llrpAccess) {
		this.llrpAccess = llrpAccess;
	}

	@Override
	public PersistenceException initialize(boolean useFallbackOnly, PersistenceDescriptor desc) throws LLRPRuntimeException {
		try {
			assertDescriptorNotNull(desc);
		} catch (PersistenceException e1) {
			throw new LLRPRuntimeException(e1);
		}
		
		Map<String, String> args = map(desc);
		PersistenceException initialException = null;
		final String dbLocation = System.getProperty(DB_STORE_LOCATION);
		
		if (!useFallbackOnly) {
			try {
				repository = repositoryFactory.create(args);
			} catch (PersistenceException e) {
				LOG.error("Could not invoke the repository, using fallback", e);
				initialException = e;
				// just to be sure
				repository = null;
			}
		}
		
		if (useFallbackOnly || (null == repository)) {
			LOG.debug("Starting internal Derby database.");
			args.put(DerbyRepository.ARG_REPO_LOCATION, 	dbLocation);
			args.put(RepositoryFactory.ARG_USERNAME, 		StringUtils.EMPTY);
			args.put(RepositoryFactory.ARG_PASSWRD, 		StringUtils.EMPTY);
			args.put(RepositoryFactory.ARG_JDBC_STRING, 	StringUtils.EMPTY);
			repository = new DerbyRepository();
			try {
				repository.initialize(args);
			} catch (LLRPRuntimeException e) {
				repository = null;
				throw e;
			}
		}
		
		return initialException;
	}

	@Override
	public boolean change(PersistenceDescriptor desc) throws PersistenceException {
		assertDescriptorNotNull(desc);
		if (!verifyOldRepoNotSame(desc)) {
			return false;
		}
		
		// try to open the repository. if it works out, switch it.
		Repository newRepository = repositoryFactory.create(map(desc));
		Repository old = repository;
		repository = newRepository;
		
		if (null != old) {
			if (null != old.getROAccessRepository()) {
				llrpAccess.deregisterPartialHandler(old.getROAccessRepository(), RO_ACCESS_REPORT.class);
			}
			// stop the old repository
			try {
				old.close();
			} catch (Exception repoE) {
				LOG.error("Old repository could not be stopped.", repoE);
			}
		}
		return true;
    }

	@Override
	public boolean test(PersistenceDescriptor desc) throws PersistenceException {
		assertDescriptorNotNull(desc);
		if (!verifyOldRepoNotSame(desc)) {
			return false;
		}
		
		Repository testRepository = repositoryFactory.create(map(desc));
		testRepository.close();
		return true;
	}
	
	private void assertDescriptorNotNull(PersistenceDescriptor desc) throws PersistenceException {
		if (null == desc) {
			throw new PersistenceException("descriptor must not be null");
		}
	}
	
	private boolean verifyOldRepoNotSame(PersistenceDescriptor desc) {
		if ((null != repository) && (repository.getClass().getName().equals(desc.getImplementingClass()))) {
			LOG.info("instantiate twice the same repository is not allowed.");
			return false;
		}
		return true;
	}
	
	private Map<String, String> map(PersistenceDescriptor desc) {
		Map<String, String> args = new HashMap<String, String> ();
  	  	
		args.put(RepositoryFactory.ARG_WIPE_DB, 					String.format("%b", desc.isWipeDbAtStartup()));
  	  	args.put(RepositoryFactory.ARG_WIPE_RO_ACCESS_REPORTS_DB,	String.format("%b", desc.isWipeRoAccessDbAtStartup()));
  	  	args.put(RepositoryFactory.ARG_LOG_RO_ACCESS_REPORT,		String.format("%b", desc.isLogRoAccess()));
  	  	args.put(RepositoryFactory.ARG_USERNAME,					desc.getUsername());
  	  	args.put(RepositoryFactory.ARG_PASSWRD,						desc.getPassword());
  	  	args.put(RepositoryFactory.ARG_JDBC_STRING,					desc.getJdbc());
  	  	args.put(RepositoryFactory.ARG_DB_CLASSNAME,				desc.getImplementingClass());
  	  	
  	  	return args;
	}

	@Override
	public boolean supportsRoAccessRepository() {
		return ((null != repository) && (null != repository.getROAccessRepository()));
	}

	@Override
	public void put(LLRPMessageItem message) {
		if (null == message) {
			LOG.trace("not adding null message to the repository.");
			return;
		}
		
		//Add the Repository
		LOG.trace("adding message to the repository." + message);
		repository.put(message);
	}

	@Override
	public LLRPMessageItem get(String id) {		
		if (null == id) {
			LOG.trace("id is null - returning null.");
			return null;
		}

		return repository.get(id);
	}

	@Override
	public List<LLRPMessageItem> get(String adaptorName, String readerName, int num, boolean content) {
		int retrieve = num;
		if (RETRIEVE_ALL == num) {
			retrieve = Repository.RETRIEVE_ALL;
		}
		return repository.get(adaptorName, readerName, retrieve, content);
	}

	@Override
	public List<ROAccessItem> getAllRoAccessReports() {
		if (null == repository) {
			return Collections.emptyList();
		}
		ROAccessReportsRepository rorepo = repository.getROAccessRepository();
		if (null == rorepo) {
			return Collections.emptyList();
		}
		try {
			return rorepo.getAll();
		} catch (Exception e) {
			LOG.error("could not load ro access reports messages from db", e);
			return Collections.emptyList();
		}
	}
	
	@Override
	public boolean isClean() {
		if (null == repository) {
			return false;
		}
		return repository.isHealth();
	}

	@Override
	public int count(String adaptor, String reader) {
		return repository.count(adaptor, reader);
	}

	@Override
	public void clearAll() {
		repository.clearAll();
	}

	@Override
	public void clearAdapter(String adapter) {
		repository.clearAdapter(adapter);
	}

	@Override
	public void clearReader(String adapter, String reader) {
		repository.clearReader(adapter, reader);
	}

	@Override
	public void clearRoAccessReports() {
		if (null == repository) {
			return;
		}
		ROAccessReportsRepository rorepo = repository.getROAccessRepository();
		if (null == rorepo) {
			return;
		}
		try {
			rorepo.clear();
		} catch (Exception e) {
			LOG.error("could not clear ro access reports db", e);
		}
	}

	@Override
	public void close() {
		repository.close();
	}

	@Override
	public void registerForRoAccessReports() {
		// get a handle of the repository.
		ROAccessReportsRepository r = repository.getROAccessRepository();
		if (null != r) {
			LOG.debug("initializing RO_ACCESS_REPORTS logging facility.");			
			llrpAccess.registerPartialHandler(r, RO_ACCESS_REPORT.class);
		}
	}
	
	/**
	 * allows to inject a repository. mainly used for testing purposes.
	 * @param repository the new repository.
	 */
	public void setRepository(Repository repository) {
		this.repository = repository;
	}
	
	/**
	 * allows to inject a repository factory delegate.
	 * @param repositoryFactory the new delegate to use.
	 */
	public void setRepositoryFactory(RepositoryFactoryDelegate repositoryFactory) {
		this.repositoryFactory = repositoryFactory;
	}
}
