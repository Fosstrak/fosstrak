import org.accada.reader.rp.proxy.NotificationChannelListener;
import org.accada.reader.rprm.core.msg.notification.Notification;


public class ReaderNotificationChannelListener implements NotificationChannelListener {

	public void dataReceived(Notification notification) {
		System.out.println("Notification received: " + notification);

		// add your code to process notification message here
	}

}
