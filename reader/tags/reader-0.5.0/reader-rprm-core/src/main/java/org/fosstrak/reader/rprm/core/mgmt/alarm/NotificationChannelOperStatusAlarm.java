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

package org.fosstrak.reader.rprm.core.mgmt.alarm;

import org.fosstrak.reader.rprm.core.NotificationChannel;
import org.fosstrak.reader.rprm.core.ReaderDevice;
import org.fosstrak.reader.rprm.core.mgmt.OperationalStatus;

/**
 * <code>NotificationChannelOperStatusAlarm</code> extends the
 * <code>TTOperStatusAlarm</code> class. Its receipt signals a change in the
 * operational status of one of a Reader's notification channels. The abstract
 * model's <code>NotificationChannel.operStatusAlarmControl</code> data
 * element controls the triggering of alarms of this type.
 */
public class NotificationChannelOperStatusAlarm extends TTOperStatusAlarm {

	/**
	 * The name of the notification channel that experienced the
	 * alarm-triggering state transition, i.e., the value of the respective
	 * <code>NotificationChannel.name</code> model element.
	 */
	private String notificationChannelName;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            The name of the alarm identifying the type of alarm, e.g.,
	 *            "NotificationChannelOperStatusAlarm"
	 * @param alarmLevel
	 *            The severity level of the alarm
	 * @param readerDevice
	 *            The reader device
	 * @param fromState
	 *            The originating <code>OperationalStatus</code> of the
	 *            notification channel before the <code>Alarm</code> is
	 *            generated
	 * @param toState
	 *            The <code>OperationalStatus</code> at the time the
	 *            <code>Alarm</code> is generated
	 * @param notificationChannelName
	 *            The name of the notification channel
	 */
	public NotificationChannelOperStatusAlarm(String name,
			AlarmLevel alarmLevel, ReaderDevice readerDevice,
			OperationalStatus fromState, OperationalStatus toState,
			String notificationChannelName) {
		super(name, alarmLevel, readerDevice, fromState, toState);
		this.notificationChannelName = notificationChannelName;
	}

	/**
	 * Returns the name of the notification channel that experienced the alarm-triggering
	 * state transition, i.e., the value of the respective
	 * <code>NotificationChannel.name</code> model element.
	 * 
	 * @return The name of the <code>NotificationChannel</code>
	 */
	public String getNotificationChannelName() {
		return notificationChannelName;
	}

}
