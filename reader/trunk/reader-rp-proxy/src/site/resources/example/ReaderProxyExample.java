import org.accada.reader.EventType;
import org.accada.reader.FieldName;
import org.accada.reader.proxy.DataSelector;
import org.accada.reader.proxy.NotificationChannel;
import org.accada.reader.proxy.ReaderDevice;
import org.accada.reader.proxy.Source;
import org.accada.reader.proxy.Trigger;
import org.accada.reader.proxy.factories.DataSelectorFactory;
import org.accada.reader.proxy.factories.NotificationChannelFactory;
import org.accada.reader.proxy.factories.ReaderDeviceFactory;
import org.accada.reader.proxy.factories.TriggerFactory;
import org.accada.reader.proxy.msg.Handshake;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;


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

        private static final String[] EVENT_FILTERS = new String[] {EventType.EV_NEW};
        private static final String[] FIELD_NAMES = new String[] {FieldName.ALL_EVENT};
        private static final String[] TAG_FIELDS = new String[] {"tagId"};
        
        private static final String NOTIFICATION_CHANNEL_NAME = "NotificationChannel";
        private static final String NOTIFICATION_CHANNEL_HOST = "localhost";
        private static final int NOTIFICATION_CHANNEL_PORT = 9000;
        private static final String NOTIFICATION_CHANNEL_MODE = "connect";
        private static final String NOTIFICATION_CHANNEL_ADDRESS = "tcp://"     + NOTIFICATION_CHANNEL_HOST
                        + ":" + NOTIFICATION_CHANNEL_PORT + "?mode=" + NOTIFICATION_CHANNEL_MODE;
        
        private static final Logger LOG = Logger.getLogger(ReaderProxyExample.class);
        
        public static void main(String[] args) throws Exception {
                
                // configurate logger
                BasicConfigurator.configure();
                
                // create handshake
                Handshake handshake = new Handshake();
                handshake.setMessageFormat(MESSAGE_FORMAT);
                handshake.setTransportProtocol(TRANSPORT_PROTOCOL);
                
                // get reader device proxy
                LOG.info("Get reader device proxy.");
                ReaderDevice readerDevice = ReaderDeviceFactory.getReaderDevice(COMMAND_CHANNEL_HOST,
                                COMMAND_CHANNEL_PORT, handshake);

                // get current source proxy
                LOG.info("Get current source.");
                Source source = readerDevice.getCurrentSource();
                LOG.info("Name of current source is: " + source.getName());
                
                // create read trigger
                LOG.info("Create read trigger.");
                Trigger readTrigger = TriggerFactory.createTrigger(READ_TRIGGER_NAME,
                                READ_TRIGGER_TYPE, READ_TRIGGER_VALUE, readerDevice);
                
                // create notification trigger
                LOG.info("Create notification trigger.");
                Trigger notificationTrigger = TriggerFactory.createTrigger(NOTIFICATION_TRIGGER_NAME,
                                NOTIFICATION_TRIGGER_TYPE, NOTIFICATION_TRIGGER_VALUE, readerDevice);
                
                // data selector
                LOG.info("Create data selector.");
                DataSelector dataSelector = DataSelectorFactory.createDataSelector(DATA_SELECTOR_NAME,
                                readerDevice);
                LOG.info("Add event filters to data selector.");
                dataSelector.addEventFilters(EVENT_FILTERS);
                LOG.info("Add field names to data selector.");
                dataSelector.addFieldNames(FIELD_NAMES);
                LOG.info("Add tag fields to data selector.");
                dataSelector.addTagFieldNames(TAG_FIELDS);
                
                // create notification channel
                LOG.info("Create notification channel.");
                NotificationChannel notificationChannel = NotificationChannelFactory.createNotificationChannel(
                                NOTIFICATION_CHANNEL_NAME, NOTIFICATION_CHANNEL_ADDRESS, readerDevice);
                LOG.info("Set data selector of notification channel.");
                notificationChannel.setDataSelector(dataSelector);
                LOG.info("Add notification trigger to notification channel.");
                notificationChannel.addNotificationTriggers(new Trigger[] {notificationTrigger});
                
                
                // add current source to notification channel
                LOG.info("Add source to notification channel.");
                notificationChannel.addSources(new Source[] {source});
                
                // add read trigger to source
                LOG.info("Add read trigger to source.");
                source.addReadTriggers(new Trigger[] {readTrigger});            
                
        }
        
}
