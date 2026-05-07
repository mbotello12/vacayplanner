# D308 Vacation Plan

## Overview

D308 Vacation Plan is a native Android mobile application designed to help users organize vacations and manage associated excursions through a centralized travel planning interface.

The application was developed as a portfolio and academic project focused on mobile application architecture, relational database integration, CRUD operations, user validation, and Android lifecycle management using Java and Room persistence.

The primary focus of this project was Android application development, Room database implementation, entity relationship management, mobile UI design, and local notification scheduling using Android system services.

---

## Application Type

Native Android mobile application with local database persistence.

---

## Core Stack

* Java
* Android SDK
* Android Studio
* Room Persistence Library
* SQLite
* RecyclerView
* XML Layouts
* AlarmManager
* BroadcastReceiver
* Material Design Components

---

## Android Deployment

* Minimum SDK: Android 7.0 (API 24)
* Target SDK: Android 16 / API 36
* Signed APK generated for Android deployment testing

---

# Technical Implementation

1) Mobile Application Architecture

* Multi-activity Android application structure
* RecyclerView-based dynamic list rendering
* Intent-driven navigation between activities
* Material Design UI components
* XML-based responsive mobile layouts
* Activity lifecycle management
* Form-based CRUD workflows
* Floating Action Button navigation implementation

---

2) Database Architecture

* Room persistence library implementation
* SQLite local database integration
* DAO-based database access structure
* Repository pattern implementation
* Entity relationship management using foreign keys
* Background database threading using ExecutorService
* Persistent local data storage

### Entity Relationships

* Vacations
* Excursions

Each excursion is associated with a parent vacation through foreign key relationships and relational database constraints.

---

3) Notification & Alert System

* AlarmManager-based scheduling system
* BroadcastReceiver notification handling
* Vacation start date alerts
* Vacation end date alerts
* Excursion date alerts
* System notification integration

---

4) Validation & Business Logic

* Date format validation using SimpleDateFormat
* Excursion date validation within vacation date range
* Required field validation
* Delete-blocking validation preventing vacation deletion when excursions exist
* Update and insert workflow differentiation
* CRUD operation handling through Room DAOs

---

## Features

* Vacation management
* Excursion management
* Add, update, and delete vacations
* Add, update, and delete excursions
* Vacation-to-excursion relationship mapping
* Detailed vacation views
* Detailed excursion views
* Vacation sharing functionality
* Start and end vacation alerts
* Excursion alerts
* Local database persistence
* RecyclerView dynamic list rendering
* Input validation and business rule enforcement
* Responsive Android mobile UI

---

## Application Screens

* Home screen
* Vacation list screen
* Vacation detail screen
* Excursion list display within vacations
* Excursion detail screen
* Alert and sharing menu options

---

## Academic Concepts Demonstrated

* Android application lifecycle
* Object-oriented programming in Java
* Relational database modeling
* Foreign key relationships
* Repository architecture pattern
* Room database integration
* CRUD operations
* Android notifications and broadcasts
* User input validation
* Mobile UI/UX workflows
