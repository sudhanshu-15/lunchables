### Lunchables - Restaurant Search App

#### Libraries Used:

* Networking:
  
  * [Retrofit]([Retrofit](https://square.github.io/retrofit/)) - Network calls
  * [Moshi]([GitHub - square/moshi: A modern JSON library for Kotlin and Java.](https://github.com/square/moshi)) - Json conversion

* [Koin](https://insert-koin.io/) - Dependency Injection

* [Quickpermissions]([GitHub - QuickPermissions/QuickPermissions-Kotlin: The most easiest way to handle Android Runtime Permissions in Kotlin](https://github.com/QuickPermissions/QuickPermissions-Kotlin))

* Android Architecture Components - Livedata, Viewmodels, Room

#### Architecture:

The App uses MVVM architecture. The UI is driven by different states:

* For changing between map and list fragment.

* For handling permission denial errors.

* For loading data on map and list, including api errors



#### TODO:

- [ ]  Add unit tests for repository and viewmodels

- [ ]  Add UI tests

- [ ]  Add filtering logic

- [ ]  Better handle network availablility