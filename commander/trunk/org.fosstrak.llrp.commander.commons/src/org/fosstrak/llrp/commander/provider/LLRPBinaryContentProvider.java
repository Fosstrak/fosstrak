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

package org.fosstrak.llrp.commander.provider;

import org.eclipse.jface.viewers.*;
import org.fosstrak.llrp.commander.type.BinaryMessage;
import org.fosstrak.llrp.commander.type.BinarySingleValue;

/**
 * Provides the content of an LLRP message for a tree viewer. This content 
 * provider returns the content encoded as an array of BinarySingleValue.
 * @author Haoning Zhang
 * @author sawielan
 *
 */
public class LLRPBinaryContentProvider implements ITreeContentProvider {

	private BinaryMessage message;
	
	public void inputChanged (Viewer aViewer, Object aOldInput, Object aNewInput) {
		message = (BinaryMessage) aNewInput;
	}
	
	public Object[] getElements(Object aElement) {
		return getChildren(aElement);
	}
	
	/**
	 * @return an array encoding the content of the LLRP message for the 
	 * binary editor. The important values are encoded as {@link BinarySingleValue}.
	 */
	public Object[] getChildren(Object aElement) {
		if (aElement instanceof BinaryMessage) {
			BinaryMessage msg = (BinaryMessage) aElement;
			
			// to achieve a nicer presentation in the binary view, we 
			// use the split parameters instead of one huge binary string.
			
			BinarySingleValue[] arr = msg.getSplitParameters();
			int len = 5 + arr.length;
			BinarySingleValue[] values = new BinarySingleValue[len];
			values[0] = msg.getReserved();
			values[1] = msg.getVersion();
			values[2] = msg.getMsgType();
			values[3] = msg.getMsgID();
			values[4] = msg.getMsgLength();
			//values[5] = msg.getParameters();
			
			for (int i=5; i<len; i++) {
				values[i] = arr[i-5];
			}
			
			return values;
		}
		
		return null;
	}
	
	public Object getParent(Object aElement) {
		if (aElement instanceof BinarySingleValue) {
			BinarySingleValue value = (BinarySingleValue) aElement;
			return value.getParent();
		}
		return null;
	}
	
	public boolean hasChildren(Object aElement) {
		if (aElement instanceof BinaryMessage) {
			return true;
		}
		
		return false;
	}
	
	public void dispose() {
		
	}
}
