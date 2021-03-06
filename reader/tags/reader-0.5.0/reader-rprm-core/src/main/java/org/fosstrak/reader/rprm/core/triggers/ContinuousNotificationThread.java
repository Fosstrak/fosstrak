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

package org.fosstrak.reader.rprm.core.triggers;

import org.fosstrak.reader.rprm.core.NotificationChannel;
import org.fosstrak.reader.rprm.core.ReaderProtocolException;
import org.fosstrak.reader.rprm.core.Trigger;
import org.fosstrak.reader.rprm.core.msg.ReadReportNotificationListener;
import org.fosstrak.reader.rprm.core.readreport.ReadReport;

/**
 * This is the implementation of the thread used by a continuous notification
 * trigger.
 * @author Markus Vitalini
 */
public class ContinuousNotificationThread extends Thread {

   /**
    * The notificationChannel this ContinuousNotificationThread belongs to.
    */
   private NotificationChannel notificationChannel;

   /**
    * The trigger that caused the ContinuousNotificationThread.
    */
   private Trigger trigger;

   /**
    * The constructor for ContinuousNotificationThread.
    * @param notificationChannel
    *           The notification channel
    * @param trigger
    *           The trigger that caused this thread
    */
   public ContinuousNotificationThread(
         final NotificationChannel notificationChannel, final Trigger trigger) {

      this.notificationChannel = notificationChannel;
      this.trigger = trigger;

   }

   /**
    * The run method of the thread. It starts the ContinuousNotificationThread
    */
   public final void run() {

      while (true) {

         try {
            ReadReport report = notificationChannel.readQueuedData(true,
                  trigger);
            if (report.getAllTags().size() > 0) {
               ReadReportNotificationListener.getInstance().notifyHost(
                     report, notificationChannel.getName(),
                     notificationChannel.getDataSelector());
            }
            // ReaderTester.printReadReport(nc);
         } catch (ReaderProtocolException e1) {
            System.out.println(e1.getErrorName());
         }

      }

   }

}
