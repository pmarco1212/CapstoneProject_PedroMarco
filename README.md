# About EventApp
This app allows users to know about events happening around them as well as create their own events for other people to assist. Events can be anything, from the range of study groups to parties, sports… The application provides the nearby events around the user shorted by distance. Also contains a search engine to search event by different fields. Events can be stored in favorites and can be rated (only if the user is at the event location).

## APIs, dependencies and third-party libraries:
-	Firebase
-	Algolia
-	Facebook Login
-	Twitter Login
-	Cloudinary
-	Glide Image Loader
-	EventBus
-	Google Play Services - Places
-	Dagger 2
-	Butterknife
-	CircleImageView (hdodenhof)
-	Image-Cropper (theartofdev)

This application is built using the Model-View-Presenter model, with Interactor classes as well. The application uses Firebase for handling the login. Also uses Firebase database to store all the app data (Users and Events). It also uses Algolia database to store a copy of all the Events, in order to use Algolia search engine when searching (due to the limited posibilites of searching inside Firebase). It stores all the images in Cloudinary.

## Javadoc with all classes and methods descriptions:
-	Download the file “Javadoc.rar”, unzip in your computer, and open the “index.html” file.

## Known issues:
-	Facebook login is not working properly, seems like an issue of the app being not published.
