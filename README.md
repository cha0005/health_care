🩺 **MedLink HealthCare — Smart Medical Assistant App**

MedLink HealthCare is a modern Android application designed to help users to manage their medical life intelligently. It combines appointment scheduling, reminder management, and AI-driven health insights to keep users organized and motivated in maintaining a healthy lifestyle.
<img width="700" height="704" alt="image" src="https://github.com/user-attachments/assets/e51c2c65-4aaf-4964-a886-1136489774c8" />

🚀 **Key Features**
🕒 **Smart Reminders:-**

1. Set and manage medication or checkup reminders easily.

2. Get notifications at precise times — even when the app is closed.

3. Supports Android 12+ with exact alarm permissions.

📅 **Appointment Booking:-**

1. Schedule medical appointments directly through the app.

2. Track upcoming appointments in one place.

🧠 **AI Health Insights:-**

1. Powered by TensorFlow Lite, MedLink monitors user behavior patterns:

    a. Number of reminders created & completed
    
    b. Skipped reminders
    
    c. Booked appointments

2. The app predicts your Health Persona:

    🟢 Proactive – highly consistent and engaged
    
    🟡 Casual – moderate engagement
    
    🔴 Needs Motivation – needs regular prompts

3. Displays personalized health tips using the AI model.

📊 **Progress Visualization:-**

- Visualize activity trends through a Line Chart powered by MPAndroidChart.

- Track consistency across reminders, completions, and bookings.

☁️ **Firebase Integration**

-> Real-time data synchronization via Firebase Firestore & Authentication.

-> Secure login and personalized analytics based on user UID.

🧩 **Tech Stack**
       **Category**          **Tools & Frameworks**
      1. Language:	            Java
      2. UI Design:	            XML, Material Design
      3. Database:	            Firebase Firestore, Realtime Database
      4. Authentication:	      Firebase Auth
      5. AI & ML:	              TensorFlow Lite
      6. Charts:	              MPAndroidChart
      7. Image Loading:	        Picasso (Displaying Profile Image [loaded from the firebase])
      8. Notifications:	        AlarmManager, BroadcastReceiver
      9. Other Libraries:	      CircleImageView, RecyclerView, CardView
     
🧠 **AI Model Overview**

The app includes a lightweight TensorFlow model trained on simulated user behavior data.

**Training Features:**
**Feature**	                       **Description**

remindersCreated	                   Total reminders created by the user
remindersCompleted	                 Completed reminders
appointmentsBooked	                 Total appointments
skippedReminders	                   Missed or snoozed reminders
averageResponseTime	                 Average user response time to notifications

**Memebers ->**
https://github.com/cha0005 ,
https://github.com/Chathuni-07,
