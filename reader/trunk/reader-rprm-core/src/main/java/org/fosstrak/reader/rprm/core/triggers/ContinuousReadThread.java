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

import org.fosstrak.reader.rprm.core.DataSelector;
import org.fosstrak.reader.rprm.core.FieldName;
import org.fosstrak.reader.rprm.core.Source;
import org.fosstrak.reader.rprm.core.Trigger;

/**
 * This is the implementation of the thread uses by a continuous read trigger.
 * @author Markus Vitalini
 */
public class ContinuousReadThread extends Thread {

   /**
    * The source this ContinuousThread belongs to.
    */
   private Source source;

   /**
    * The trigger that caused the ContinuousThread.
    */
   private Trigger trigger;

   /**
    * The DataSelector for the report.
    */
   private DataSelector dataSelector;

   /**
    * The constructor of ContinuousThread.
    * @param source
    *           The source this ContinuousThread belongs to
    * @param trigger
    *           The trigger that caused the ContinuousThread
    */
   public ContinuousReadThread(final Source source, final Trigger trigger) {

      this.source = source;
      this.trigger = trigger;

      // new DataSelector
      String name = "continuousDataSelecotorOf" + source.getName();
      dataSelector = new DataSelector(name, source.getReaderDevice());

      // set field names
      String[] allFieldNames = new String[] {FieldName.ALL};
      dataSelector.addFieldNames(allFieldNames);

   }

   /**
    * The run method of the thread. It starts the ContinuousReadThread
    */
   public final void run() {

      while (true) {

         source.readWithEventGeneration(dataSelector, trigger);

      }

   }

}
