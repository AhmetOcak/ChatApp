# Jetpack Compose - Chat App 💬💬💬
![ic_launcher](https://github.com/user-attachments/assets/03389514-d809-4f02-aa4e-cfbd5cc1de96)

The application is a Chat App developed using Jetpack Compose and follows a Modular architecture. To log into the app, users must create an account or sign in directly using a Google account. Authentication processes are handled through Firebase Auth. Users can update their profile picture or username, and if desired, delete their account. The app allows real-time messaging with individuals or creating group chats with friends for instant messaging. It supports text, PDF, image, and voice recording types. Within the app, you can also take photos using the built-in camera feature and share them. The notification feature is provided through Firebase Cloud Messaging. Even when you are not using the app, incoming messages are displayed to you via notifications.

## Features
* <b>Authentication</b>: Allows users to create an account or log in directly with a Google account using Firebase Authentication.
* <b>Profile Management</b>: Users can update their profile picture or username and delete their account if desired.
* <b>Instant Messaging</b>: The app enables real-time messaging with an individual or group chats with friends.
* <b>File Sharing</b>: Supports text, PDF, image, and voice recording sharing.
* <b>Camera Integration</b>: Users can take photos using the in-app camera and share them instantly.
* <b>Notifications</b>: Receive real-time message notifications via Firebase Cloud Messaging, even when the app is not in use.

## Tech Stack 📚

* [Android Architecture Components](https://developer.android.com/topic/architecture)
    * [Navigation](https://developer.android.com/guide/navigation)
    * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
    * [Repository](https://developer.android.com/topic/architecture/data-layer?hl=en)
* [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
* [Room](https://developer.android.com/training/data-storage/room)
* [Retrofit](https://github.com/square/retrofit)
* [Coil](https://github.com/coil-kt/coil)
* [Okhttp](https://square.github.io/okhttp/)
* [Firebase Auth](https://firebase.google.com/docs/auth)
* [Firebase Storage](https://firebase.google.com/docs/storage?hl=en)
* [Firebase Firestore](https://firebase.google.com/docs/firestore?hl=en)
* [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
* [Datastore](https://developer.android.com/topic/libraries/architecture/datastore)
* [Splash API](https://developer.android.com/develop/ui/views/launch/splash-screen)
* [Google Sign In](https://firebase.google.com/docs/auth/android/google-signin)
* [Accompanist Permissions](https://google.github.io/accompanist/permissions/)
* [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging)
* [Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization)
* [Lottie Compose](https://github.com/airbnb/lottie/blob/master/android-compose.md)
* [Constraint Layout](https://developer.android.com/develop/ui/compose/layouts/constraintlayout)
* [CameraX](https://developer.android.com/media/camera/camerax)

## Outputs 🖼

|                | Dark Theme | Light Theme |
|----------------|------------|-------------|
| Login          | <img src="https://github.com/user-attachments/assets/a8955e94-e005-4149-9a1d-6f7f846dcb22" width="240" height="480"/>           | <img src="https://github.com/user-attachments/assets/edd932b5-c20f-43f6-a7d2-e1ad0acd044e" width="240" height="480"/>            |
| Sign Up        | <img src="https://github.com/user-attachments/assets/43610975-09d7-4320-b173-4fb570fee6a7" width="240" height="480"/>           | <img src="https://github.com/user-attachments/assets/7257b6a0-3874-407d-a539-3627abe2d7ab" width="240" height="480"/>            |
| Chats          | <img src="https://github.com/user-attachments/assets/293f86d7-941b-41e0-812b-9a868a2e3512" width="240" height="480"/>           | <img src="https://github.com/user-attachments/assets/7545cd79-f4bc-4656-90b8-167d3edfb8fb" width="240" height="480"/>            |
| Chat Box       | <img src="https://github.com/user-attachments/assets/474c0bd9-979a-4b40-9056-3110855e5770" width="240" height="480"/>           | <img src="https://github.com/user-attachments/assets/ebffe254-3a08-474d-8f23-7b3614ed4217" width="240" height="480"/>            |
| Profile        | <img src="https://github.com/user-attachments/assets/2df690d7-ae08-410c-ae88-935419dca1b0" width="240" height="480"/>           | <img src="https://github.com/user-attachments/assets/40d25b12-0243-4f8c-9b36-f11801d968ac" width="240" height="480"/>            |
| Create Contact | <img src="https://github.com/user-attachments/assets/8a15b57e-9275-4b1e-9d18-6706ff4c604b" width="240" height="480"/>           | <img src="https://github.com/user-attachments/assets/0b443070-841d-4a1f-ae3e-dd738f62b1d3" width="240" height="480"/>            |
| Create Group   | <img src="https://github.com/user-attachments/assets/bf8fa6a2-3c43-4bee-86a3-1005429d4a32" width="240" height="480"/>           | <img src="https://github.com/user-attachments/assets/a04d76cb-8863-4c03-aae0-88f1b6fad9fd" width="240" height="480"/>            |
| Settings       | <img src="https://github.com/user-attachments/assets/e9ce4e9e-14f6-4d92-94df-1c31eceb8058" width="240" height="480"/>           | <img src="https://github.com/user-attachments/assets/0559e0b2-97a4-40c6-85b3-8051dd50bb3a" width="240" height="480"/>            |

## Modularization 📦
<img src="https://github.com/user-attachments/assets/591c59b6-94f9-4e62-bc22-c5fbb9e810c3" />

<table>
  <tr>
   <td><strong>Name</strong>
   </td>
   <td><strong>Responsibilities</strong>
   </td>
   <td><strong>Key classes</strong>
   </td>
  </tr>
  <tr>
   <td><code>app</code>
   </td>
   <td>Brings everything together required for the app to function correctly. This module responsible for navigation.
   </td>
   <td><code>ChatApp, MainActivity</code><br>
   </td>
  </tr>
  <tr>
   <td><code>feature:1,</code><br>
   <code>feature:2</code><br>
   ...
   </td>
   <td>Functionality associated with a specific feature or user journey. Typically contains UI components and ViewModels which read data from other modules.<br>
   Examples include:<br>
   <ul>
      <li><a href="https://github.com/AhmetOcak/ChatApp/tree/master/feature/chats"><code>feature:chats</code></a> On the Chats screen you can view your friends, add friends or create groups.</li>
      </ul>
   </td>
   <td><code>ChatsScreen</code><br>
   <code>ChatsViewModel</code>
   </td>
  </tr>
  <tr>
   <td><code>core:data</code>
   </td>
   <td>Fetching app data from multiple sources and sends it to the UI through the <code>core:domain</code> module.
   </td>
   <td><code>ChatRepository</code><br>
   </td>
  </tr>
  <tr>
   <td><code>core:designsystem</code>
   </td>
   <td>Design system which includes Core UI components (many of which are customized Material 3 components), app theme and icons.
   </td>
   <td>
   <code>ChatAppModalBottomSheet</code>    <code>ChatAppButton</code>    <code>ChatAppTheme</code> 
   </td>
  </tr>
  <tr>
   <td><code>core:ui</code>
   </td>
   <td>Composite UI components and resources used by feature modules. Unlike the <code>designsystem</code> module, it is dependent on the data layer since it renders models. 
   </td>
   <td> <code>ChatItem</code>
   </td>
  </tr>
  <tr>
   <td><code>core:common</code>
   </td>
   <td>Common classes shared between modules.
   </td>
   <td><code>UiText</code><br>
   <code>Response</code>
   </td>
  </tr>
  <tr>
   <td><code>core:network</code>
   </td>
   <td>Making network requests and handling responses from a remote data source.
   </td>
   <td><code>KtorChatApi</code>    <code>MessagesRemoteDataSource</code>
   </td>
  </tr>
  <tr>
   <td><code>core:datastore</code>
   </td>
   <td>Storing persistent data using DataStore.
   </td>
   <td><code>AppPreferencesDataSource</code><br>
   </td>
  </tr>
  <tr>
   <td><code>core:database</code>
   </td>
   <td>Local database storage using Room.
   </td>
   <td><code>UserDatabase</code><br>
   <code>Dao</code> classes
   </td>
  </tr>
   
  <tr>
   <td><code>core:model</code>
   </td>
   <td>Model classes used throughout the app.
   </td>
   <td><code>User</code><br>
   <code>UserChat</code><br>
   <code>Message</code>
   </td>
  </tr>
   
  <tr>
   <td><code>core:domain</code>
   </td>
   <td> It houses use cases. It serves as a bridge between the data layer's repositories and the UI.
   </td>  
     <td> <code>GetMessagesUseCase</code>
   <code>UpdateUserUseCase</code>
   </td>
  </tr>

<tr>
   <td><code>core:authentication</code>
   </td>
   <td> Manages user identity verification and access control.
   </td>  
     <td> <code>GoogleSignInClient</code>
        <code>FirebaseEmailPasswordClient</code>
   </td>
  </tr>
</table>

## Architecture 🏗
The app uses MVVM [Model-View-ViewModel] architecture to have a unidirectional flow of data, separation of concern, testability, and a lot more.

![mvvm](https://user-images.githubusercontent.com/73544434/197416569-d42a6bbe-126e-4776-9c8f-2791925f738c.png)
