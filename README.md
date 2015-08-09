# EasyPark
Copyright © 2015 Swetha Venkatachari Sundarajan

An android application which helps people in finding the nearest vacant parking spot. 

### Project Description
This app helps users in finding the free or paid vacant parking spots near them. In the Free Parking, users get to see a list of vacant parking spots that are near them and also get direction to their parking spot. The app also allows users to enter the free parking spots found by them and populate the database for rest of the user community. Thus, if you are a user of this app and find vacant parking spots near you, you can key in the number of vacant parking spots. The app records your current location automatically. You can also drag the marker in the google map and point it to a different location, if needed.<BR>
 <t/><t/>        In the paid parking, users can search for parking lots by entering radius or zip code and choose the time of parking. Based on the availability of parking lots, users are provided with two views map view and list view. On click of the map view or the list view , the parking spots in the corresponding parking lot are listed. Users can choose the convenient parking spot and block it. The application also allows users to mark their spot and the app will automatically notify the user 10 minutes before the expiry of the parking ticket. In addition, the app provides driving directions and estimated time of travel from the current location to the parking lot.

###Technical Details
1.This app was  developed in  Android with API target 22 (5.1.1) Lollipop and tested in Lollipop (5.0.1) <br>
2. It uses WebAPI developed in PHP/MySQL in the backend to store and retrieve parking data. The WebAPI is hosted in http://www.000webhost.com/.<br>
 Website name: http://easypark.net46.net/easypark/ <br>
3.SQLite to store Login related info(username and password) 

###Source Code
https://github.com/swethavs/EasyPark

###License Information
https://github.com/swethavs/EasyPark/blob/master/LICENSE

###Bug or Issue Tracker 
You can report the bugs in the following link: https://github.com/swethavs/EasyPark/issues

### Future Enhancements
1. Add verification number after user blocks the parking  spot<BR>
2. Get real time parking data for paid parking<BR>
3. Integrate with a pay service<BR>
4. Introduce reward points to  increase the number of customers

### References
1. DateTimePicker: http://custom-android-dn.blogspot.com/2013/03/how-to-create-custom-date-time-picker.html <BR>
2. GoogleMaps: https://developers.google.com/maps/documentation/directions/intro ,<BR>
 https://developers.google.com/maps/documentation/android/start#getting_the_google_maps_android_api_v2<BR>
3. PHP: http://www.w3schools.com/php<BR>
4. MySQL Haversines Formula to calculate radius:<Br> http://stackoverflow.com/questions/574691/mysql-great-circle-distance-haversine-formula


### User Docs
You can refer the user manaul documnet to get build instructions and prerequisites of the project

#####Contact Information
You can email to vswetha@pdx.edu if you have any queries on this project
