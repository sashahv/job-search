# Job-Search RESTful Spring Application

## Overview
This project is a job-search web application that allows users to find and apply for jobs while providing companies with a platform to post job openings and manage their hiring teams. The application is built using Spring and follows the RESTful architectural style.

## Features
Authentication and Security
The application implements JWT-based authentication and authorization to ensure that only authenticated users can access protected resources. Users can recover and change their passwords using the provided functionality.

#### Company Management
Every user can create a new company and add a hiring team to it. This allows companies to manage their job postings and applications using a dedicated team.

### Job Search and Application
Users can find jobs by applying filters, view all available jobs, and apply for a chosen job by attaching their CV.

### Application Management
Recruiters can view all applications for a specific company and job opening. When a recruiter looks at an application, the user automatically receives an email notification. Recruiters can change application status to decline, considered, or make other changes, and the user will receive an email notification accordingly.

### Job Expiration and Deletion
The application has a 30-day job expiration policy. After 30 days, the job will be automatically closed. Every night at 00:00, the system checks and deletes all expired jobs and marks them as inactive. Inactive jobs will be deleted in 90 days.

### Application Deletion
The system checks for declined and closed applications every night at 00:00 and deletes them if they exist for more than 30 days.

### User Profile Management
Users can add their skills, work experience, education, language proficiency, certificates, and a default CV to their profile.

### User Connection List
Users can find other users and invite them to their connection list.

### Premium Accounts
Users can upgrade their accounts to premium, which provides their company's jobs and applications with priority placement.

### Architecture
The application follows the RESTful architectural style and uses Spring, Spring Security, and JWT for authentication and authorization. It also uses Spring Data JPA and Hibernate for persistence and data access.

## Installation and Setup
Clone the repository
Import the project into your IDE as a Maven project
Set up a PostgreSQL database
Update the application.properties file with the appropriate database connection details
Run the application using mvn spring-boot:run
Contributing
Pull requests are welcome. Please make sure to update tests as appropriate.

## License
MIT
