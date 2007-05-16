/*
 * Copyright (C) 2007 ETH Zurich
 *
 * This file is part of Accada (www.accada.org).
 *
 * Accada is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1, as published by the Free Software Foundation.
 *
 * Accada is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Accada; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA  02110-1301  USA
 */

package org.accada.reader.rp.proxy.invocationHandlers;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import org.accada.reader.rprm.core.msg.reply.ReadReportType;
import org.accada.reader.rprm.core.msg.reply.Reply;
import org.accada.reader.rp.proxy.RPProxyException;
import org.accada.reader.rp.proxy.ReadReport;
import org.accada.reader.rp.proxy.msg.ProxyConnection;

/**
 * This class is the core piece of the reader device proxy. Each call to a proxy method will be intercepted by this class.
 * The call will be redirected to the proxy connection and the achieved result will be transformed to a suitable type.
 * 
 * @author regli
 */
public class ProxyInvocationHandler implements InvocationHandler {

	/** the proxy connection which is used for command executions */
	protected ProxyConnection proxyConnection;
	
	/** the type of the object to which the proxy of this invocation handler belong to */
	private final String object;
	
	/** the name of the object to which the proxy of this invocation handler belongs to */
	private final String target;

	/**
	 * Constructor sets the parameters.
	 * 
	 * @param object the type of the object
	 * @param target the name of the object
	 * @param proxyConnection the connection for command execution
	 */
	public ProxyInvocationHandler(String object, String target, ProxyConnection proxyConnection) {
		
		super();
		this.proxyConnection = proxyConnection;
		this.object = object;
		this.target = target;
		
	}
	
	/**
	 * This method intercepts all method calls to a proxy. It redirects the calls to the proxy connection and generates
	 * from his reply the result in a suitable form.
	 */
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		// handle toString method
		if ("toString".equals(method.getName())) {
			return toString();
		}
		
		// handle goodbye method
		if ("goodbye".equals(method.getName())) {
			if (proxyConnection.isConnected()) {
				proxyConnection.disconnect();
			}
			return null;
		}
		
		// handle reboot method
		if ("reboot".equals(method.getName()) && "".equals(target) && args == null) {
			proxyConnection.executeCommand(object, "reboot", new Class[0], null, target, true);
			while (!proxyConnection.connect()) {
				Thread.sleep(100);
			}
			return null;
		}
			
		// get method information
		Class returnType = method.getReturnType();
		Class[] parameterTypes = method.getParameterTypes();
		
		// prepare parameter
		for (int i = 0; i < parameterTypes.length; i++) {
			if (parameterTypes[i].isArray()) {
				Object[] arrayValues = (Object[])args[i];
				if (arrayValues != null) {
					String stringValue = "";
					for (int j = 0; j < arrayValues.length; j++) {
						if (j > 0) stringValue += ", ";
						stringValue += arrayValues[j].toString();
					}
					args[i] = stringValue;
				} else {
					args[i] = null;
				}
				parameterTypes[i] = Collection.class;
			} else {
				if (parameterTypes[i] == int.class) {
					parameterTypes[i] = Integer.class;
				} else if (parameterTypes[i] == boolean.class) {
					parameterTypes[i] = Boolean.class;
				} else if (parameterTypes[i] != Integer.class && parameterTypes[i] != Boolean.class && parameterTypes[i] != Collection.class) {
					parameterTypes[i] = String.class;
				}
			}
		}
		
		// execute
		Reply reply = proxyConnection.executeCommand(object, method.getName(), parameterTypes, args, target, false);
		
