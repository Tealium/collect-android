---
layout: post
title: Trackable Actions
subtitle: figure out what to track
---

* [Screen Views](trackable-actions.html#screen-views)
* [Button Clicks](trackable-actions.html#button-clicks)
* [Form Entry](trackable-actions.html#form-entry)

<hr/>

<!--more--> 

### <span id="screen-views"/> Screen Views

Screen Views are some of the simplest and useful data to collect. Assuming that we&apos;re using a *Screen View* is an ```Activity```, we can track this behavior with the following: 

```java
/* MainActivity.java*/

@Override
    protected void onResume() {
        super.onResume();
		
		Map<String, String> data = new HashMap<String, String>(2);
		data.put("screen_title", "main");
		data.put("view_action", "resume");
		
		AudienceStream.sendView(data);
    }

    @Override
    protected void onPause() {
		
		Map<String, String> data = new HashMap<String, String>(2);
		data.put("screen_title", "main");
		data.put("view_action", "pause");
		
		AudienceStream.sendView(data);
		
        super.onPause();
    }
```

Not only will this setup allow for view counts, but it also equips us to track how much time is spent on every screen. 

### <span id="button-clicks"/> Button Clicks

Button Clicks (and other UI interactions) too, are simple and useful data to collect; and can be done with the following:  

```java
@Override
public void onClick(View v) {
	
	Map<String, String> data = new HashMap<String, String>(2);
	data.put("event_name", "login");
	data.put("event_action", "click");
	
	AudienceStream.sendEvent(data);
	
	// Perform login...
}
```

### <span id="form-entry"/> Form Entry

It is easy to overlook user input when establishing visitor behavior. Leveraging Android&apos;s ```OnFocusChangeListener```, we can track this data:  

```java
@Override
public void onFocusChange(View view, boolean hasFocus) {
	
	Map<String, String> data = new HashMap<String, String(2);
	data.put("field_name", "comment");
	data.put("field_action", hasFocus ? "focus" : "blur");
	
	AudienceStream.sendEvent(data);
}
```

### Don&apos;t forget some other user behaviors: 

* Geofencing
* Beacons
* Scores
* Spending 

Every application offers something to it&apos;s users, so don&apos;t forget events unique to your app!