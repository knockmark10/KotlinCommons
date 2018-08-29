# **Welcome to Kotlin Commons!**

Why did I decide to do this library? When you're developing an app, you _copy-paste_ the same code over and over again, with the same mistakes. So I decided to build this library to reduce boilerplate, and that's how this library was born. It's under constant development and you can use it anytime you want.

# **Setup**
[![](https://jitpack.io/v/knockmark10/KotlinCommons.svg)](https://jitpack.io/#knockmark10/KotlinCommons)

This library is available _via_ Jitpack ([here](https://jitpack.io/#org.bitbucket.mchaveza/commons-kotlin))

 1. Add it in your root build.gradle at the end of repositories:

```java
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

 2. Add the dependency:
```java
dependencies {
	implementation 'com.github.knockmark10:KotlinCommons:$version'
}
```

## **What's new in v2.0**

Since older versions of the library (1.5 and above) there were many methods and classes that were marked as *deprecated* due to the implementation of newer Android API's, or the availability to customize features in an easier and more secure ways to do it.

Below you can find all those methods/classes that were removed from v2.0 and above, and what you can do to replace them.

|Class|Method|Replace with|Type|
|:--:|:--:|:--:|:--|
|GPSManager|All|TrackingManager|Class|
|SharedPreferencesManager|getStringPreference|getSharedPreference|Method|
|SharedPreferencesManager|getBooleanPreference|getSharedPreference|Method|
|SharedPreferencesManager|getIntPreference|getSharedPreference|Method|
|SharedPreferencesManager|getFloatPreference|getSharedPreference|Method|
|SharedPreferencesManager|setStringPreference|setSharedPreference|Method|
|SharedPreferencesManager|setBooleanPreference|setSharedPreference|Method|
|SharedPreferencesManager|setIntPreference|setSharedPreference|Method|
|SharedPreferencesManager|setFloatPreference|setSharedPreference|Method|

Beside this, some extensions changed for better customization. 

|Extensions Type|Extension Name|Replace with|
|:--:|:--:|:--:|
|FragmentManager|replaceFragment|performReplacingTransaction|
|FragmentManager|addFragment|performAddingTransaction|

## **What's inside it?**

You have multiple features within this library:

 [1. Shared Preferences](https://github.com/knockmark10/KotlinCommons/wiki#1-shared-preferences)
 
 [2. Runtime Permissions Manager](https://github.com/knockmark10/KotlinCommons/wiki#2-runtime-permissions-manager)
 
 [3. Geocoder Manager](https://github.com/knockmark10/KotlinCommons/wiki#3-geocoder-manager)
 
 [4. Tracking Manager](https://github.com/knockmark10/KotlinCommons/wiki#4-tracking-manager-gps)
 
 [5. Maps Manager](https://github.com/knockmark10/KotlinCommons/wiki#5-maps-manager)
 
 [6. Connectivity Manager](https://github.com/knockmark10/KotlinCommons/wiki#6-connectivity-manager)
 
 [7. Validator Utils](https://github.com/knockmark10/KotlinCommons/wiki#7-validator-utils)
 
 [8. Image Compression](https://github.com/knockmark10/KotlinCommons/wiki#8-image-compression)
 
 [9. Fingerprint Authentication](https://github.com/knockmark10/KotlinCommons/wiki#9-fingerprint-authentication)
 
 [10. Barcode Generation](https://github.com/knockmark10/KotlinCommons/wiki#10-barcode-generation)

 [11. Encryption](https://github.com/knockmark10/KotlinCommons/wiki#11-encryption)
 
 [12. Bonus Stuff](https://github.com/knockmark10/KotlinCommons/wiki#12-bonus-stuff)

## **1. Shared Preferences**

### **Description**

This feature provides a persistent store for simple data. Data is persisted to disk asynchronously. You can save Int, String or Boolean. And you can even clear preferences.

|PUBLIC CONSTRUCTOR|DESCRIPTION|
|:--:|:--:|
|**SharedPreferencesManager**(*context*: Context)|Constructs the class to manage preferences|


**Public Methods**

|Return type|Method name|Parameters|Description|
|:---------:|:---------:|:--------:|:---------:|
|Any*| getSharedPreference| stringPreference, defaultValue |Returns the requested value, defaultValue (if provided) or empty string.
|Void| setSharedPreference| *stringPreference**, defaultValue |Stores the value provided with the key given.
|Void| clearPreferences | *preferenceName**, preference|Deletes the preference given.

**Note:** The return value of the shared preference depends on the default value type provided.

### **Usage**

```kotlin
val preferences = SharedPreferencesManager(context)
preferences.setSharedPreference(PARAM_NAME, value)
preferences.getSharedPreference(PARAM_NAME, defaultValue)
preferences.clearPreferences(PARAM_NAME)
```

## **2. Runtime Permissions Manager**

### **Description**

This feature allows you to check if some permission was already requested, and to request permissions at runtime in a very easy way. It is based on _RxPermission_ library. 

|PUBLIC CONSTRUCTOR|DESCRIPTION|
|:--:|:--:|
|**PermissionManager** (*activity*: Activity)|Constructs the class to check if certain permission was granted or rejected.|
|**PermissionManager** (*activity*: Activity, *listener*: PermissionCallback)|Constructs the class to check and request runtime permissions at will. With the callback supplied you will get the requesting result to handle it as you want|

**Public Methods**

|Return type|Method name|Parameters|Description|
|:---------:|:---------:|:--------:|:---------:|
|Boolean|requestSinglePermission|*permission**|Requests the desired permission and return a boolean value indication whether or not the request was made succesfully or not|
|Boolean|requestMultiplePermissions|vararg *permissions**|Requests the desired permissions and return a boolean value indication whether or not the request was made succesfully or not|
|Boolean|permissionGranted|*permission**|Returns a value indicating if the permission was granted or rejected|
|Boolean|verifyPermissions|*permission**|Checks that all given permissions have been granted by verifying that each entry in the given array is of the value **PackageManager.PERMISION_GRANTED**|

**PermissionCallback**

In order to use this feature, you need to implement this callback wherever you need it.

|Method | Parameters |Description|
|:-----:|:----------:|:---------:|
|onPermissionGranted|permission|If requested permission was granted, you'll be notified within this method|
|onPermissionDenied|permission|In case the permission gets rejected, you can handle the actions with this method|

### **Usage**

```kotlin
/**
* Let's instantiate class manager. It requires to
* implement PermissionCallback in your class for
* it to work properly.
*/
val permissionManager = PermissionManager(activity, this)
permissionManager.requestSinglePermission(Manifest.permission.GET_ACCOUNTS)
permissionManager.permissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)

override fun onPermissionGranted(permission: String) {  
	//Permission was granted. Do something
}

override fun onPermissionDenied(permission: String){
	//Permission was denied. Handle it as you want
}
```

### **Sample**

![enter image description here](https://i.stack.imgur.com/Q7GkS.jpg)

## **3. Geocoder Manager**

### **Description**

This feature is currently under development. For now you can only get the postal code related to your location.

### **Usage**

```kotlin
val geocoder = GeocoderManager(context)
val postalCode = geocoder.getPostalCodeByCoordinates(context, location.latitude, location.longitude, errorString)

```

## **4. Tracking Manager (GPS)**

### **Description**

This feature allows you to detect user's location with a very easy setup.

|PUBLIC CONSTRUCTOR|DESCRIPTION|
|:--:|:--:|
|**TrackingManager** (*context*: Context)|Constructs the class necessary to handle location services. You can request those location updates, and to stop them when needed|

**Public Methods**

|Return type|Method name|Parameters|Description|
|:---------:|:---------:|:--------:|:---------:|
|Void|startLocationUpdates|*listener**, updateInterval, fastestInterval, useLooper|Starts receiving updates from the gps|
|Void|stopLocationUpdates|-|Stops receiving updates from gps and turn off location provider|
|Location|getLastLocation|-|Gets user's last known location|
|Double|getLatitude|-|Gets user's last known latitude|
|Double|getLongitude|-|Gets user's last known latitude|
|Boolean|areLocationServicesEnabled|-|Returns a boolean value indicating if location services are enabled, up and working, and if required permissions were given.|

**TrackingManagerLocationCallback**

In order to use this feature, you need to implement this callback wherever you need it.

|Method | Parameters |Description|
|:-----:|:----------:|:---------:|
|onLocationHasChanged|location|Inside this method, you get location updates|
|onLocationHasChangedError|error|If any exception is raised while trying to get location, you can handle the error with this method.|

### **Usage**

```kotlin
//Your class needs to implement LocationHasChangedCallback
class MyClass : LocationHasChangedCallback{
	...
	val trackingManager = TrackingManager(context)
	// Start receiving location updates
	trackingManager.startLocationUpdates(this)
	//let's check if location services are running
	val canGetLocation = trackingManager.areLocationServicesEnabled()
	...

	//In here we get your last location
	override fun onLocationHasChanged(location: Location){
	}
	
	//Let's handle exceptions related to location detection
	override fun onLocationHasChangedError(error: Exception){
	}

	//Don't forget to stop updates when activity's
	//on the background
	override fun onPause(){
		super.onPause()
		trackingManager.stopLocationUpdates()
	}
}
```

## **5. Maps Manager**

### **Description**

This library contains many extensions and utilities to use Google Maps. You can style your map very easily, or set a custom bitmap marker with barely 1 or 2 lines of code.

|PUBLIC CONSTRUCTOR|DESCRIPTION|
|:--:|:--:|
|**MapsManager** (*context*: Context)|Constructs the class that allows you to change the marker bitmap with the desired one|

**Public Methods**

|Return type|Method name|Parameters|Description|
|:---------:|:---------:|:--------:|:---------:|
|Bitmap|getMarkerBitmapFromView|*resourceId**|Draws a map marker with provided drawable|

**GoogleMap's extensions**

|Method name|Parameters|Description|
|:---------:|:--------:|:---------:|
|setMapStyle|*context**, *style**|Sets desired map style with the enum class given|
|setDaylightStyle|*context**|Sets the map style according to the daylight, with a fixed style for every daytime|
|setCurrentPosition|*position**, *position**|Sets the camera to desired position with custom zoom provided|
|centerCamera|*listMarker**, *zoom**|Centers the camera with a given marker list|
|centerMarkers|*position**, *position**|Centers the camera with a given LatLng list|

### **Usage**

```kotlin
val mapsManager = MapsManager(context)
//Setting the map in current position with desired zoom
googleMap.setCurrentPosition(latLng, zoom)
//Set Aubergine style to map (includes many others)
googleMap.setMapStyle(context, MapStyles.Aubergine)
//Setting style according to the daylight
googleMap.setDaylightStyle(context)
//Setting marker with desired icon
googleMap.addMarker(new MarkerOptions()  
 .position(position)  
 .icon(BitmapDescriptorFactory.fromBitmap(mapsManager.getMarkerBitmapFromView(R.drawable.ic_custom_marker))))
 //Centering camera with a marker list provided
googleMap.centerMarkers(context, markersList)
```

## **6. Connectivity Manager**

### **Description**

You can check whether user's device is connected to the Internet or not.

|PUBLIC CONSTRUCTOR|DESCRIPTION|
|:--:|:--:|
|**ConnectivityManager** (*context*: Context)|Constructs the class that allows you to check for internet connection|

**Public Methods**

|Return type|Method name|Parameters|Description|
|:---------:|:---------:|:--------:|:---------:|
|NetworkInfo|getNetworkInfo|-|Gets the network info|
|Boolean|isConnected|-|Checks if there is any connectivity|
|Boolean|isConnectedWifi|-|Check if there is any connectivity to a Wifi network|
|Boolean|isConnectedMobile|-|Checks if there is any connectivity to a mobile network|
|Boolean|isConnectedFast|-|Check if there is fast connectivity|
|Boolean|isConnectionFast|-|Check if the connection is fast taking into account the kind of network the device is connected on|

### **Usage**

```kotlin
val connection = ConnectivityManager(context)
connection.isConnected()
connection.isConnectedWifi()
connection.isConnectedMobile()
```

## **7. Validator Utils**

### **Description**

Utility to validate fields in a very easy and fun way. Every method inside this class, besides telling you if the given content fullfill  the requirements, it also displays the error (if present).

**Public Methods**

|Return type|Method name|Parameters|Description|
|:---------:|:---------:|:--------:|:---------:|
|Boolean|isEmailValid|container, field, wrongFormat, emptyField|Checks if field is not empty and if the field has an email valid format|
|Boolean|isEmptyField|container, field, emptyField|Checks if field is or not empty|
|Boolean|isFieldLongEnough|container, field, fieldMessage|Checks if field's content is long enough|
|Boolean|isCheckboxChecked|checkbox|Checks if checkbox provided is checked|
|Boolean|areFieldsTheSame|checkbox|Checks if checkbox provided is checked|
|Boolean|isValidRFC|container, field, wrongFormatMessage|Checks if rfc provided is valid. It also checks for the verifying digit|
|Boolean|patternMatches|container, field, pattern, errorMessage|Check if the provided content matches the pattern given

### **Usage**

```kotlin
val isValid : Boolean = ValidatorUtils.isEmailValid(textInputLayout, editText, wrongFormatString, emptyFieldString)
val isEmptyField = ValidatorUtils.isEmptyField(textInputLayout, editText, emptyFieldString)
val isFieldLongEnough= ValidatorUtils.isFieldLongEnough(textInputLayout, editText, length, fieldMessageString)
```

## **8. Image Compression**

### **Description**

This tool helps you when you want to send an image to the server. In most cases, you need to resize it before sending it. That's what this tool is here for. 

|PUBLIC CONSTRUCTOR|DESCRIPTION|
|:--:|:--:|
|**ImageCompression** (*context*: Context, *listener*: ImageCompressionCallback)|Constructs the class that allows you to compress the imagen not only in size, but also in quality|

**Public Methods**

|Return type|Method name|Parameters|Description|
|:---------:|:---------:|:--------:|:---------:|
|Void|compressImage|uri|Compresses the image given as uri.|

**ImageCompressionCallback**

In order to use this feature, you need to implement this callback wherever you need it.

|Method | Parameters |Description|
|:-----:|:----------:|:---------:|
|onImageCompressed|image, fileSize, base64Image|When the compress process is done, this method is called.|
|onImageCompressedError|error|If some exception is raised, this method is called|

### **Usage**
```kotlin
class MyClass: ImageCompression.ImageCompressionCallback{
	
	private val imageCompression = ImageCompression(context, this)
	
	imageCompression.compressImage(uri) 

	override fun onImageCompressed(image: Bitmap, fileSize: String, base64Image: String) {
		//Here we have our image compressed
	}
	override fun onImageCompressedError(error: Throwable) {
		//We can handle errors in here
	}
}
```

## **9. Fingerprint Authentication**

### **Description**

The process to authenticate users with fingerprint detection might lead us to bang our heads with the wall. It's a long process to make it work and takes a lot of validations. That's why it was included in here. 

This library provides all the validations required, and the authentication process fully automated. All you need to do is create an instance of *FingerPrintUtils* class and call different methods (depending on your needs). 

**Importante note:** This feature requires specific callbacks to work properly.

|PUBLIC CONSTRUCTOR|DESCRIPTION|
|:--:|:--:|
|**FingerPrintUtils** (*activity*: Activity)|Constructs the class that handles the fingerprint authentication process. Without any interface supplie, this class is useless|
|**FingerPrintUtils** (*activity*: Activity, *basicListener*: FingerPrintBasicCallback)|Constructs the class that handles the starting fingerprint validations. These validations are performed by the class itself and notifies the result via *basicListener* interface|
|**FingerPrintUtils** (*activity*: Activity, *authListener*: FingerPrintAuthCallback)|Constructs the class that handles the authentication process **WITHOUT** the previous validations. The result of the process will be notified through *FingerPrintAuthCallback* interface|
|**FingerPrintUtils** (*activity*: Activity, *basicListener*: FingerPrintBasicCallback, *authListener*: FingerPrintAuthCallback)|Constructs the class that handles all the authentication process. When both interfaces are supplied, this class will handle the starting validations, such as the authenticacion process itself. The result will be emitted and the interfaces will receive it|

**Public Methods**

Please note that all of the method listed below return an instance of the dialog, so all the calls can be chained to set it up and fully customize it.

|Return type|Method name|Parameters|Description|
|:---------:|:---------:|:--------:|:---------:|
|Void|validateFingerPrint|-|Validates if your device supports fingertip detection|
|Void|startAuthProcess|-|Starts the authentication process|
|Void|stopAuth|-|Stops the authentication process|

**FingerPrintBasicCallback**

You will need this interface when you want to check wether or not the device supports this kind of authentication. The interface will provide you with the necesary methods to indicate the user why his device doesn't support this feature, or, when your device is ready to start the authentication process.

|Method | Parameters |Description|
|:-----:|:----------:|:---------:|
|onNoFingerPrintSensor|-|The device doesn't have fingerprint sensor hardware|
|onFingerPrintPermissionDenied|-|The fingerprint permission was rejected by user|
|onNoFingerPrintRegistered|-|The device doesn't have any fingerprint registered|
|onLockScreenSecurityDisabled|-|The device doesn't have any screen security method enabled (such as NIP, password or pattern)|
|onFingerPrintReady|-|If every requirement was met, the device is ready to start authentication process|

**FingerPrintAuthCallback**

Once the validation process is done, you need this interface to carry on with the authentication process, and handle errors (if raised).

|Method | Parameters |Description|
|:-----:|:----------:|:---------:|
|onAuthProcessFailed|error|The device doesn't have fingerprint sensor hardware|
|onAuthenticationError|errorCode, errString|The device doesn't have fingerprint sensor hardware|
|onAuthenticationFailed|-|The device doesn't have fingerprint sensor hardware|
|onAuthenticationHelp|helpCode, helpString|The device doesn't have fingerprint sensor hardware|
|onAuthenticationSucceeded|result|The device doesn't have fingerprint sensor hardware|
|onTooManyAttempts|errorCode, errString|The device doesn't have fingerprint sensor hardware|

### **Usage**

First of all, we need to instantiate our class.
```kotlin
...
val fingerPrintManager = FingerPrintUtils(context, mBasicListener, mAuthListener) 
```
Then, if we're gonna check user's device, we do it like this:

```kotlin
//Let's check if user's device supports fingerprint authentication
fingerPrintManager.validateFingerPrint()

override fun onNoFingerPrintSensor(){
	//Device doesn't support this authentication method
}  
override fun onFingerPrintPermissionDenied(){
	//User has explicitly rejected permission
}    

override fun onNoFingerPrintRegistered()  {
	//The user doesn't have any fingerprint registered previously
}  
override fun onLockScreenSecurityDisabled()  {
	//The user doesn't have PIN or pattern to unlock device
}  
override fun onFingerPrintReady(){
	//Device is ready to start authentication
}  
```

If everything's ok, we can go on with the process.

```kotlin
//Let's start listening for our sensor
fingerPrintManager.startAuthProcess()

override fun onAuthProcessFailed(error: Throwable)  {
	//Request another authentication method. This is unrecoverable error
}
override fun onAuthenticationError(errorCode: Int, errString: CharSequence?)  {
	//Something went wrong with authentication. We can try again
}
override fun onAuthenticationFailed(){
	//The fingerprint wasn't recognized. We can keep trying
}  
override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?){
	//This may happen when you move your finger too fast
}
  
override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult?){
	//Fingerprint was recognized. The process is over.
}  
override fun onTooManyAttempts(errorCode: Int, errString: CharSequence?){
	//There were many failed attempts. You need to try another method.
}
```
### **9.1. Fingerprint Dialog**

### **Description**

Besides all of this, this library also provides a *Fingerprint Dialog*. With this, you can forget about all the auth process, now that this dialog does it by itself. You only have to check whether or not some device can or not use this authentication. As explained above, you only have to call  *validateFingerPrint* method to make sure the device fullfill the requirements, and then you're good to go.

**Public Methods**

|Return type|Method name|Parameters|Description|
|:---------:|:---------:|:--------:|:---------:|
|FingerPrintDialog|titleEnabled|enabled|Specifies wheter or not the dialog will display the title|
|FingerPrintDialog|title|title|Set the title to the dialog|
|FingerPrintDialog|titleEnabled|enabled|Specifies wheter or not the dialog will display the title|
|FingerPrintDialog|message|message|Set the message to the dialog|
|FingerPrintDialog|cancelButton|buttonMessage|Sets the message to the cancel button|
|FingerPrintDialog|changeMethodButton|buttonMessage|Specifies the message for the button that allows to use another method as authentication method|
|FingerPrintDialog|successIcon|resId|Pick the success icon to be displayed|
|FingerPrintDialog|errorIcon|resId|Pick the error icon to be displayed|
|FingerPrintDialog|setListener|listener|Sets the interface needed to communicate the result of the process|
|FingerPrintDialog|defaultStatusMessage|statusMessage|Sets the default message to indicate the user what he needs to do to authenticate him|
|FingerPrintDialog|buttonsColor|resId|Changes the buttons color with the supplied one|

### **Usage**

```kotlin
//Create an instance of the dialog
val fingerPrintDialog = FingerPrintDialog()
//Then, set the listener and implement FingerPrintDialogCallback
fingerPrintDialog.setListener(this)
//Show the dialog to start the auth process
fingerPrintDialog.show(fragmentManager, tag)
```

You can fully customize titles, messages, and colors to fullfill your expectations. To do so, you must do it under *setupDialog* overriden method. 

```kotlin
override fun onSetupDialog() {  
  fingerPrintDialog  
	  //enable or disable dialog's title
	  .titleEnabled(true)
	  //choose the title to the dialog
	  .title(title)
	  //Show your custom message
	  .message(customMessage)
	  //Set your own message to the button
	  .cancelButton(message)
	  //Same for the changeMethod button
	  .changeMethodButton(message)
	  //Set your favorite icon to display success state
	  .successIcon(R.drawable.ic_success)
	  //And also your error starte
	  .errorIcon(R.drawable.ic_error)
	  //Change the default status message to your own
	  .defaultStatusMessage(message)
	  //Change the buttons colors
	  .buttonsColor(R.color.orange)
}
```

### **Sample**
If you do it right, you will see something like this: 

![Fingerprint Dialog](https://media.giphy.com/media/Yl9qAbq0jLKdjeKyXc/giphy.gif)


## **10. Barcode Generation**

### **Description**

This feature allows you to generate QR within ImageView in one single step. Is included as extension, so you can call it everywhere without the need to instantiate some class.

### **Usage**
```kotlin
//If you want to display barcode
ivQrCode.loadBarcode(text, BarcodeFormat.CODABAR)
//QRCode
ivQrCode.loadBarcode(text, BarcodeFormat.QR_CODE)
```

## **11. Encryption**

### **Description**

This tool helps you to encrypt/decrypt strings in a very easy way. It is defined as a part of an extension. This feature is based on [*Encryption*](https://github.com/simbiose/Encryption) library.

**Public Methods (String extension)**

|Return type|Method name|Parameters|Description|
|:---------:|:---------:|:--------:|:---------:|
|String|encrypt|-|Encrypts a string an returns the converted string, or *empty string* if something went wrong with the process |
|String|customEncrypt|key, salt, iv|Encrypts a string with custom parameters, an returns the converted string, or *empty string* if something went wrong with the process |
|String|decrypt|-|Decrypts the given string an returns the decoded string, or *empty string* if something went wrong with the process |
|String|customDecrypt|key, salt, iv|Decrypts a string with custom parameters, an returns the converted string, or *empty string* if something went wrong with the process |
|String|encryptAsync|listener*|Encrypts the given string in an asynchronous way. To get the result back, you need to provide ***CryptoUtilsCallback***|
|String|decryptAsync|listener*|Decrypts the given string in an asynchronous way. To get the result back, you need to provide ***CryptoUtilsCallback***|

**CryptoUtilsCallback**

Interface needed to get the decryption result back when requested in an asynchronous way.

|Method | Parameters |Description|
|:-----:|:----------:|:---------:|
|onSuccess|value|Returns the result of the encrypting/decrypting process|
|onError|error|Returns the error back to handle it|

### **Usage**
```kotlin
...
val password = "MyPassword"
val encryptedPassword = password.encrypt()
val decryptedPassword = password.decrypt()
```

## **12. Bonus Stuff**

### **12.1. Material Palette**

#### **Description**

The library already has all material design color palette added. All you need to do is to pick one of the colors. Below there is a table that shows how to build the name of the colors available.

| Material Design Color Name|Alpha Values Spec|Example|
|:-----:|:----------:|:---------:|
|red|[50 - 500_75]|md_red_50|
|pink|[50 - 500_75]|md_pink_100|
|purple|[50 - 500_75]|md_purple_200|
|deep_purple|[50 - 500_75]|md_deep_purple_300|
|indigo|[50 - 500_75]|md_indigo_400|
|blue|[50 - 500_75]|md_blue_500|
|light_blue|[50 - 500_75]|md_light_blue_600|
|cyan|[50 - 500_75]|md_cyan_700|
|teal|[50 - 500_75]|md_teal_800|
|green|[50 - 500_75]|md_green_900|
|light_green|[50 - 500_75]|md_light_green_A100|
|lime|[50 - 500_75]|md_lime_A200|
|yellow|[50 - 500_75]|md_yellow_A400|
|amber|[50 - 500_75]|md_amber_A700|
|orange|[50 - 500_75]|md_orange_500_25|
|deep_orange|[50 - 500_75]|md_deep_orange_500_50|
|brown|[50 - 500_75]|md_brown_500_75|
|grey|[50 - 500_75]|md_grey_800|
|blue_grey|[50 - 500_75]|md_blue_grey_800|
|falcon|[50 - 500_75]|md_falcon_800|
|black|[1000 - 1000_75]|md_black_1000_50|
|white|[1000 - 1000_75]|md_falcon_1000_75|

#### **Usage**
```kotlin
...
//To specify a light grey
tvText.setColor(ContextCompat.getColor(context, R.color.md_grey_200))
//This is some darker blue
tvText.setColor(ContextCompat.getColor(context, R.color.md_blue_800))
```

### **12.2. Usefull Extensions**

#### **12.2.1. Description**

This library also provides many usefull extensions for the most commons tasks. Look at the table below to know what you can do.

#### **12.2.2. Activity Extensions**

These extensions required an activity to be performed. You can use them to get metrics or to hide soft keyboard.

|Extension Type|Extension Name|Parameters|Description|
|:-------:|:---------:|:--------:|:------:|
|Activity|hideSoftKeyboard|view|Hides the keyboard of the input given|
|Activity|windowHeight|-|Returns the height of the current screen|
|Activity|windowWidth|-|Returns the width of the current screen|

#### **12.2.2.1 . Usage**

```kotlin
val myView = findViewById(R.id.view)
activity.hideSoftKeyboard(myView )
```

#### **12.2.3. Fragment Manager Extensions**

Are you tired of fragment transactions? Then, you may find these extensions very usefull. They simplify you code style in a very cool way.

|Extension Type|Extension Name|Parameters|Description|
|:-------:|:---------:|:--------:|:------:|
|FragmentManager|performReplacingTransaction|containerViewId, fragment, *animations**, *backStackTag**, *allowStateLoss**|Perfoms the replacing fragment transaction|
|FragmentManager|performAddingTransaction|containerViewId, fragment, *animations**, *backStackTag**, *allowStateLoss**|Perfoms the adding fragment transaction|
|FragmentManager|removeLastFragment|-|Pops up back stack|

**NOTE:** The parameters marked with (*) indicate that they're optional and you can call them only when needed.

#### **12.2.3.1. Usage**

```kotlin
...
//Simple replacing transaction
fragmentManager.performFragmentTransaction(container, yourFragment)
//Replacing transaction with custom animations
fragmentManager.performFragmentTransaction(container, yourFragment, enterAnim, exitAnim)
//Replacing transaction with back stack
fragmentManager.performFragmentTransaction(container, yourFragment, backStackTag = "MyTag")
//Replacing transaction allowing state loss
fragmentManager.performFragmentTransaction(container, yourFragment, allowStateLoss = true)
```

#### **12.2.4. View Extensions**

These extensions allow you to change the state of the views with one sinlge line of code per action. 

|Extension Type|Extension Name|Parameters|Description|
|:-------:|:---------:|:--------:|:------:|
|View|visible|-|Changes the visibility of the view to **VISIBLE**|
|View|invisible|-|Changes the visibility of the view to **INVISIBLE**|
|View|gone|-|Changes the visibility of the view to **GONE**|
|View|setDrawableBackground|drawableResId|Sets some drawable background to the view|
|View|inflate|layoutRes|Sets some color background to the view|
|View|snack|message, length|Shows a snackbar with the message and duration provided|
|ViewGroup|setBackgroundColor|drawableResId|Inflates the view with the root attached to it|
|ViewGroup|setBackgroundColor|drawableResId|Inflates the view with the root attached to it|

#### **12.2.4.1. Usage**
```kotlin
...
view.gone()
view.visible()
```

#### **12.2.5. Another Extensions**

Some other usefull extensions you can find here.

|Extension Type|Extension Name|Parameters|Description|
|:-------:|:---------:|:--------:|:------:|
|ClosedRange|random|-|Generates a random number between the clsed range provided|
|Drawable|changeDrawableColor|context, color|Changes the color of the drawable provided|

#### **12.2.6. Image Loading**

Last but not least, there's an image loading extension, and it's a very powerful one. Below you can find every variation.

|Extension Type|Extension Name|Parameters|Description|
|:-------:|:---------:|:--------:|:------:|
|ImageView|loadUrl|url, requestOptions|Loads the url especified in the image provided, with generic placeholder|
|ImageView|loadUrl|url, requestOptions, placeholder, error|Loads the url especified in the image provided, with the provided placeholder|
|ImageView|loadCircularView|url, *placeholder**|Loads the url especified in the image provided with a circular border. **Placeholder** can be provided as well|
|ImageView|loadCircularView|bitmap, *placeholder**|Loads the bitmap especified in the image provided with a circular border. **Placeholder** can be provided as well|

#### **12.2.6.1. Usage**

```kotlin
//Loads the URL within myView and sets custom placeholder
myView.loadUrl(url, R.drawable.placeholder, R.drawable.error)
//Loads the bitmap image with circular border and default placehold
myView.loadCircularView(bitmap)
```

#### **12.2.7. Version Name & Version Code**

There's also an extension that you can use to get *VersionName* and *VersionCode* of your package. 

|Extension Type|Extension Name|Parameters|Description|
|:-------:|:---------:|:--------:|:------:|
|Activity|getAppVersion|appVersion*|Gets app version or app code (depending on the enum type given) in String format|

**Note:** The *appVersion* parameter described above refers to **AppVersion** enum class. Youo can chose between *VersionCode* and *VersionName*

#### **12.2.7.1. Usage**

```kotlin
val versionName = activity?.getAppVersion(AppVersion.VersionName)
val versionCode = activity?.getAppVersion(AppVersion.VersionCode)
tvVersion = String.format("Version %s", versionName)
