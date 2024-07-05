# PalatePal

PalatePal is an idea pitch for students to share their favorite dishes found at a random dining hall in real-time with their social circle.

## Table of Contents
1. [Overview](#Overview)
2. [Features](#Features)
3. [Contributors](#Contributors)

   
## Overview
### Why PaletePal?

- Motivation: food quality at MSU dining halls is often of inconsistent quality
- Crowdsource reviews so that students can know if the dish will be good before trying it 
- Reduces food waste and allows students to enjoy food they like


### Targeted Users

- Students at Michigan State University
- Visitors at Michigan State University
- Anyone who eats at University dining halls
- REHS so that they can measure how their dishes are being perceived


## Features

1. [Log in/Register](#Login/Register)
2. [User Profile](#UserProfile)
3. [Main Page](#MainPage)
4. [Reviews](#Reviews)
5. [Map Functionality](#MapFunctionality)


### Login/Register
<img width="343" alt="image" src="https://github.com/shanepat/PalatePal/assets/11996311/1b9b618b-f878-4db9-a3f4-83e18c6a90fe">

- First screen of the app (Authenticated with Firebase)
- Username, email address, and password will be sent to the real-time database
- When the checker box is checked, the user remains logged in even when they close and rerun the app
- Login screen(success) -> Main activity
- When the user presses the logout button in the navigation bar, the user will be redirected to the login screen
  
### User Profile
<img width="344" alt="image" src="https://github.com/shanepat/PalatePal/assets/11996311/15447742-8384-44cf-9072-72894e5c08f2">

- Username, email address, and passwords are displayed
- User can change email address and password
- Press the apply button will update the user’s email address and password
- After pressing the button it will move to the main activity

### Main Page
<img width="171" alt="image" src="https://github.com/shanepat/PalatePal/assets/11996311/49b32b2a-fbd3-4714-a4a5-97426d270301">

- Hub of the app from where you can get to anywhere in the app
- Easy to use UI to access dining halls to see reviews on food items or leave reviews.
- Users can access the My Account page to see the details of your account
- Access the map page to see all the dining halls that are  listed

### Reviews
<img width="168" alt="image" src="https://github.com/shanepat/PalatePal/assets/11996311/47ebef20-1333-454b-ba27-79e8a520865c">

- Uses Firebase NoSQL server to store all app information in JSON format
- Workflow goes Dining Hall > Meal Time > Specific Dish > View/Create Review
- When a meal is selected all available reviews are displayed
- When Create Review is selected the user can add a review and it is added to the DB

### Map Functionality
<img width="337" alt="image" src="https://github.com/shanepat/PalatePal/assets/11996311/d3b39dc0-ece5-4680-b3c1-b26d5726f2b3">

- All dining halls listed on the maps
- Navigation feature when dining is selected


## Contributors
Shane, Emmett, Jiwoo, Mannan, Zachary







