
# **README**

## **StudyWithME**

**StudyWithME** is an Android application designed to help users organize their study sessions, set goals, and track their progress efficiently. It provides a simple and interactive interface for managing study schedules and goals.

---

## **1. Features**

1. **User Authentication**:
   - Sign up and log in securely.
   - Password reset functionality.

2. **Dashboard**:
   - Central hub to navigate through the app.
   - Access study sessions and goals.
   - Animations for an engaging user experience.

3. **Study Session Management**:
   - Add, view, and delete study sessions.
   - Track details like subject, date, time, duration, and description.

4. **Goal Tracking**:
   - Add, view, and delete goals.
   - Monitor completion status and due dates.

5. **Utility Functions**:
   - Database integration for persistent storage of sessions and goals.
   - Email notifications for important updates.

6. **User Experience Enhancements**:
   - Bounce and fade-in animations.
   - Alerts and welcome messages for better user engagement.

---

## **2. Installation**

1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```
2. Open the project in **Android Studio**.
3. Sync the project with Gradle files.
4. Run the app on an emulator or a physical device.

---

## **3. Tech Stack**

- **Language**: Java
- **Database**: SQLite
- **Frameworks**: Android SDK
- **Build System**: Gradle

---

## **4. File Structure**

### **4.1 Activities**
- `MainActivity`: Entry point, redirects to LoginActivity.
- `DashboardActivity`: Central navigation for managing sessions and goals.
- Other activities include `GoalActivity`, `LoginActivity`, `SignUpActivity`, etc.

### **4.2 Utilities**
- `EmailSender.java`: Sends email notifications.
- `PasswordHasher.java`: Securely hashes user passwords.

### **4.3 Resources**
- **Layouts**: XML files for UI design (`activity_dashboard.xml`, etc.).
- **Drawables**: Assets for UI elements (`swme_logo.png`, etc.).
- **Animations**: Bounce and fade-in effects (`bounce.xml`, `fade_in.xml`).

### **4.4 Database**
- `DBHelper.java`: Handles SQLite database operations for storing study sessions and goals.

---

## **5. Permissions**

The app requires the following permissions:
- **Internet**: For connectivity features.
- **Notifications**: To notify users about session and goal updates.

---

## **6. Contributors**

This project is made possible thanks to the contributions of:
- **Albin Shala**
- **Albion Ahmeti**
- **Agnesa Hulaj**

---

## **7. How to Contribute**

1. Fork the repository.
2. Create a new feature branch:
   ```bash
   git checkout -b feature-branch
   ```
3. Commit your changes and open a pull request.

---

## **8. License**

This project is licensed under the **MIT License**.

---

