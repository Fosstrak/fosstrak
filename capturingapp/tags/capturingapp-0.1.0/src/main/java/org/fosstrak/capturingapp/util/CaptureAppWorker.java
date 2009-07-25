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

package org.fosstrak.capturingapp.util;

import java.io.IOException;
import java.util.concurrent.Future;

import org.fosstrak.capturingapp.CaptureApp;
import org.fosstrak.capturingapp.CaptureAppPortTypeImpl;

/**
 * tiny helper to execute a capture application and at the same time maintain 
 * a handle to it.
 * @author sawielan
 *
 */
public class CaptureAppWorker {
	// the capture application itself.
	private CaptureApp captureApp;
	
	// an identifier for this worker/capture application.
	private final String identifier;
	
	// a handle to the executor service running the capture application.
	@SuppressWarnings("unchecked")
	private Future handle;
	
	/**
	 * creates a new worker.
	 * @param identifier an identifier for this worker/capture application.
	 * @param cap a handle to the executor service running the capture application.
	 */
	public CaptureAppWorker(String identifier, 
			CaptureApp cap) {
		
		this.captureApp = cap;
		this.identifier = identifier;
	}
	
	/**
	 * start the execution of the worker.
	 */
	public void start() {
		handle = CaptureAppPortTypeImpl.submitToThreadPool(captureApp);
	}
	
	/**
	 * stop the execution of the worker.
	 */
	public void stop() {
		try {
			captureApp.stopCaptureApp();
		} catch (IOException e) {
			e.printStackTrace();
		}
		handle.cancel(true);
	}
	
	/**
	 * @return a handle to the capture application.
	 */
	public org.fosstrak.capturingapp.CaptureApp getCaptureApp() {
		return captureApp;
	}
	
	/**
	 * @return the identifier for this worker.
	 */
	public final String getIdentifier() {
		return identifier;
	}
}