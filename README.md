# TrainingPage user-service

## Description 

A fitness application built with multiple microservices to help you with planning, logging and improving your home training.
The system is a microservice application built with Spring Boot, React, and PostgreSQL and containerized using Docker for easy setup and deployment.

User-service is handling registration, login and logout of a user. The service uses BCryptPasswordEncoder and JWT Authentication for safe user handling.


## Technology stack

* Java Spring Boot
* Eureka Client
* PostgreSQL
* Spring Security (with BCrypt for password hashing)
* JWT (JSON Web Tokens)

## Endpoints

| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :--- |
| POST | /user/register | Register a new user | No |
| POST | /user/login | Login a user | No |
| GET | /user/all | Fetch list of all registered users | Yes |
| GET | /user/id/{id} | Fetch user profile details  | Yes |
| POST | /user/logout | Logout a user | Yes |


## Setup

This service is designed to be run as part of the Docker Compose cluster.
For instructions on how to start the project go to the readme file at [trainingpage](https://github.com/TheSteinkamp/trainingpage.git)
