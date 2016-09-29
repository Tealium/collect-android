---
layout: post
title: Getting Started
subtitle: A quick start guide covering all of the basics, including cloning a library, adding code to a project, linking frameworks, adding linker flags, and importing headers.
---
<!--more-->
 
<div class="sidebar">
    <div class="context_container pageNavigation_wrapper">
        <span class="context_title">{{ page.title }}</span>
        <ul class="pageNavigation">
            <li><a href="getting-started.html#requirements">Requirements</a></li>
            <li><a href="getting-started.html#download">Download</a></li>
            <li><a href="getting-started.html#android-manifest">Update AndroidManifest.xml</a></li>
            <li><a href="getting-started.html#enable">Enable</a></li>
            <li><a href="getting-started.html#send-events">Send Events</a></li>
            <li><a href="getting-started.html#visitor-profile">Leveraging the Visitor Profile</a></li>
        </ul>
    </div>
    {% include post-sidebar.html %}
</div>

## <span id="requirements"/>Requirements

* Min API 9+ (Gingerbread)
* Permissions
 * ```android.permission.INTERNET```
 * ```android.permission.ACCESS_NETWORK_STATE```  
 
## <span id="download"/>Download

> TODO: link

Available in 2 options: 

* Source (As an Android Studio project)
* tealium-AS_1.0.jar 

## <span id="android-manifest"/> Update AndroidManifest.xml

Add the *Tealium Collect* library required permissions to the ```AndroidManifest.xml```:

* ```android.permission.INTERNET```
* ```android.permission.ACCESS_NETWORK_STATE```

## <span id="enable"/>Enable

From an ```Application``` or ```Activity``` subclass, initialize the ```AudienceStream``` library with the [enable]({{ site.baseurl }}/javadoc/com/tealium/collect/TealiumCollect.html#enable(com.tealium.collect.TealiumCollect.Config)) command: 

```java
import com.tealium.collect.TealiumCollect;

/* ... */

TealiumCollect.enable(new TealiumCollect.Config(this, 
    "tealiummobile", 
    "demo", 
    "dev"));
```

This method only needs to be called a single time while the application is running, so an ```Application``` subclass' ```onCreate``` method is the ideal location to make this method call.

## <span id="send-events"/>Send Events

After determining what visitor behaviors should be tracked, utilize the [sendEvent]({{ site.baseurl }}/javadoc/com/tealium/collect/TealiumCollect.html#sendEvent(java.util.Map)) and [sendView]({{ site.baseurl }}/javadoc/com/tealium/collect/TealiumCollect.html#sendView(java.util.Map)) methods to send that data to AudienceStream: 

```java
Map<String, String> data = new HashMap<String, String>(1);
data.put("action", "logout");

TealiumCollect.sendEvent(data);
```

```java
Map<String, String> data = new HashMap<String, String>(1);
data.put("screen_title", "checkout");

TealiumCollect.sendView(data);
```

For some ideas on what actions to track, please see the [Trackable Actions]({{ site.baseurl}}/trackable-actions.html) guide.

## <span id="visitor-profile"/>Leveraging the Visitor Profile

The *Tealium Collect* library offers a variety of means to identifiy visitor behavior and offer a personalized app experience to them.  

Add event listeners to ```TealiumCollect``` through the [getEventListeners]({{ site.baseurl}}/javadoc/com/tealium/collect/TealiumCollect.html#getEventListeners()) property to listen for visitor updates: 

```java
AudienceStream.getEventListeners().add(new AudienceStream.OnBadgeUpdateListener() {
    @Override
    public void onBadgeUpdate(BadgeAttribute oldBadge, BadgeAttribute newBadge) {
        if(newBadge != null && "30".equals(newBadge.getId())) {
            // Visitor has earned a "Fan" Badge!
        }
    }   
});
```   
 
Visitor Attributes are defined in *AudienceStream* through [TealiumIQ](https://my.tealiumiq.com). The available attributes are:  
 
* Audience
 * Groups of Visitor Attributes.
* Badge
 * Badges are ways of identifying segments of new users.
* Date
 * Stores a date value.
* Flag
 * Stores boolean values.
* Metric
 * Stores numerical data.
* Trait
 * Stores string. 

To look up what attributes are currently defined for your *AudienceStream* account-profile: 

```
GET http(s)://visitor-service.tealiumiq.com/datacloudprofiledefinitions/{account}/{profile}/{visitor_id}
``` 

A visitor id is created per-install by the *Tealium Collect* library, and can be found through LogCat logs or the [getVisitorId]({{ site.baseurl}}/javadoc/com/tealium/collect/TealiumCollect.html#getVisitorId()) method.

###### Updated by chadhartman