		// convert result
		Object result = null;
		if (returnType != Void.TYPE) {
			if (reply == null) {
				if (returnType == int.class) {
					return new Integer(-1);
				} else if (returnType == boolean.class) {
					return new Boolean(false);
				} else {
					return null;
				}
			} else {
				try {
					Object typedReply = reply.getClass().getMethod("get" + object, new Class[0]).invoke(reply, new Object[0]);
					if (typedReply == null) {
						result = reply.getClass().getMethod("getAny", new Class[0]).invoke(reply, new Object[0]);
					} else {
						Object replyObject = typedReply.getClass().getMethod("get" + method.getName().substring(0, 1).toUpperCase() + method.getName().substring(1), new Class[0]).invoke(typedReply, new Object[0]);
						result = replyObject.getClass().getMethod("getReturnValue", new Class[0]).invoke(replyObject, new Object[0]);
						try {
							result = result.getClass().getMethod("getList", new Class[0]).invoke(result, new Object[0]);
						} catch(NoSuchMethodException e) {
						}
						try {
							result = result.getClass().getMethod("getValue", new Class[0]).invoke(result, new Object[0]);
						} catch(NoSuchMethodException e) {
						}
					}
				} catch (NoSuchMethodException e) {
					result = reply.getClass().getMethod("getAny", new Class[0]).invoke(reply, new Object[0]);
				}

				if (returnType == int.class) {
					returnType = Integer.class;
				} else if (returnType == boolean.class) {
					returnType = Boolean.class;
				}
				if (returnType.isArray()) {
					if (!result.getClass().isArray()) {
						if (result instanceof ArrayList) {
							Class type = returnType.getComponentType();
							String[] resultStrings = ((ArrayList<String>)result).toArray(new String[0]);
							int length = resultStrings.length;
							result = Array.newInstance(type, length);
							for (int i = 0; i < length; i++) {
								Array.set(result, i, createObject(type, resultStrings[i]));
							}
						}
					}
					if (result.getClass().getComponentType() != returnType.getComponentType()) {
						int arrayLength = ((Object[])result).length;
						Object resultArray = Array.newInstance(returnType.getComponentType(), arrayLength);
						for (int i = 0; i < arrayLength; i++) {
							Array.set(resultArray, i, returnType.getComponentType().cast(createObject(returnType.getComponentType(), ((String[])result)[i])));
						}
						result = resultArray;
					}
				}else if (returnType == ReadReport.class) {
					if (result instanceof ReadReportType) {
						result = new ReadReport((ReadReportType)result);
					} else {
						result = new ReadReport((String[])result);
					}
				} else {
					if (result.getClass().isArray() && ((Object[])result).length == 1) {
						result = ((Object[])result)[0];
					}
					if (returnType != String.class) {
						if (returnType == Integer.class) {
							result = new Integer((String)result);
						} else if (returnType == Boolean.class) {
							result = new Boolean((String)result);
						} else {
							result = createObject(returnType, (String)result);
						}
					}
				}
			}
		}
		
		return result;
		
	}

	//
	// private methods
	//
	
	/**
	 * This method parses a result set and returns the content in a string array.
	 */
	private String[] getResultSet(String resultString) {
		
		ArrayList results = new ArrayList();
		int start = resultString.indexOf("<value>", 0);
		int end = resultString.indexOf("</value>", start);
		while (start > -1 && end > start) {
			results.add(resultString.substring(start + "<value>".length(), end));
			start = resultString.indexOf("<value>", end);
			end = resultString.indexOf("</value>", start);
		}
		return (String[])results.toArray(new String[0]);
		
	}

	/**
	 * This method creates an object of type returnType with the initialize value initValue.
	 * If the returnType is an interface the method createObjectForInterface() is invoked.
	 * 
	 * @param returnType the type of the new object
	 * @param initValue the initialize value of the new object
	 * @return an object of type returnType with initialize value initValue
	 * @throws Exception if the new object could not be created
	 */
	private Object createObject(Class returnType, String initValue) throws Exception {
		
		if (returnType.isInterface()) {
			return createObjectForInterface(returnType, initValue);
		} else {
			Constructor constructor = returnType.getConstructor(new Class[]{String.class});
			return constructor.newInstance(new Object[]{initValue});
		}
		
	}

	/**
	 * This method creates an object which implements the interface interfaceType by calling
	 * the getInterfaceType() method of the InterfaceMethodFactory with the value initValue.
	 * 
	 * @param interfaceType the type of the new object
	 * @param initValue the initialize value
	 * @return a new object which implements the interface interfaceType
	 * @throws RPProxyException if the new object could not be created
	 */
	private Object createObjectForInterface(Class interfaceType, String initValue) throws RPProxyException {
		
		String proxyFactoryPackageName = interfaceType.getPackage().getName() + ".factories";
		String proxyFactoryName = proxyFactoryPackageName + "." + interfaceType.getSimpleName().substring(0, interfaceType.getSimpleName().length()) + "Factory";
		String methodName = "get" + interfaceType.getSimpleName().substring(0, interfaceType.getSimpleName().length());
		Class proxyFactory;
		try {
			proxyFactory = Class.forName(proxyFactoryName);
		} catch (ClassNotFoundException e) {
			throw new RPProxyException("Factory '" + proxyFactoryName + "' to create a " + interfaceType.getSimpleName() + "-proxy not found.");
		}
		Method getProxyMethod = null;
		try {
			getProxyMethod = proxyFactory.getDeclaredMethod(methodName, new Class[]{String.class, ProxyConnection.class});
		} catch (NoSuchMethodException e) {
			throw new RPProxyException("Method '" + methodName + "' in factory '" + proxyFactoryName + "' not found.");
		} catch (SecurityException e) {
			throw new RPProxyException("Security problem with method '" + methodName + "' in factory '" + proxyFactoryName + "'.");
		}
		try {
			return getProxyMethod.invoke(proxyFactory, new Object[]{initValue, proxyConnection});
		} catch (Exception e) {
			throw new RPProxyException("Create new " + interfaceType.getName() + "-proxy failed.");
		}
		
	}
	
	/**
	 * This method returns the name of the object.
	 */
	public String toString() {
		
		return target;
		
	}
	
}