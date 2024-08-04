# Demo Project For A Simple CRUD Application

## Introduction
This project is a simple CRUD application build with SpringBoot and MySQL.
It simulated a simple order system, where you can create, update and query orders.
It also implemented a Google Map API to get the estimate distance between two coordinates.

## How To Run
First you need to install Docker in your machine.
After that you need to get an google maps api key from Google 
and replace the key in the application.yml file(google.map.distance.key=YOUR_GOOGLE_API_KEY).
Then cd to the project root directory and run the following command in your terminal:
1. chmod +x start.sh
2. ./start.sh

Then you can access the application at http://localhost:8080
- Create an Order 
```bash
curl --location --request POST 'localhost:8080/orders' \
--header 'Content-Type: application/json' \
--data-raw '{
    "origin": ["37.7663444", "-122.4412006"],
    "destination": ["37.7680296", "-122.4379126"]
}'
```
- Take an Order
```bash
curl --location --request PATCH 'localhost:8080/orders/1'
```
- Query Orders
```bash
curl --location --request GET 'localhost:8080/orders?page=1&limit=10' 
```

## Limitations
This only designed for demo purpose, so don't use it in your production environment.
The application is not designed for high availability, so it may not work properly when there are many requests.
As for the race condition, it is only partially handled, 
thus, only in single node environment,
if you want to use it in a cluster environment, 
you need to do some extra work, for example, implement a distributed lock for take order endpoint.



