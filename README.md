Program #2

Name: Kevin Littell Cosc 4735

Description: runs on Sony Xperia X Compact (F5321) physical device. Android 8.0.0. build and run.

Anything that doesn't work: not sure that there is anything that specifically doesnt work. this project was a disaster for me, so it is ugly and not implemented to the highest of standards, but it should work.

# Graded: 47/50 #

* Last location updates are handled when the camera is opened, which creates issues with marker placement when pictures are cancelled. *(-3 points)*

**For example:**

**Test Case:**
Open app at the Engineering Building. Open the camera, but do not take a picture and back out of the camera. Walk over to the Union, take a picture. Walk to the Ben Franklin statue and take a picture

**Result:**
Picture at the Union shows as though it was taken at Engineering and picture of the Ben Franklin statue shows as though it was taken at the Union.

While it may seem intuitive to only get a location when it is needed (i.e. when you want to take a picture and place a marker), it is best just to recieve a constant stream of updating locations on a set interval. Alternatively, you just need to ensure that you are handling the case that no picture is taken. For some reason it still wants to use a location even if you are updating it again the next time you open the camera.

Overall, everything works perfectly if you are only testing for the ideal use case (i.e. everytime the user opens the camera you assume that a picture is taken), but it is important to account for any strange things the user might do when using the app. These are the weird sort of things I will be looking at when testing your apps.
