# rxlightstreamer
A library to work using LightStreamer with RxAndroid.
##Introduction
RxLightStreamer is a library that allows developers to easy integrate [LightStreamer](https://www.lightstreamer.com/) with RxAndroid.

##How to use it
Once you have added the dependency to your gradle, you can start by connecting a LightStreamer client.
```java
RxLightStreamerClient rxLightStreamerClient = new RxLightStreamerClient();
rxLightStreamerClient.connect("http://your_ls_url", "your_adapter_set");
```
The client (RxLightStreamerClient) provides an observer that allows the notification of connection events (ClientStatus).
```java
rxLightStreamerClient.getClientStatusObservable().subscribe(
  status -> Log.d("Status update", status.getLightStreamerStatus()); //This method would print the status just as returned by LS
  Throwable:printStackTrace //Error events are turned into exceptions.
)
```

The library includes a subscription class (RxSubscription) which is meant to be extended to create the desired object.
```java
public class ExampleSubscription extends RxSubscription<Integer>
{
  public ExampleSubscription()
  {
      super(SubscriptionType.MERGE, "your_adapter_name", new String[]{"intField"}, new String[]{"int1", "int2"});
  }
  
  @Override
  public Observable<SubscriptionEvent<Integer>> getSubscriptionObservable() {
      return mRawObservable.map(i -> Integer.toString(i.getValue("intField")); //mRawObservable is the one RxSubscription genrates.
  }
}
```

Finally, the subscription can be subscribed and events can be received.
```java
ExampleSubscription example = new ExampleSubscription();
rxLightStreamerClient.subscribe(example);
example.getSubscriptionObservable().subscribe(s -> Log.d("Integer received", "The new integer is " + s.getUpdatedItem());
```
##Adding the dependencies
```gradle
compile 'com.psa:rxlightstreamer:0.1'
```
You also need to add the Lightstreamer repository in order to satisfy the internal dependencies with the
official LightStreamer API.
```gradle
repositories {
    jcenter()
    maven {
        url "http://www.lightstreamer.com/repo/maven"
    }
}
```