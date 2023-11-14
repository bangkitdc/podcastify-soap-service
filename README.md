# Podcastify SOAP Service

Podcastify SOAP Service is a comprehensive service designed to handle incoming subscription requests from the Podcastify App (Monolith). It serves the Podcastify REST Service by providing subscription data and facilitating the approval or rejection of subscriptions. Additionally, it sends notification emails to the admin for every incoming subscription from the Podcastify App (Monolith). It also includes a logging service that saves logs to the Podcastify SOAP service’s database.

## Functionality
1. <b>Incoming Subscriptions</b> </br> This service handles incoming subscription requests from the Podcastify App (Monolith).
2. <b>Subscription Status Update</b> </br> It provides an updateStatus function to update the subscription status (reject or accept) for the Podcastify REST service. When the status is updated, it sends a request to the Podcastify App (Monolith) to trigger push notifications, informing the user that the status has been updated to either rejected or accepted.
3. <b>Subscription Data</b> </br> The Podcastify SOAP service provides subscription data to the Podcastify REST service. This includes the types of incoming subscriptions. The Podcastify REST service can retrieve all subscriptions, retrieve subscriptions by creator ID or subscriber ID, and specify the status of the subscriptions it wants to retrieve.
4. <b>Notification Emails</b> </br> For every incoming subscription from the Podcastify App (Monolith), the Podcastify SOAP service sends a notification email to the administrator.
5. <b>Logging and Authentication</b> </br> The Podcastify SOAP service includes a logging service that saves logs to its own database, providing a record of transactions and events within the Podcastify SOAP service itself. This service also performs an authentication process by checking the API key of every incoming request.

## DB Schema
[Belom]

## API Endpoint
Please refer here [link postman] to get the full versions of the endpoints.

### Subscription
|Method| URL | Explanation | Consumer |
|:--:|:--|:--|:--:|
| POST | /subscription | Base Subscription Endpoint | REST & Monolith |

## SEI
|Method Name| Param | Explanation | Consumer |
|:--|:--|:--|:--:|
| subscribe | subscriber_id, creator_id, subscriber_name, creator_name | Sends a subscription request to a specific creator | Monolith |
| updateStatus | subscriber_id, creator_id, creator_name, status | Updates the status of a subscription request for a specific subscriber_id and creator_id | REST |
| getStatus | subscriber_id, creator_id | Retrieves the status for a specific subscriber_id and creator_id | REST |
| getSubscriptionBySubscriberID | subscriber_id, status | Retrieves subscription data by subscriber ID with a specified status | REST |
| getSubscriptionByCreatorID | creator_id, status | Retrieves subscription data by creator ID with a specified status | REST |
| getAllSubscriptions | - | Retrieves all subscription data | REST |

## Tech Stacks  
1. Docker
2. OpenJDK-8
3. JAX-WS
4. Java Mail

## How to Get Started
1. Clone this repository
2. Copy the `.env.example` file and rename it to `.env`:
```bash
    cp .env.example .env
```
3. Open the `.env` file and replace the placeholder values with your actual data.
4. On the root of this project, run the following commands:
```bash
    docker-compose up -d --build
```
5. To shut down the app, run
```bash
    docker-compose down
```
6. Ensure that the Docker Daemon is running

## Tasking
| 13521055                            | 13521072                                    | 13521102                   |
| :---------------------------------- | :------------------------------------------ | :------------------------- |
| Setup Docker, DB, and Structure     | Subscription Data Retrieval (by creator ID) | Setup DB and Structure     |
| Email Notification                  |                                             | Subscription Request       |
|                                     |                                             | Subscription Update        |
|                                     |                                             | Subscription Data Retrieval|
|                                     |                                             | Logging                    |
|                                     |                                             | Authentication             |

## Copyright
2023 © Podcastify. All Rights Reserved.
