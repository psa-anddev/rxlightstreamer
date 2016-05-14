# RxLightstreamer
A library to work using LightStreamer with RxAndroid.
##Introduction
RxLightStreamer is a library that allows developers to easy integrate [LightStreamer](https://www.lightstreamer.com/) with RxAndroid.

## How to use it
### Using the unified API
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
      super(SubscriptionType.MERGE, "your_adapter_name", 
            new String[]{"intField"}, new String[]{"int1", "int2"});
  }
  
  @Override
  public Observable<SubscriptionEvent<Integer>> getSubscriptionObservable() {
      return mRawObservable.map(
        i -> new SubscriptionEvent<>(i.isSubscribed(), Integer.toString(i.getValue("intField"))
      ); //mRawObservable is the one RxSubscription genrates.
  }
}
```

Finally, the subscription can be subscribed and events can be received.
```java
ExampleSubscription example = new ExampleSubscription();
rxLightStreamerClient.subscribe(example);
example.getSubscriptionObservable().subscribe(
    s -> Log.d("Integer received", "The new integer is " + s.getUpdatedItem())
);
```
### Using the non unified API
The client is connected this way
```java
RxNonUnifiedLSClient lsClient = new RxNonUnifiedLSClient();
lsClient.connect("myhost", "adapter set", "user", "password);
```

The adapters are created by extending RxNonUnifiedSubscription
```java
public class MySubscription extends RxNonUnifiedSubscription<String>
{
    @Overrides
    public Observable<SubscriptionEvent<String>> getSubscriptionObservable()
    {
        return mRawObservable.map(i -> i.getItemValue().getNewValue("Fields"));
    }
    
    public MySubscription()
    {
        super(SubscriptionType.MERGE, "adapter", new String[]{"Fields"}, new String[]{"items"}, true);
    }
}
```
The rest is pretty much the same as in the unified API.

## Adding the dependencies
```gradle
compile 'com.psa:rxlightstreamer:0.1.3'
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