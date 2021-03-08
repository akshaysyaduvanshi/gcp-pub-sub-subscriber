import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.ReceivedMessage;
import com.google.pubsub.v1.StreamingPullRequest;
import java.time.Duration;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.http.concurrent.Cancellable;



public class Main {
  public static void main(String... args) throws Exception {
    String projectId = ""; // replace with proj id
    String subscriptionId = ""; // replace with asset feed id

    subscribeAsyncExample(projectId, subscriptionId);
  }

  public static void subscribeStreamPullExample(String projectId, String subscriptionId) {

    final StreamingPullRequest request =
        StreamingPullRequest.newBuilder()
            .setSubscription("projects/" + projectId + "/subscriptions/" + subscriptionId)
            .setStreamAckDeadlineSeconds(10)
            .build();

    final Duration pollInterval = Duration.ofSeconds(1);

//    request

//    GooglePubSub.subscribe(request, pollInterval);

  }

  public static void subscribeAsyncExample(String projectId, String subscriptionId) {
    ProjectSubscriptionName subscriptionName =
        ProjectSubscriptionName.of(projectId, subscriptionId);
    Set<String> messageIds = Collections.synchronizedSet(new HashSet<>());

    // Instantiate an asynchronous message receiver.
    MessageReceiver receiver1 =
        (PubsubMessage message, AckReplyConsumer consumer) -> {
          System.out.println("receiver1 Id: " + message.getMessageId());
          System.out.println("Data: " + message.getData().toStringUtf8());
          consumer.ack();
        };

//    try {
    Subscriber subscriber1 = Subscriber.newBuilder(subscriptionName, receiver1).build();
    subscriber1.startAsync().awaitRunning();
    System.out.printf("Listening for messages on %s:\n", subscriptionName.toString());
    subscriber1.awaitTerminated();
//    } catch (TimeoutException timeoutException) {
//      // Shut down the subscriber after 30s. Stop receiving messages.
//      subscriber.stopAsync();
//    }
  }
}
