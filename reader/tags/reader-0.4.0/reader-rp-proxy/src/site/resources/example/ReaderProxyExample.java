import org.accada.reader.rprm.core.EventType;
import org.accada.reader.rprm.core.FieldName;
import org.accada.reader.rp.proxy.DataSelector;
import org.accada.reader.rp.proxy.NotificationChannel;
import org.accada.reader.rp.proxy.ReaderDevice;
import org.accada.reader.rp.proxy.Source;
import org.accada.reader.rp.proxy.Trigger;
import org.accada.reader.rp.proxy.factories.DataSelectorFactory;
import org.accada.reader.rp.proxy.factories.NotificationChannelFactory;
import org.accada.reader.rp.proxy.factories.ReaderDeviceFactory;
import org.accada.reader.rp.proxy.factories.TriggerFactory;
import org.accada.reader.rp.proxy.msg.Handshake;

public class ReaderProxyExample {

   private static int MESSAGE_FORMAT = Handshake.FORMAT_XML;
   private static int TRANSPORT_PROTOCOL = Handshake.HTTP;

   private static final String COMMAND_CHANNEL_HOST = "localhost";
   private static final int COMMAND_CHANNEL_PORT = 8000;

   private static final String READ_TRIGGER_NAME = "ReadTrigger";
   private static final String READ_TRIGGER_TYPE = Trigger.TIMER;
   private static final String READ_TRIGGER_VALUE = "ms=2000";

   private static final String NOTIFICATION_TRIGGER_NAME = "NotificationTrigger";
   private static final String NOTIFICATION_TRIGGER_TYPE = Trigger.CONTINUOUS;
   private static final String NOTIFICATION_TRIGGER_VALUE = "";

   private static final String DATA_SELECTOR_NAME = "DataSelector";
   private static final String[] EVENT_FILTERS = new String[] {EventType.EV_OBSERVED,
         EventType.EV_LOST};
   private static final String[] FIELD_NAMES = new String[] {FieldName.EVENT_TYPE,
         FieldName.READER_NAME, FieldName.TAG_ID, FieldName.TAG_ID_AS_PURE_URI,
         FieldName.TAG_ID_AS_TAG_URI, FieldName.SOURCE_NAME};

   private static final String NOTIFICATION_CHANNEL_NAME = "NotificationChannel";
   private static final String NOTIFICATION_CHANNEL_HOST = "localhost";
   private static final int NOTIFICATION_CHANNEL_PORT = 9000;
   private static final String NOTIFICATION_CHANNEL_MODE = "connect";
   private static final String NOTIFICATION_CHANNEL_ADDRESS = "tcp://"
         + NOTIFICATION_CHANNEL_HOST + ":" + NOTIFICATION_CHANNEL_PORT + "?mode="
         + NOTIFICATION_CHANNEL_MODE;


   public static void main(String[] args) throws Exception {
      // create handshake
      Handshake handshake = new Handshake();
      handshake.setMessageFormat(MESSAGE_FORMAT);
      handshake.setTransportProtocol(TRANSPORT_PROTOCOL);

      // get reader device proxy
      System.out.println("Get reader device proxy.");
      ReaderDevice readerDevice = ReaderDeviceFactory.getReaderDevice(COMMAND_CHANNEL_HOST,
            COMMAND_CHANNEL_PORT, handshake);

      // get current source proxy
      System.out.println("Get current source.");
      Source source = readerDevice.getCurrentSource();
      System.out.println("Name of current source is: " + source.getName());

      // create read trigger
      System.out.println("Create read trigger.");
      Trigger readTrigger = TriggerFactory.createTrigger(READ_TRIGGER_NAME,
            READ_TRIGGER_TYPE, READ_TRIGGER_VALUE, readerDevice);

      // create notification trigger
      System.out.println("Create notification trigger.");
      Trigger notificationTrigger = TriggerFactory.createTrigger(NOTIFICATION_TRIGGER_NAME,
            NOTIFICATION_TRIGGER_TYPE, NOTIFICATION_TRIGGER_VALUE, readerDevice);

      // data selector
      System.out.println("Create data selector.");
      DataSelector dataSelector = DataSelectorFactory.createDataSelector(DATA_SELECTOR_NAME,
            readerDevice);
      System.out.println("Add event filters to data selector.");
      dataSelector.addEventFilters(EVENT_FILTERS);
      System.out.println("Add field names to data selector.");
      dataSelector.addFieldNames(FIELD_NAMES);
      //System.out.println("Add tag fields to data selector.");
      //dataSelector.addTagFieldNames(TAG_FIELDS);

      // create notification channel
      System.out.println("Create notification channel.");
      NotificationChannel notificationChannel = NotificationChannelFactory.createNotificationChannel(
            NOTIFICATION_CHANNEL_NAME, NOTIFICATION_CHANNEL_ADDRESS, readerDevice);
      System.out.println("Set data selector of notification channel.");
      notificationChannel.setDataSelector(dataSelector);
      System.out.println("Add notification trigger to notification channel.");
      notificationChannel.addNotificationTriggers(new Trigger[] {notificationTrigger});

      // add current source to notification channel
      System.out.println("Add source to notification channel.");
      notificationChannel.addSources(new Source[] {source});

      // add read trigger to source
      System.out.println("Add read trigger to source.");
      source.addReadTriggers(new Trigger[] {readTrigger});
   }
}