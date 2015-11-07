#Description
this is a very simple view that extands Imageview.
its allow to add tip at corner, its like video app or picture browser app

#Effect
!["effect"](effect2.jpg)

#Useage

## the first way   
   clone this project to your disk by command line 
	
## the second way

## code sample
```java
	    <com.xuefengyang.tipimageview.TipImageView
        android:layout_width="170dp"
        android:layout_height="140dp"
        android:src="@drawable/t1"
        android:scaleType="fitXY"
        app:tipDirection="left_top"
        app:tipBgColor="#F44336"
        app:tipTextColor="#fff"
        app:tipText="会员"
        />
```
## custom attributes 
  `tipDirection="left_top"` `tipBgColor="#F44336"` `tipTextColor="#fff"`
  `app:tipText="会员"` `tipTextSize="22sp"` 


