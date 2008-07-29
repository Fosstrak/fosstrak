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

import java.util.TimerTask;

import org.fosstrak.reader.rprm.core.DataSelector;
import org.fosstrak.reader.rprm.core.FieldName;
import org.fosstrak.reader.rprm.core.Source;
import org.fosstrak.reader.rprm.core.Trigger;

/**
 * This class is the implementation of the thread userd by the read timer
 * trigger.
 * @author Markus Vitalini
 */
public class TimerReadThread extends TimerTask {

   /**
    * The source this TimerThread belongs to.
    */
   private Source source;

   /**
    * The trigger that caused the TimerThread.
    */
   private Trigger trigger;

   /**
    * The DataSelector for the report.
    */
   private DataSelector dataSelector;

   /**
    * The constructor of TimerThread.
    * @param source
    *           The source this TimerThread belongs to
    * @param trigger
    *           The trigger that caused the TimerThread
    */
   public TimerReadThread(final Source source, final Trigger trigger) {

      this.source = source;
      this.trigger = trigger;

      // new DataSelector
      String name = "timerDataSelecotorOf" + source.getName();
      dataSelector = new DataSelector(name, source.getReaderDevice());

      // set field names
      String[] allFieldNames = new String[] {FieldName.ALL};
      dataSelector.addFieldNames(allFieldNames);

   }

   /**
    * The run method of the thread. It starts the TimerReadThread
    */
   public final void run() {

      source.readWithEventGeneration(dataSelector, trigger);

   }

}
